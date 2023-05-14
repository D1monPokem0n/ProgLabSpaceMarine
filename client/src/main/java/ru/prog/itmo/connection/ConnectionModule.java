package ru.prog.itmo.connection;

import ru.prog.itmo.speaker.Speaker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ConnectionModule {
    private DatagramSocket socket;
    private InetAddress host;
    private static final int PORT = 31725;
    private static final int PACKET_SIZE = 4096;
    private static final byte STOP_BYTE = 0x60;
    private final Speaker speaker;

    public ConnectionModule(Speaker speaker) {
        this.speaker = speaker;
    }

    public void connect() {
        try {
            speaker.speak("Идёт подключение к сети");
            host = InetAddress.getLocalHost();
            socket = new DatagramSocket();
            socket.setSoTimeout(10000);
            speaker.speak("Соединение прошло успешно");
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось подключиться к сети. Попробуйте снова");
        }
    }

    public void sendRequest(ByteBuffer request) {
        try {
            byte[] buffer = request.array();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, host, PORT);
            socket.send(packet);
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось отправить запрос на сервер");
        }
    }

    public ByteBuffer receiveResponse() {
        try {
            byte[] fromServer = new byte[PACKET_SIZE];
            ArrayList<ByteBuffer> packets = new ArrayList<>();
            boolean isReceivingDone = false;
            while (!isReceivingDone) {
                DatagramPacket packet = new DatagramPacket(fromServer, fromServer.length);
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(fromServer);
                ByteBuffer data = ByteBuffer.wrap(bais.readNBytes(PACKET_SIZE - 1));
                packets.add(data);
                if (fromServer[PACKET_SIZE - 1] == STOP_BYTE) {
                    isReceivingDone = true;
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (ByteBuffer currentBuffer : packets) {
                byte[] currentBytes = currentBuffer.array();
                baos.write(currentBytes);
            }
            return ByteBuffer.wrap(baos.toByteArray());
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось получить ответ от сервера" + e.getMessage());
        }
    }
}
