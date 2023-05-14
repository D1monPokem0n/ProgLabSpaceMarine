package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.ServerIOCommand;
import ru.prog.itmo.connection.ConnectionModule;
import ru.prog.itmo.connection.InvalidConnectionException;
import ru.prog.itmo.connection.Request;
import ru.prog.itmo.connection.Response;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.reader.Reader;
import ru.prog.itmo.spacemarine.*;
import ru.prog.itmo.spacemarine.builder.script.InvalidScriptException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.speaker.Speaker;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class UpdateScriptCommand extends ServerIOCommand implements ScriptExecutable {
    private final ConsoleArgument argument;
    private final HashMap<String, SpaceMarineUpdatable> updatingFields;

    public UpdateScriptCommand(ConnectionModule connectionModule, ConsoleArgument argument, Speaker speaker, Reader reader) {
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
            Request<Long> request1 = new Request<>(COMMAND_TYPE, id, true);
            ByteBuffer toServer1 = serializeRequest(request1);
            connectionModule().sendRequest(toServer1);
            ByteBuffer fromServer1 = connectionModule().receiveResponse();
            Response<?> response1 = getDeserializedResponse(fromServer1);
            SpaceMarine searchableMarine = (SpaceMarine) response1.getData();
            if (searchableMarine == null)
                throw new InvalidScriptException("Введено несуществующее id");
            SpaceMarine TMPMarine = SpaceMarine.getTMPSpaceMarine();
            TMPMarine.setAllFields(searchableMarine);
            String field = reader().read();
            if (!updatingFields.containsKey(field))
                throw new InvalidScriptException("Такого поля не существует.");
            updatingFields.get(field).update(TMPMarine);
            searchableMarine.setAllFields(TMPMarine);
            Request<SpaceMarine> request2 = new Request<>(COMMAND_TYPE, searchableMarine, true);
            ByteBuffer toServer2 = serializeRequest(request2);
            connectionModule().sendRequest(toServer2);
            ByteBuffer fromServer2 = connectionModule().receiveResponse();
            Response<?> response2 = getDeserializedResponse(fromServer2);
            speaker().speak((String) response2.getData());
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Некорректный ввод");
        } catch (InvalidSpaceMarineValueException | CreateCancelledException e) {
            throw new InvalidScriptException(e.getMessage());
        } catch (InvalidConnectionException e){
            throw new InvalidScriptException("Проблемы с соединением...");
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
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
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
        }
        throw new InvalidScriptException("Вы ввели неверный текст для подтверждения обновления части.");
    }

    private boolean updateFields(SpaceMarine marine, String[] fields) {
        for (String field : fields) {
            updatingFields.get(field).update(marine);
        }
        speaker().speak("Данные успешно обновлены" + "\n" + marine);
        return true;
    }

    private boolean updateId(SpaceMarine marine) {
        speaker().speak("Знаете, мы считаем, что обновлять id космодесантников не лучшая идея)");
        return true;
    }

    private boolean updateName(SpaceMarine marine) {
        String newName = reader().read();
        if (newName == null) throw new InvalidScriptException("Не вводите пустую строку");
        marine.setName(newName);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateX(SpaceMarine marine) {
        String newXValue = reader().read();
        if (newXValue == null) throw new InvalidScriptException("Введена пустая строка");
        float newX;
        try {
            newX = Float.parseFloat(newXValue);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Некорректный ввод");
        }
        if (newX == Float.POSITIVE_INFINITY | newX == Float.NEGATIVE_INFINITY)
            throw new InvalidScriptException("Вы вышли за пределы Java.");
        marine.getCoordinates().setX(newX);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateY(SpaceMarine marine) {
        String newYValue = reader().read();
        if (newYValue == null) throw new InvalidScriptException("Некорректный ввод");
        double newY;
        try {
            newY = Float.parseFloat(newYValue);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Введено не число");
        }
        if (newY > 431)
            throw new InvalidScriptException("Значение местоположения по OY должно быть меньше 431");
        if (newY == Double.POSITIVE_INFINITY) throw new InvalidScriptException("Вы вышли за пределы Java.");
        marine.getCoordinates().setY(newY);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCreationDate(SpaceMarine marine) {
        speaker().speak("А как... заново родить его?...");
        return true;
    }

    private boolean updateHealth(SpaceMarine marine) {
        String newHealthValue = reader().read();
        if (newHealthValue == null) throw new InvalidScriptException("Не вводите пустую строку");
        int newHealth;
        try {
            newHealth = Integer.parseInt(newHealthValue);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Вы должны вводить число");
        }
        if (newHealth > Integer.MAX_VALUE) throw new InvalidScriptException("Вы вышли за пределы Java.");
        if (newHealth <= 0)
            throw new InvalidScriptException("Значение здоровья должно быть больше 0, иначе десантник, ха-ха, МЁРТВ! Ха-Ха-Ха.");
        marine.setHealth(newHealth);
        return true;
    }

    private boolean updateHeartCount(SpaceMarine marine) {
        String newHeartCountValue = reader().read();
        if (newHeartCountValue == null) throw new InvalidScriptException("Не вводите пустую строку");
        long newHeartCount;
        try {
            newHeartCount = Long.parseLong(newHeartCountValue);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Вы должны вводить число");
        }
        if (newHeartCount > 3 | newHeartCount < 1)
            throw new InvalidScriptException("Число сердец у десантника - от 1 до 3.");
        marine.setHeartCount(newHeartCount);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCategory(SpaceMarine marine) {
        String newCategoryValue = reader().read();
        if (newCategoryValue == null) {
            marine.setCategory(null);
            return true;
        }
        AstartesCategory newCategory;
        try {
            newCategory = AstartesCategory.getCategory(newCategoryValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidScriptException("Это не является категорией космодесантника.");
        }
        marine.setCategory(newCategory);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateMeleeWeapon(SpaceMarine marine) {
        String newMeleeWeaponValue = reader().read();
        if (newMeleeWeaponValue == null) throw new InvalidScriptException("Не вводите пустую строку");
        MeleeWeapon newMeleeWeapon;
        try {
            newMeleeWeapon = MeleeWeapon.getMeleeWeapon(newMeleeWeaponValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidScriptException("Это не является оружием космодесантника.");
        }
        marine.setMeleeWeapon(newMeleeWeapon);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterName(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newChapterName = reader().read();
        if (newChapterName == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        marine.getChapter().setName(newChapterName);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterParentLegion(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newParentLegion = reader().read();
        marine.getChapter().setParentLegion(newParentLegion);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterMarinesCount(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newMarinesCountValue = reader().read();
        if (newMarinesCountValue == null) throw new InvalidScriptException("Не вводите пустую строку");
        long newMarinesCount;
        try {
            newMarinesCount = Long.parseLong(newMarinesCountValue);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Вы должны вводить число");
        }
        if (newMarinesCount > 1000 | newMarinesCount < 1)
            throw new InvalidScriptException("Число солдат в части - от 1 до 1000.");
        marine.getChapter().setMarinesCount(newMarinesCount);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterWorld(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newChapterWorld = reader().read();
        if (newChapterWorld == null) throw new InvalidScriptException("Не вводите пустую строку");
        marine.getChapter().setWorld(newChapterWorld);
        speaker().speak("Значение поля обновлено успешно");
        return true;
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}
