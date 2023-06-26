package ru.prog.itmo.connection;

import ru.prog.itmo.control.Controller;
import ru.prog.itmo.speaker.Speaker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Objects;


public class SendModule {
    private DatagramSocket socket;
    private InetAddress host;
    private final Speaker speaker;
    private static int port;
    private static final int DEFAULT_PORT = 31725;
    private static final String ENV_PORT = "SERVER_PORT";

    public SendModule(DatagramSocket socket, InetAddress host, Speaker speaker) {
        this.socket = socket;
        this.host = host;
        this.speaker = speaker;
        setPort();
    }

    private void setPort() {
        try {
            var envPort = System.getenv(ENV_PORT);
            if (Objects.isNull(envPort)) {
                speaker.speak("Задан стандартный порт серва: " + DEFAULT_PORT);
                port = DEFAULT_PORT;
            } else port = Integer.parseInt(envPort);
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidConnectionException("Не задана переменная окружения SERVER_PORT." +
                                                 "Задайте порт сервера.");
        }
    }



    public synchronized void submitSending(Request<?> request) {
        request.setUser(Controller.getUser());
        ByteBuffer toServer = serializeRequest(request);
        sendRequest(toServer);
    }

    private ByteBuffer serializeRequest(Request<?> request) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            return ByteBuffer.wrap(baos.toByteArray());
        } catch (IOException e) {
            throw new InvalidConnectionException(e.getMessage());
        }
    }


    private void sendRequest(ByteBuffer request) {
        try {
            byte[] buffer = request.array();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, host, port);
            socket.send(packet);
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось отправить запрос на сервер");
        }
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }
}
