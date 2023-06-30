package ru.prog.itmo.connection;

import org.apache.logging.log4j.Level;
import ru.prog.itmo.control.ClientCommandsMap;
import ru.prog.itmo.storage.Storage;

import java.net.SocketAddress;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import static ru.prog.itmo.control.ClientListener.LOGGER;

public class ClientRequestsHandler {
    private ClientCommandsMap commandMap;
    private final TokenValidator tokenValidator;
    private final Storage storage;
    private final ConcurrentLinkedQueue<SocketAddress> clients;
    private final ConcurrentHashMap<SocketAddress, Request<?>> requestMap;
    private final ConcurrentHashMap<SocketAddress, Response<?>> responseMap;
    private final ExecutorService handleExecutor;
    private final SendModule sendModule;
    private static final int HANDLE_THREADS_COUNT = 16;
    private static final String LOGIN_COMMANDS_PATTERN = ("(login|register|token_update)");

    public ClientRequestsHandler(ClientCommandsMap commandMap,
                                 Storage storage,
                                 ConcurrentHashMap<SocketAddress, Request<?>> requestMap,
                                 ConcurrentHashMap<SocketAddress, Response<?>> responseMap,
                                 SendModule sendModule,
                                 ConcurrentLinkedQueue<SocketAddress> clients) {
        this.commandMap = commandMap;
        this.storage = storage;
        this.requestMap = requestMap;
        this.responseMap = responseMap;
        this.sendModule = sendModule;
        this.clients = clients;
        handleExecutor = Executors.newFixedThreadPool(HANDLE_THREADS_COUNT);
        this.tokenValidator = new TokenValidator();
    }


    public void submitHandleTask(Future<SocketAddress> task) {
        try {
            SocketAddress address = task.get();
            var nextTask = handleExecutor.submit(handleTask(address));
            sendModule.submitSendTask(nextTask);
        } catch (InterruptedException e) {
            LOGGER.log(Level.INFO, "Обработка запроса прервана.");
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARN, e.getMessage());
        }
    }

    public Callable<SocketAddress> handleTask(SocketAddress address) {
        return () -> {
            Request<?> request = requestMap.get(address);
            String commandName = request.getCommandType();
            if (validateUser(request.getUser(), commandName)) {
                serveAuthorizedUser(request, commandName, address);
            } else {
                LOGGER.log(Level.INFO, "Пользователь " + request.getUser().getLogin() + " не прошёл валидацию.");
                kickNotAuthorizedUser(address);
            }
            return address;
        };
    }

    private void serveAuthorizedUser(Request<?> request, String commandName, SocketAddress address) {
        LOGGER.log(Level.INFO, "Пользователь " + request.getUser().getLogin() + " прошёл валидацию.");
//        if (commandName.equals("listen")) {
        if (!clients.contains(address)) {
            LOGGER.log(Level.INFO,"Новый пол трансегденред");
            clients.add(address);
        }
//        }
        commandMap.getCommand(commandName).execute(address);
    }

    private void kickNotAuthorizedUser(SocketAddress address) {
        Response<Object> response = new Response<>();
        response.setComment("Пользователь не авторизован.");
        response.setNotAuthorized(true);
        responseMap.put(address, response);
    }

    private boolean validateUser(User user, String commandName) {
        if (Pattern.matches(LOGIN_COMMANDS_PATTERN, commandName))
            return true;
        String token = user.getToken();
        String login = user.getLogin();
        return tokenValidator.validate(token, login);
    }

    public void setCommandMap(ClientCommandsMap commandMap) {
        this.commandMap = commandMap;
    }

    public void shutdown() {
        handleExecutor.shutdown();
    }
}
