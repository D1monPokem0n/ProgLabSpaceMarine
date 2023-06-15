package ru.prog.itmo.connection;

import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Objects;

import static ru.prog.itmo.control.ClientListener.LOGGER;

public class ConnectionModule {
    private DatagramChannel datagramChannel;
    private final SocketAddress address;
    private final int port;
    private static final String PORT_ENV = "PORT";
    private static final int DEFAULT_PORT = 31725;

    public ConnectionModule() {
        try {
            var envPort = System.getenv(PORT_ENV);
            if (Objects.isNull(envPort)) {
                port = DEFAULT_PORT;
                LOGGER.log(Level.INFO, "Задан стандартный порт: " + DEFAULT_PORT);
            }
            else port = Integer.parseInt(envPort);
            address = new InetSocketAddress(port);
        } catch (NumberFormatException | NullPointerException e){
            throw new InvalidConnectionException("Некореектно задана переменная окружения PORT");
        }
    }

    public void connect() throws InvalidConnectionException {
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(address);
            LOGGER.log(Level.INFO, "Успешное соединение.");
        } catch (IOException e) {
            throw new InvalidConnectionException(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            datagramChannel.close();
        } catch (IOException e) {
            throw new InvalidConnectionException(e.getMessage());
        }
    }

    public DatagramChannel datagramChannel() {
        return datagramChannel;
    }
}
