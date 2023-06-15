package ru.prog.itmo.connection;

import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectionModule {
    private DatagramSocket socket;
    private InetAddress host;
    private final Speaker speaker;
    private static final int WAITING_TIME = 10000;

    public ConnectionModule(Speaker speaker) {
        this.speaker = speaker;
    }

    public void connect() {
        try {
            speaker.speak("Идёт подключение к сети");
            host = InetAddress.getLocalHost();
            socket = new DatagramSocket();
            socket.setSoTimeout(WAITING_TIME);
            speaker.speak("Соединение прошло успешно");
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось подключиться к сети. Попробуйте снова");
        }
    }

    public InetAddress getHost() {
        return host;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
