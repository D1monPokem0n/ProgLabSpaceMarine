package ru.prog.itmo.connection;

import org.apache.logging.log4j.Level;
import ru.prog.itmo.control.ServerState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.*;

import static ru.prog.itmo.control.ClientListener.LOGGER;
import static ru.prog.itmo.connection.ConnectionManager.MAX_REQUESTS_COUNT;


public class ReceiveModule {
    private final DatagramChannel datagramChannel;
    private final ServerState serverState;
    private final ClientRequestsHandler requestsHandler;
    private final ConcurrentHashMap<SocketAddress, Request<?>> requestMap;
    private final ExecutorService receiveExecutor;
    public static final int PACKET_SIZE = 4096;
    public static final int RECEIVE_THREADS_COUNT = 16;

    public ReceiveModule(DatagramChannel datagramChannel,
                         ConcurrentHashMap<SocketAddress, Request<?>> requestMap,
                         ServerState serverState,
                         ClientRequestsHandler requestHandler) {
        this.datagramChannel = datagramChannel;
        this.requestMap = requestMap;
        this.serverState = serverState;
        this.requestsHandler = requestHandler;
        receiveExecutor = Executors.newFixedThreadPool(RECEIVE_THREADS_COUNT);
    }

    public void submitReceiveTask() {
        while (serverState.isWorkStatus()) {
            if (requestMap.size() <= MAX_REQUESTS_COUNT) {
                Future<SocketAddress> nextTask = receiveExecutor.submit(receiveTask());
                requestsHandler.submitHandleTask(nextTask);
            }
        }
    }

    private Request<?> deserializeRequest(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Request<?>) ois.readObject();
    }

    private Callable<SocketAddress> receiveTask() {
        return () -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
                SocketAddress address = receiveRequest(buffer);
                putRequest(address, buffer);
                return address;
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.WARN, "Не удалось получить запрос: " + e.getMessage());
                throw new InvalidConnectionException("Не удалось получить запрос");
            }
        };
    }

    private void putRequest(SocketAddress address, ByteBuffer buffer) throws IOException, ClassNotFoundException {
        Request<?> request = deserializeRequest(buffer);
        LOGGER.log(Level.INFO, "Получен запрос от пользователя "
                               + request.getUser().getLogin()
                               + " на исполнение команды "
                               + request.getCommandType()
        );
        requestMap.put(address, request);
    }

    private synchronized SocketAddress receiveRequest(ByteBuffer buffer) throws IOException {
        return datagramChannel.receive(buffer);
    }

    public void shutdown() {
        receiveExecutor.shutdown();
    }
}
