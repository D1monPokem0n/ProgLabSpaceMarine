package ru.prog.itmo.connection;

import org.apache.logging.log4j.Level;
import ru.prog.itmo.control.ClientCommandsMap;
import ru.prog.itmo.control.ServerState;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static ru.prog.itmo.control.ClientListener.LOGGER;

public class ConnectionManager {
    private ConcurrentHashMap<SocketAddress, Request<?>> requestMap;
    private ConcurrentHashMap<SocketAddress, Response<?>> responseMap;
    private ConcurrentLinkedQueue<SocketAddress> clients;
    private boolean doesHaveChanges;
    private Thread updatingThread;
    private final ServerState serverState;
    private final ConnectionModule connectionModule;
    private final ReceiveModule receiveModule;
    private final SendModule sendModule;
    private final ClientRequestsHandler requestsHandler;
    public static final int MAX_REQUESTS_COUNT = 32;

    public ConnectionManager(ClientCommandsMap commandMap, ServerState serverState, Storage storage) {
        initializeCollections();
        this.serverState = serverState;
        connectionModule = new ConnectionModule();
        connectionModule.connect();
        sendModule = new SendModule(responseMap);
        requestsHandler = new ClientRequestsHandler(commandMap, storage, requestMap, responseMap, sendModule, clients);
        receiveModule = new ReceiveModule(connectionModule.datagramChannel(),
                requestMap,
                serverState,
                requestsHandler);
    }

    public void setCommandMap(ClientCommandsMap commandMap) {
        requestsHandler.setCommandMap(commandMap);
    }

    private void initializeCollections() {
        requestMap = new ConcurrentHashMap<>();
        responseMap = new ConcurrentHashMap<>();
        clients = new ConcurrentLinkedQueue<>();
    }

    public void startModules() {
        startUpdating();
        receiveModule.submitReceiveTask();
    }

    public Request<?> getRequestByAddress(SocketAddress address) {
        return requestMap.get(address);
    }

    public void putResponse(SocketAddress address, Response<?> response) {
        LOGGER.log(Level.INFO, "Начата отправка пользователю "
                               + getUserNameByAddress(address)
                               + " по адресу: " + address);
        requestMap.remove(address);
        responseMap.put(address, response);
    }

    public void disconnect() {
        receiveModule.shutdown();
        requestsHandler.shutdown();
        sendModule.shutdown();
        connectionModule.disconnect();
        updatingThread.interrupt();
    }

    public String getUserNameByAddress(SocketAddress address) {
        return requestMap.get(address)
                .getUser()
                .getLogin();
    }

    private void startUpdating() {
        doesHaveChanges = false;
        updatingThread = new Thread(updateTask());
        updatingThread.start();
    }

    private Runnable updateTask() {
        return () -> {
//            LOGGER.log(Level.INFO, "Update 1 notification ");
//            while (serverState.isWorkStatus()) {
////                LOGGER.log(Level.INFO, "Update 2 notification to client: ");
//                if (doesHaveChanges) {
//                    LOGGER.log(Level.INFO, "Update is 4 notification to client:");
//                    for (var address : clients) {
//                        LOGGER.log(Level.INFO, "Update 5 notification to client: " + address);
//                        var response = new Response<>(null, true);
//                        responseMap.put(address, response);
//                        sendModule.submitSendTask(address);
//                    }
//                    doesHaveChanges = false;
//                }
//            }
        };
    }

    public void setHaveUpdates() {
        // doesHaveChanges = true;

        LOGGER.log(Level.INFO, "Update is 4 notification to client:");
        for (var address : clients) {
            LOGGER.log(Level.INFO, "Update 5 notification to client: " + address);
            var response = new Response<>(null, true);
            responseMap.put(address, response);
            sendModule.submitSendTask(address);
        }
        // doesHaveChanges = false;
    }


}
