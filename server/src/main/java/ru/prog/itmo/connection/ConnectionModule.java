package ru.prog.itmo.connection;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectionModule {
    DatagramChannel datagramChannel;
    int port;
    SocketAddress address;
    Request<?> request;

    public ConnectionModule() {
        port = 31725;
        address = new InetSocketAddress(port);

    }

    public void connect() {
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(address);
        } catch (IOException e) {
            throw new InvalidConnectionException("Проблемы с соединением...");
        }
    }

    public void sendResponse(Response<?> response) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            ByteBuffer fromClient = ByteBuffer.wrap(baos.toByteArray());
            datagramChannel.send(fromClient, address);
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось отправить ответ клинету.");
        }
    }

    public void receiveRequest() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            address = datagramChannel.receive(buffer);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            request = (Request<?>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidConnectionException("Не удалось получить запрос.");
        }
    }

    public Request<?> getRequest() {
        return request;
    }
}
