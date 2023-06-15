package ru.prog.itmo;

import ru.prog.itmo.control.ClientListener;
import ru.prog.itmo.control.ServerController;

public class Main {
    public static void main(String[] args) {
        ClientListener clientListener = new ClientListener();
        ServerController controller = new ServerController(
                clientListener.getConnectionModule(),
                clientListener.getCommandsMap(),
                clientListener.getServerState(),
                clientListener.getStorage()
        );
        Thread controlThread = new Thread(controller);
        controlThread.start();
        clientListener.run();
    }
}