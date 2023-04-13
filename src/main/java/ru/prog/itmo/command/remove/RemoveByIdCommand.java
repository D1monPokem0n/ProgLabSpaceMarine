package ru.prog.itmo.command.remove;

import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.storage.EmptyStorageException;
import ru.prog.itmo.command.StorageOCommand;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.speaker.Speaker;

public class RemoveByIdCommand extends StorageOCommand {
    private final ConsoleArgument argument;

    public RemoveByIdCommand(Storage storage, Speaker speaker, ConsoleArgument argument) {
        super(storage, speaker);
        this.argument = argument;
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute() {
        super.execute();
        try {
            if (getStorage().isEmpty()) throw new EmptyStorageException();
            int id = Integer.parseInt(argument.getValue());
            SpaceMarine searchableMarine = null;
            for (SpaceMarine marine : getStorage().getHashSet()) {
                if (marine.getId() == id) {
                    searchableMarine = marine;
                    break;
                }
            }
            if (!getStorage().remove(searchableMarine)) {
                throw new InvalidSpaceMarineValueException("Не сущесвует десантника с таким id");
            } else {
                getSpeaker().speak("Из коллекции удалён морпех: " + searchableMarine);
            }
        } catch (NumberFormatException e) {
            getSpeaker().speak("Вы должны вводить целое число...");
        } catch (EmptyStorageException | InvalidSpaceMarineValueException e) {
            getSpeaker().speak(e.getMessage());
        }
    }
}
