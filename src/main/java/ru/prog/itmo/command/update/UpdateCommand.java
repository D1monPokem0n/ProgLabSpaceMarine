package ru.prog.itmo.command.update;

import ru.prog.itmo.command.UserAsking;
import ru.prog.itmo.control.ConsoleArgument;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.storage.Storage;
import ru.prog.itmo.command.StorageIOCommand;
import ru.prog.itmo.reader.SpaceMarineReader;
import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.MeleeWeapon;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.CreateCancelledException;
import ru.prog.itmo.spacemarine.builder.InvalidSpaceMarineValueException;
import ru.prog.itmo.speaker.Speaker;

import java.util.HashMap;

public class UpdateCommand extends StorageIOCommand implements UserAsking {
    private final ConsoleArgument argument;
    private final HashMap<String, SpaceMarineUpdatable> updatingFields;

    public UpdateCommand(Storage storage, ConsoleArgument argument, Speaker speaker, SpaceMarineReader reader) {
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
                throw new InvalidSpaceMarineValueException("Морпеха с данным id не существует.");
            SpaceMarine TMPMarine = SpaceMarine.getTMPSpaceMarine();
            TMPMarine.setAllFields(searchableMarine);
            getSpeaker().speak("Вы начали обновлять значения полей у данного морпеха:\n\n" +
                    searchableMarine +
                    "\n\nДля отмены введите \"cancel\"\n" +
                    "\nВыберите, какое поле вы хотите обновить:\n" + updatingFields.keySet());
            boolean isUpdatingDone = false;
            while (!isUpdatingDone) {
                try {
                    String field = getReader().read();
                    if (field.equals("cancel")) throw new CreateCancelledException("Обновление отменено.");
                    if (!updatingFields.containsKey(field))
                        throw new InvalidSpaceMarineValueException("Такого поля не существует.\nПовторите ввод:");
                    while (!isUpdatingDone) {
                        try {
                            isUpdatingDone = updatingFields.get(field).update(TMPMarine);
                        } catch (InvalidSpaceMarineValueException e) {
                            getSpeaker().speak(e.getMessage());
                        }
                    }
                } catch (InvalidSpaceMarineValueException e) {
                    getSpeaker().speak(e.getMessage());
                }
            }
            searchableMarine.setAllFields(TMPMarine);
        } catch (NumberFormatException e) {
            getSpeaker().speak("Вам следует вводить число.");
        } catch (InvalidSpaceMarineValueException | UpdatingCancelledException | CreateCancelledException e) {
            getSpeaker().speak(e.getMessage());
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
            getSpeaker().speak("До этого часть была не назначена. " +
                    "\nДля обновления будет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        getSpeaker().speak("Вы хотите обнулить нынешнюю часть?(Y/N)");
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
            default -> {
                getSpeaker().speak("Вы ввели какую-то фигню. Обмновление прекращено");
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
                    getSpeaker().speak(e.getMessage());
                }
            }
        }
        getSpeaker().speak("Данные успешно обновлены" + "\n" + marine);
        return true;
    }

    private boolean updateId(SpaceMarine marine) {
        getSpeaker().speak("Знаете, мы считаем, что обновлять id космодесантников не лучшая идея)");
        return true;
    }

    private boolean updateName(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        getSpeaker().speak("Нынешнее имя: " + marine.getName() +
                "\nВведите новое имя: ");
        String newName = getReader().read();
        if (newName == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newName.equals("cancel")) throw new UpdatingCancelledException();
        marine.setName(newName);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateX(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        getSpeaker().speak("Нынешнее местоположение по оси OX: " + marine.getCoordinates().getX() +
                "\nВведите новое местоположение: ");
        String newXValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCoordinateY(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        getSpeaker().speak("Нынешнее местоположение по оси OY: " + marine.getCoordinates().getY() +
                "\nВведите новое местоположение: ");
        String newYValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCreationDate(SpaceMarine marine) {
        getSpeaker().speak("А как... заново родить его?...");
        return true;
    }

    private boolean updateHealth(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        getSpeaker().speak("Нынешнее количество здоровья: " + marine.getHealth() +
                "\nВведите новое здоровье: ");
        String newHealthValue = getReader().read();
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
        getSpeaker().speak("Нынешнее число сердец: " + marine.getHeartCount() +
                "\nВведите новое число сердец: ");
        String newHeartCountValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateCategory(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        getSpeaker().speak("Нынешняя категория десантника: " + marine.getCategory().getName());
        getSpeaker().speak("""
                Существует 4 категории космодесантников:
                Chaplain, Librarian, Apothecary, Scout
                Введите новую категорию вашего солдата:
                """);
        String newCategoryValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateMeleeWeapon(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        getSpeaker().speak("Нынешнее оружие десантника: " + marine.getMeleeWeapon().getName());
        getSpeaker().speak("""
                Существует 3 вида оружия космодесантников:
                Manreaper, Lighting claw, Power fist
                Введите новое оружие вашего солдата:
                """);
        String newMeleeWeaponValue = getReader().read();
        if (newMeleeWeaponValue == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newMeleeWeaponValue.equals("cancel")) throw new UpdatingCancelledException();
        MeleeWeapon newMeleeWeapon;
        try {
            newMeleeWeapon = MeleeWeapon.getMeleeWeapon(newMeleeWeaponValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidSpaceMarineValueException("Это не является оружием космодесантника.");
        }
        marine.setMeleeWeapon(newMeleeWeapon);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterName(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        if (marine.getChapter() == null) {
            getSpeaker().speak("До этого часть была не назначена. " +
                    "\nДля обновления будет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        getSpeaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новое название части: ");
        String newChapterName = getReader().read();
        if (newChapterName == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newChapterName.equals("cancel")) throw new UpdatingCancelledException();
        marine.getChapter().setName(newChapterName);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterParentLegion(SpaceMarine marine) throws UpdatingCancelledException {
        if (marine.getChapter() == null) {
            getSpeaker().speak("До этого часть была не назначена. " +
                    "\nБудет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        getSpeaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новоый легион: ");
        String newParentLegion = getReader().read();
        if (newParentLegion == null){
            marine.getChapter().setParentLegion(null);
            return true;
        }
        if (newParentLegion.equals("cancel")) throw new UpdatingCancelledException();
        marine.getChapter().setParentLegion(newParentLegion);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterMarinesCount(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        if (marine.getChapter() == null) {
            getSpeaker().speak("До этого часть была не назначена. " +
                    "\nБудет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        getSpeaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новое число солдат в части: ");
        String newMarinesCountValue = getReader().read();
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
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    private boolean updateChapterWorld(SpaceMarine marine) throws InvalidSpaceMarineValueException, UpdatingCancelledException {
        if (marine.getChapter() == null) {
            getSpeaker().speak("До этого часть была не назначена. " +
                    "\nБудет создана часть со стандартными значениями");
            Chapter chapter = new Chapter();
            marine.setChapter(chapter);
        }
        getSpeaker().speak("Нынешняя часть: " + marine.getChapter() +
                "\nВведите новое название мира, в котороом расположена часть: ");
        String newChapterWorld = getReader().read();
        if (newChapterWorld == null) throw new InvalidSpaceMarineValueException("Не вводите пустую строку");
        if (newChapterWorld.equals("cancel")) throw new UpdatingCancelledException();
        marine.getChapter().setWorld(newChapterWorld);
        getSpeaker().speak("Значение поля обновлено успешно");
        return true;
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному.";
    }
}
