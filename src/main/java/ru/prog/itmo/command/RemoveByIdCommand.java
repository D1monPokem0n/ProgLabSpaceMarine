package ru.prog.itmo.command;

import ru.prog.itmo.ConsoleArgument;
import ru.prog.itmo.Storage;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class RemoveByIdCommand extends StorageCommand {
    private final ConsoleArgument argument;

    public RemoveByIdCommand(Storage storage, ConsoleArgument argument) {
        super(storage);
        this.argument = argument;
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute() {
        super.execute();
        Speaker speaker = new ConsoleSpeaker();
        try {
            if (getStorage().getHashSet().isEmpty()) throw new EmptyStorageException();
            int id = Integer.parseInt(argument.getValue());
            SpaceMarine searchableMarine = null;
            for (SpaceMarine marine : getStorage().getHashSet()) {
                if (marine.getId() == id) {
                    searchableMarine = marine;
                    break;
                }
            }
            if (searchableMarine == null) {
                throw new InvalidSpaceMarineValueException("Не сущесвует десантника с таким id");
            } else {
                getStorage().getHashSet().remove(searchableMarine);
            }
        } catch (NumberFormatException e) {
            speaker.speak("Вы должны вводить целое число...");
        } catch (EmptyStorageException e) {
            speaker.speak(e.getMessage());
        } catch (InvalidSpaceMarineValueException e){
            speaker.speak(e.getMessage());
        }
    }
}
