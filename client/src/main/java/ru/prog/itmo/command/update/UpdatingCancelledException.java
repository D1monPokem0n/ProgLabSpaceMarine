package ru.prog.itmo.command.update;

public class UpdatingCancelledException extends RuntimeException {
    public UpdatingCancelledException(String message) {
        super(message);
    }

    public UpdatingCancelledException() {
        super("Обновления данных об космодесантнике прервано.");
    }
}
