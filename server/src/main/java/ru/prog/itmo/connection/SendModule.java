package ru.prog.itmo.connection;

import org.apache.logging.log4j.Level;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.concurrent.*;

import static ru.prog.itmo.control.ClientListener.LOGGER;

public class SendModule {
    private boolean isNotShutdown;
    private final ConcurrentHashMap<SocketAddress, Response<?>> responseMap;
    private static final byte STOP_BYTE = 0x60;
    private static final byte CONTINUE_BYTE = 0x4E;
    public static final int PACKET_SIZE = 4096;
    public static final int PAUSE_BETWEEN_SENDING = 1;

    public SendModule(ConcurrentHashMap<SocketAddress, Response<?>> responseMap) {
        this.responseMap = responseMap;
        isNotShutdown = true;
    }

    public void submitSendTask(SocketAddress address) {
        if (isNotShutdown) {
            var sendThread = new Thread(sendTask(address));
            sendThread.start();
        }
    }

    public void submitSendTask(Future<SocketAddress> task) {
        try {
            SocketAddress address = task.get();
            if (isNotShutdown) {
                var sendThread = new Thread(sendTask(address));
                sendThread.start();
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.INFO, "Отправка ответа пользователю прервана.");
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARN, e.getMessage());
        }
    }

    private Runnable sendTask(SocketAddress address) {
        return () -> {
            try {
                DatagramChannel datagramChannel = DatagramChannel.open();
                Response<?> response = responseMap.get(address);
                responseMap.remove(address);
                sendResponse(response, datagramChannel, address);
                datagramChannel.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARN, "Не удалось отправить сообщение клиенту.");
            }
        };
    }

    private void sendResponse(Response<?> response, DatagramChannel datagramChannel, SocketAddress address) {
        try {
            ArrayList<ByteBuffer> byteBuffers = getPackets(response);
            sendPackets(byteBuffers, datagramChannel, address);
        } catch (IOException e) {
            String message = "Не удалось отправить ответ клиенту.";
            LOGGER.log(Level.WARN, message);
            throw new InvalidConnectionException(message);
        }
    }

    private void sendPackets(ArrayList<ByteBuffer> byteBuffers,
                             DatagramChannel datagramChannel,
                             SocketAddress address) throws IOException {
        for (ByteBuffer toClient : byteBuffers) {
            datagramChannel.send(toClient, address);
            LOGGER.log(Level.INFO, "Отправлен пакет по адресу: " + address);
            try {
                TimeUnit.MILLISECONDS.sleep(PAUSE_BETWEEN_SENDING);
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO, "Пауза между отправками прервана.");
            }
        }
    }

    private ArrayList<ByteBuffer> getPackets(Response<?> response) throws IOException {
        var baos = getSerializedResponse(response);
        int packetsCount = getPacketsCount(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return getChunks(bais, packetsCount);
    }

    private int getPacketsCount(ByteArrayOutputStream baos) {
        byte[] bytesToSend = baos.toByteArray();
        int packetsCount = bytesToSend.length / (PACKET_SIZE - 1);
        if (bytesToSend.length % (PACKET_SIZE - 1) != 0)
            packetsCount++;
        return packetsCount;
    }

    private ByteArrayOutputStream getSerializedResponse(Response<?> response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);
        return baos;
    }

    private ArrayList<ByteBuffer> getChunks(ByteArrayInputStream bais, int packetsCount) {
        ArrayList<ByteBuffer> byteBuffers = new ArrayList<>();
        for (int i = 1; i <= packetsCount; i++) {
            byte[] currentBytes = getCurrentBytes(bais);
            setControlByte(currentBytes, bais);
            ByteBuffer buffer = ByteBuffer.wrap(currentBytes);
            byteBuffers.add(buffer);
        }
        return byteBuffers;
    }

    private void setControlByte(byte[] currentBytes, ByteArrayInputStream bais) {
        if (bais.available() > 0) {
            currentBytes[PACKET_SIZE - 1] = CONTINUE_BYTE;
        } else {
            currentBytes[PACKET_SIZE - 1] = STOP_BYTE;
        }
    }

    private byte[] getCurrentBytes(ByteArrayInputStream bais) {
        byte[] currentBytes = new byte[PACKET_SIZE];
        for (int j = 0; j < PACKET_SIZE - 1; j++) {
            if (bais.available() > 0)
                currentBytes[j] = (byte) bais.read();
            else break;
        }
        return currentBytes;
    }

    public void shutdown() {
        isNotShutdown = false;
    }
}
