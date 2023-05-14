package ru.prog.itmo.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConnectionModule {
    private DatagramChannel datagramChannel;
    private static final int PORT = 31725;
    private SocketAddress address;
    private Request<?> request;
    private static final Logger LOGGER = LogManager.getLogger(ConnectionModule.class);
    private static final int PACKET_SIZE = 4096;
    private static final byte STOP_BYTE = 0x60;
    private static final byte CONTINUE_BYTE = 0x4E;

    public ConnectionModule() {
        address = new InetSocketAddress(PORT);
    }

    public void connect() throws InvalidConnectionException {
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(address);
            LOGGER.log(Level.INFO, "Успешное соединение.");
        } catch (IOException e) {
            LOGGER.log(Level.WARN, "Проблемы с соединением: " + e.getMessage() );
            throw new InvalidConnectionException("Проблемы с соединением...");
        }
    }

    public void sendResponse(Response<?> response) {
        try {
            ArrayList<ByteBuffer> byteBuffers = getPackets(response);
            for (ByteBuffer toClient : byteBuffers) {
                datagramChannel.send(toClient, address);
                LOGGER.log(Level.INFO, "Отправлен пакет клиенту.");
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new InvalidConnectionException(e.getMessage());
                }
            }
        } catch (IOException e) {
            String message = "Не удалось отправить ответ клиенту." + e.getMessage();
            LOGGER.log(Level.WARN, message);
            throw new InvalidConnectionException(message);
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

    private ArrayList<ByteBuffer> getChunks(ByteArrayInputStream bais, int packetsCount) throws IOException {
        ArrayList<ByteBuffer> byteBuffers = new ArrayList<>();
        for (int i = 1; i <= packetsCount; i++) {
            byte[] currentBytes = new byte[PACKET_SIZE];
            for (int j = 0; j < PACKET_SIZE; j++) {
                if (bais.available() > 0)
                    currentBytes[j] = bais.readNBytes(1)[0];
            }
            if (bais.available() > 0) {
                currentBytes[PACKET_SIZE - 1] = CONTINUE_BYTE;
            } else {
                currentBytes[PACKET_SIZE - 1] = STOP_BYTE;
            }
            ByteBuffer buffer = ByteBuffer.wrap(currentBytes);
            byteBuffers.add(buffer);
        }
        return byteBuffers;
    }

    public void receiveRequest() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
            address = datagramChannel.receive(buffer);
            LOGGER.log(Level.INFO, "Получени запрос от клиента.");
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            request = (Request<?>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARN, "Не удалось получить запрос: " + e.getMessage());
            throw new InvalidConnectionException("Не удалось получить запрос.");
        }
    }

    public void disconnect() {
        try {
            datagramChannel.close();
        } catch (IOException e) {
            throw new InvalidConnectionException(e.getMessage());
        }
    }

    public Request<?> getRequest() {
        return request;
    }

}
