package ru.prog.itmo.command.update;

import ru.prog.itmo.command.ScriptExecutable;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.command.script.InvalidScriptException;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.speaker.Speaker;
import ru.prog.itmo.storage.Storage;

import java.util.HashMap;

public class UpdateScriptCommand extends StorageIOCommand implements ScriptExecutable {
    private final ConsoleArgument argument;
    private final HashMap<String, SpaceMarineUpdatable> updatingFields;

    public UpdateScriptCommand(Storage storage, ConsoleArgument argument, Speaker speaker, SpaceMarineReader reader) {
        super(storage, speaker, reader);
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
            SpaceMarine searchableMarine = getStorage().getById(id);
            if (searchableMarine == null)
                throw new InvalidScriptException("Введено несуществующее id");
            SpaceMarine TMPMarine = SpaceMarine.getTMPSpaceMarine();
            TMPMarine.setAllFields(searchableMarine);
            String field = getReader().read();
            if (!updatingFields.containsKey(field))
                throw new InvalidScriptException("Такого поля не существует.");
            updatingFields.get(field).update(TMPMarine);
            searchableMarine.setAllFields(TMPMarine);
        } catch (NumberFormatException e) {
            throw new InvalidScriptException("Некорректный ввод");
        } catch (InvalidSpaceMarineValueException | CreateCancelledException e) {
            throw new InvalidScriptException(e.getMessage());
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
        String answer = getReader().read();
        switch (answer) {
            case "Y", "y" -> {
                marine.setChapter(null);
                return true;
            }
            case "N", "n" -> {
                getSpeaker().speak("Значит обновим все поля части)");
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
        getSpeaker().speak("Данные успешно обновлены" + "\n" + marine);
        return true;
    }

    private boolean updateId(SpaceMarine marine) {
        getSpeaker().speak("Знаете, мы считаем, что обновлять id космодесантников не лучшая идея)");
        return true;
    }

    private boolean updateName(SpaceMarine marine) {
        String newName = getReader().read();
        if (newName == null) throw new InvalidScriptException("Не вводите пустую строку");
        marine.setName(newName);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateX(SpaceMarine marine) {
        String newXValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateY(SpaceMarine marine) {
        String newYValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCreationDate(SpaceMarine marine) {
        getSpeaker().speak("А как... заново родить его?...");
        return true;
    }

    private boolean updateHealth(SpaceMarine marine) {
        String newHealthValue = getReader().read();
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
        String newHeartCountValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCategory(SpaceMarine marine) {
        String newCategoryValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateMeleeWeapon(SpaceMarine marine) {
        String newMeleeWeaponValue = getReader().read();
        if (newMeleeWeaponValue == null) throw new InvalidScriptException("Не вводите пустую строку");
        MeleeWeapon newMeleeWeapon;
        try {
            newMeleeWeapon = MeleeWeapon.getMeleeWeapon(newMeleeWeaponValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidScriptException("Это не является оружием космодесантника.");
        }
        marine.setMeleeWeapon(newMeleeWeapon);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterName(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newChapterName = getReader().read();
        if (newChapterName == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        marine.getChapter().setName(newChapterName);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterParentLegion(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newParentLegion = getReader().read();
        marine.getChapter().setParentLegion(newParentLegion);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterMarinesCount(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newMarinesCountValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterWorld(SpaceMarine marine) {
        if (marine.getChapter() == null) {
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        String newChapterWorld = getReader().read();
        if (newChapterWorld == null) throw new InvalidScriptException("Не вводите пустую строку");
        marine.getChapter().setWorld(newChapterWorld);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}
