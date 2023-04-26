package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.server.ConnectionModule;
import ru.prog.itmo.server.InvalidConnectionException;
import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.speaker.Speaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class UpdateCommand extends ServerIOCommand implements UserAsking {
    private final ConsoleArgument argument;
    private final HashMap<String, SpaceMarineUpdatable> updatingFields;

    public UpdateCommand(ConnectionModule connectionModule, ConsoleArgument argument, Speaker speaker, Reader reader) {
        super("update", connectionModule, speaker, reader);
        this.argument = argument;
        updatingFields = new HashMap<>();
        updatingFields.put("all", this::updateAll);
        updatingFields.put("id", this::updateId);
        updatingFields.put("name", this::updateName);
        updatingFields.put("coordinate_x", this::updateCoordinateX);
        updatingFields.put("coordinate_y", this::updateCoordinateY);
        updatingFields.put("creation_date", this::updateCreationDate);
        updatingFields.put("health", this::updateHealth);
        updatingFields.put("heart_count", this::updateHeartCount);
        updatingFields.put("category", this::updateCategory);
        updatingFields.put("melee_weapon", this::updateMeleeWeapon);
        updatingFields.put("chapter", this::updateChapter);
        updatingFields.put("chapter_name", this::updateChapterName);
        updatingFields.put("chapter_parent_legion", this::updateChapterParentLegion);
        updatingFields.put("chapter_marines_count", this::updateChapterMarinesCount);
        updatingFields.put("chapter_world", this::updateChapterWorld);
    }

    @Override
    public void execute() {
        try {
            long id = Long.parseLong(argument.getValue());
            Request<Long> request1 = new Request<>(COMMAND_TYPE, id);
            ByteBuffer toServer1 = serializeRequest(request1);
            connectionModule().sendRequest(toServer1);
            ByteBuffer fromServer1 = connectionModule().receiveResponse();
            ObjectInputStream inputStream1 = getDeserializedInputStream(fromServer1);
            @SuppressWarnings("unchecked")
            Response<SpaceMarine> response1 = (Response<SpaceMarine>) inputStream1.readObject();
            SpaceMarine searchableMarine = response1.getData();
            if (searchableMarine == null)
                throw new InvalidSpaceMarineValueException("Морпеха с данным id не существует.");
            SpaceMarine TMPMarine = SpaceMarine.getTMPSpaceMarine();
            TMPMarine.setAllFields(searchableMarine);
            speaker().speak("Вы начали обновлять значения полей у данного морпеха:\n\n" +
                    searchableMarine +
                    "\n\nДля отмены введите \"cancel\"\n" +
                    "\nВыберите, какое поле вы хотите обновить:\n" + updatingFields.keySet());
            boolean isUpdatingDone = false;
            while (!isUpdatingDone) {
                try {
                    String field = reader().read();
                    if (field.equals("cancel")) throw new CreateCancelledException("Обновление отменено.");
                    if (!updatingFields.containsKey(field))
                        throw new InvalidSpaceMarineValueException("Такого поля не существует.\nПовторите ввод:");
                    while (!isUpdatingDone) {
                        try {
                            isUpdatingDone = updatingFields.get(field).update(TMPMarine);
                        } catch (InvalidSpaceMarineValueException e) {
                            speaker().speak(e.getMessage());
                        }
                    }
                } catch (InvalidSpaceMarineValueException e) {
                    speaker().speak(e.getMessage());
                }
            }
            searchableMarine.setAllFields(TMPMarine);
            Request<SpaceMarine> request2 = new Request<>(COMMAND_TYPE, searchableMarine);
            ByteBuffer toServer2 = serializeRequest(request2);
            connectionModule().sendRequest(toServer2);
            ByteBuffer fromServer2 = connectionModule().receiveResponse();
            ObjectInputStream inputStream2 = getDeserializedInputStream(fromServer2);
            @SuppressWarnings("unchecked")
            Response<String> response2 = (Response<String>) inputStream2.readObject();
            speaker().speak(response2.getData());
        } catch (NumberFormatException e) {
            speaker().speak("Вам следует вводить число.");
        } catch (InvalidSpaceMarineValueException | UpdatingCancelledException | CreateCancelledException e) {
            speaker().speak(e.getMessage());
        } catch (IOException | ClassNotFoundException | InvalidConnectionException e) {
            speaker().speak("Проблемы с соединением");
        }
    }

    private boolean updateAll(SpaceMarine marine) throws UpdatingCancelledException {
        String[] fields = {"name",
                "coordinate_x",
                "coordinate_y",
                "health",
                "heart_count",
                "category",
                "melee_weapon",
                "chapter"
        };
        return updateFields(marine, fields);
    }

    private boolean updateChapter(SpaceMarine marine) throws UpdatingCancelledException {
        if (marine.getChapter() == null) {
            speaker().speak("До этого часть была не назначена. " +
                    "\nДля обновления будет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        speaker().speak("Вы хотите обнулить нынешнюю часть?(Y/N)");
        String answer = reader().read();
        switch (answer) {
            case "Y", "y" -> {
                marine.setChapter(null);
                return true;
            }
            case "N", "n" -> {
                speaker().speak("Значит обновим все поля части)");
                String[] fields = {"chapter_name",
                        "chapter_parent_legion",
                        "chapter_marines_count",
                        "chapter_world"
                };
                return updateFields(marine, fields);
            }
            default -> {
                speaker().speak("Вы ввели какую-то фигню. Обмновление прекращено");
                return true;
            }
        }
    }

    private boolean updateFields(SpaceMarine marine, String[] fields) throws UpdatingCancelledException {
        for (String field : fields) {
            boolean isUpdatingDone = false;
            while (!isUpdatingDone) {
                try {
                    isUpdatingDone = updatingFields.get(field).update(marine);
                } catch (InvalidSpaceMarineValueException e) {
                    speaker().speak(e.getMessage());
                }
            }
        }
        speaker().speak("Данные успешно обновлены" + "\n" + marine);
        return true;
    }

    private boolean updateId(SpaceMarine marine) {
        speaker().speak("Знаете, мы считаем, что обновлять id космодесантников не лучшая идея)");
        return true;
    }

    private boolean updateName(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешнее имя: " + marine.getName() +
                "\nВведите новое имя: ");
        String newName = reader().read();
        if (newName == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newName.equals("cancel")) throw new UpdatingCancelledException();
        marine.setName(newName);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateX(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешнее местоположение по оси OX: " + marine.getCoordinates().getX() +
                "\nВведите новое местоположение: ");
        String newXValue = reader().read();
        if (newXValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newXValue.equals("cancel")) throw new UpdatingCancelledException();
        float newX;
        try {
            newX = Float.parseFloat(newXValue);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Вы должны вводить число");
        }
        if (newX == Float.POSITIVE_INFINITY | newX == Float.NEGATIVE_INFINITY)
            throw new InvalidSpaceMarineValueException("Вы вышли за пределы Java.");
        marine.getCoordinates().setX(newX);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateY(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешнее местоположение по оси OY: " + marine.getCoordinates().getY() +
                "\nВведите новое местоположение: ");
        String newYValue = reader().read();
        if (newYValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newYValue.equals("cancel")) throw new UpdatingCancelledException();
        double newY;
        try {
            newY = Float.parseFloat(newYValue);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Вы должны вводить число");
        }
        if (newY > 431)
            throw new InvalidSpaceMarineValueException("Значение местоположения по OY должно быть меньше 431");
        if (newY == Double.POSITIVE_INFINITY) throw new InvalidSpaceMarineValueException("Вы вышли за пределы Java.");
        marine.getCoordinates().setY(newY);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCreationDate(SpaceMarine marine) {
        speaker().speak("А как... заново родить его?...");
        return true;
    }

    private boolean updateHealth(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешнее количество здоровья: " + marine.getHealth() +
                "\nВведите новое здоровье: ");
        String newHealthValue = reader().read();
        if (newHealthValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newHealthValue.equals("cancel")) throw new UpdatingCancelledException();
        int newHealth;
        try {
            newHealth = Integer.parseInt(newHealthValue);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Вы должны вводить число");
        }
        if (newHealth > Integer.MAX_VALUE) throw new InvalidSpaceMarineValueException("Вы вышли за пределы Java.");
        if (newHealth <= 0)
            throw new InvalidSpaceMarineValueException("Значение здоровья должно быть больше 0, иначе десантник, ха-ха, МЁРТВ! Ха-Ха-Ха.");
        marine.setHealth(newHealth);
        return true;
    }

    private boolean updateHeartCount(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешнее число сердец: " + marine.getHeartCount() +
                "\nВведите новое число сердец: ");
        String newHeartCountValue = reader().read();
        if (newHeartCountValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newHeartCountValue.equals("cancel")) throw new UpdatingCancelledException();
        long newHeartCount;
        try {
            newHeartCount = Long.parseLong(newHeartCountValue);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Вы должны вводить число");
        }
        if (newHeartCount > 3 | newHeartCount < 1)
            throw new InvalidSpaceMarineValueException("Число сердец у десантника - от 1 до 3.");
        marine.setHeartCount(newHeartCount);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCategory(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешняя категория десантника: " + marine.getCategory().getName());
        speaker().speak("""
                Существует 4 категории космодесантников:
                Chaplain, Librarian, Apothecary, Scout
                Введите новую категорию вашего солдата:
                """);
        String newCategoryValue = reader().read();
        if (newCategoryValue == null) {
            marine.setCategory(null);
            return true;
        }
        if (newCategoryValue.equals("cancel")) throw new UpdatingCancelledException();
        AstartesCategory newCategory;
        try {
            newCategory = AstartesCategory.getCategory(newCategoryValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("Это не является категорией космодесантника.");
        }
        marine.setCategory(newCategory);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateMeleeWeapon(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        speaker().speak("Нынешнее оружие десантника: " + marine.getMeleeWeapon().getName());
        speaker().speak("""
                Существует 3 вида оружия космодесантников:
                Manreaper, Lighting claw, Power fist
                Введите новое оружие вашего солдата:
                """);
        String newMeleeWeaponValue = reader().read();
        if (newMeleeWeaponValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newMeleeWeaponValue.equals("cancel")) throw new UpdatingCancelledException();
        MeleeWeapon newMeleeWeapon;
        try {
            newMeleeWeapon = MeleeWeapon.getMeleeWeapon(newMeleeWeaponValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("Это не является оружием космодесантника.");
        }
        marine.setMeleeWeapon(newMeleeWeapon);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterName(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        if (marine.getChapter() == null) {
            speaker().speak("До этого часть была не назначена. " +
                    "\nДля обновления будет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        speaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новое название части: ");
        String newChapterName = reader().read();
        if (newChapterName == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newChapterName.equals("cancel")) throw new UpdatingCancelledException();
        marine.getChapter().setName(newChapterName);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterParentLegion(SpaceMarine marine) throws UpdatingCancelledException {
        if (marine.getChapter() == null) {
            speaker().speak("До этого часть была не назначена. " +
                    "\nБудет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        speaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новоый легион: ");
        String newParentLegion = reader().read();
        if (newParentLegion == null) {
            marine.getChapter().setParentLegion(null);
            return true;
        }
        if (newParentLegion.equals("cancel")) throw new UpdatingCancelledException();
        marine.getChapter().setParentLegion(newParentLegion);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterMarinesCount(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        if (marine.getChapter() == null) {
            speaker().speak("До этого часть была не назначена. " +
                    "\nБудет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        speaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новое число солдат в части: ");
        String newMarinesCountValue = reader().read();
        if (newMarinesCountValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newMarinesCountValue.equals("cancel")) throw new UpdatingCancelledException();
        long newMarinesCount;
        try {
            newMarinesCount = Long.parseLong(newMarinesCountValue);
        } catch (NumberFormatException e) {
            throw new InvalidSpaceMarineValueException("Вы должны вводить число");
        }
        if (newMarinesCount > 1000 | newMarinesCount < 1)
            throw new InvalidSpaceMarineValueException("Число солдат в части - от 1 до 1000.");
        marine.getChapter().setMarinesCount(newMarinesCount);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterWorld(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        if (marine.getChapter() == null) {
            speaker().speak("До этого часть была не назначена. " +
                    "\nБудет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        speaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новое название мира, в котороом расположена часть: ");
        String newChapterWorld = reader().read();
        if (newChapterWorld == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newChapterWorld.equals("cancel")) throw new UpdatingCancelledException();
        marine.getChapter().setWorld(newChapterWorld);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}
