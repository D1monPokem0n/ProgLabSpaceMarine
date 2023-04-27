package ru.prog.itmo.connection;

import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ConnectionModule {
    Socket socket;
    OutputStream os;
    InputStream is;
    InetAddress host;
    int port;

    public ConnectionModule(Speaker speaker) {
        try {
            speaker.speak("Идёт подключение к серверу");
            port = 31725;
            host = InetAddress.getLocalHost();
            socket = new Socket(host, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            speaker.speak("Соединение прошло успешно");
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось подключиться к серверу. Попробуйте снова");
        }
    }



    public void sendRequest(ByteBuffer request) {
        try {
            os.write(request.array());
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось отправить запрос на сервер");
        }
    }

    public ByteBuffer receiveResponse() {
        try {
            ByteBuffer fromServer = ByteBuffer.allocate(1024);
            fromServer.put(is.readAllBytes());
            return fromServer;
        } catch (IOException e){
            throw new InvalidConnectionException("Не удалось получить ответ от сервера");
        }
    }
}
