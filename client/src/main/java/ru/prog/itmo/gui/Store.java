package ru.prog.itmo.gui;

import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class Store {
    /*
     private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private final java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private String ownerUser;
     */
    private ArrayList<SpaceMarine> marines;

    private Comparator<? super SpaceMarine> sortComparator;
    private boolean reversedSort;
    private String filter;
    private Map<String, Comparator<? super SpaceMarine>> sortComparatorMap = new HashMap<>();

    public static final String[] FIELDS_NAME = {
            "id", "name", "coordinate_x", "coordinate_y", "creation_date",
            "health", "heart_count", "category", "weapon",
            "chapter_name", "chapter_parent_legion", "chapter_marines_count", "chapter_world",
            "user_owner"
    };

    public Store(ArrayList<SpaceMarine> marines) {
        this.marines = marines;
        sortComparator = Comparator.naturalOrder();
        reversedSort = false;
        filter = "";
        initSortMap();
    }

    private void initSortMap() {
        sortComparatorMap = Map.ofEntries(
                entry("None", Comparator.naturalOrder()),
                entry(FIELDS_NAME[0], Comparator.comparing(SpaceMarine::getId)),
                entry(FIELDS_NAME[1], Comparator.comparing(SpaceMarine::getName)),
                entry(FIELDS_NAME[2], Comparator.comparing(SpaceMarine::getCoordinatesX)),
                entry(FIELDS_NAME[3], Comparator.comparing(SpaceMarine::getCoordinatesY)),
                entry(FIELDS_NAME[4], Comparator.comparing(SpaceMarine::getCreationDate)),
                entry(FIELDS_NAME[5], Comparator.comparing(SpaceMarine::getHealth)),
                entry(FIELDS_NAME[6], Comparator.comparing(SpaceMarine::getHeartCount)),
                entry(FIELDS_NAME[7], comparatorByCategory()),
                entry(FIELDS_NAME[8], Comparator.comparing(SpaceMarine::getMeleeWeapon)),
                entry(FIELDS_NAME[9], comparatorByChapterName()),
                entry(FIELDS_NAME[10], comparatorByChapterParentLegion()),
                entry(FIELDS_NAME[11], comparatorByChapterMarinesCount()),
                entry(FIELDS_NAME[12], comparatorByChapterWorld()),
                entry(FIELDS_NAME[13], Comparator.comparing(SpaceMarine::getOwnerUser))
        );
    }

    private Comparator<? super SpaceMarine> comparatorByCategory() {
        return (o1, o2) -> AstartesCategory.comparing(o1.getCategory(), o2.getCategory());
    }

    private Comparator<? super SpaceMarine> comparatorByChapterName() {
        return (o1, o2) -> {
            if (o1.getChapter() == null && o2.getChapter() == null) return 0;
            if (o1.getChapter() == null) return -1;
            if (o2.getChapter() == null) return 1;
            return String.CASE_INSENSITIVE_ORDER.compare(o1.getChapter().getName(), o2.getChapter().getName());
        };
    }

    private Comparator<? super SpaceMarine> comparatorByChapterParentLegion() {
        return (o1, o2) -> {
            if (o1.getChapter() == null && o2.getChapter() == null) return 0;
            if (o1.getChapter() == null) return -1;
            if (o2.getChapter() == null) return 1;
            var c1 = o1.getChapter();
            var c2 = o2.getChapter();
            if (c1.getParentLegion() == null && c2.getParentLegion() == null) return 0;
            if (c1.getParentLegion() == null) return -1;
            if (c2.getParentLegion() == null) return 1;
            return c1.getParentLegion().compareTo(c2.getParentLegion());
        };
    }

    private Comparator<? super SpaceMarine> comparatorByChapterMarinesCount() {
        return (o1, o2) -> {
            if (o1.getChapter() == null && o2.getChapter() == null) return 0;
            if (o1.getChapter() == null) return -1;
            if (o2.getChapter() == null) return 1;
            return Long.compare(o1.getChapter().getMarinesCount(),
                    o2.getChapter().getMarinesCount());
        };
    }

    private Comparator<? super SpaceMarine> comparatorByChapterWorld() {
        return (o1, o2) -> {
            if (o1.getChapter() == null && o2.getChapter() == null) return 0;
            if (o1.getChapter() == null) return -1;
            if (o2.getChapter() == null) return 1;
            return o1.getChapter().getWorld().compareTo(
                    o2.getChapter().getWorld()
            );
        };
    }


    public void setMarines(ArrayList<SpaceMarine> marines) {
        var newMarines = new ArrayList<SpaceMarine>();
        var sortedMarines = marines
                .stream()
                .filter((marine) -> marine.toLineString().contains(filter))
                .sorted(sortComparator);
        if (reversedSort) sortedMarines
                .collect(Collectors.toCollection(ArrayDeque::new))
                .descendingIterator()
                .forEachRemaining(newMarines::add);
        else newMarines = sortedMarines.collect(Collectors.toCollection(ArrayList::new));
        this.marines = newMarines;
    }

    public void setSortItem(String sortItem) {
        this.sortComparator = sortComparatorMap.get(sortItem);
    }

    public void setFilterItem(String filterItem) {
        if (filterItem == null || filterItem.trim().equals(""))
            filterItem = "";
        this.filter = filterItem;
    }

    public void setReversedSort(boolean reversedSort) {
        this.reversedSort = reversedSort;
    }

    public int size() {
        return marines.size();
    }

    public int getMarineFieldsCount() {
        return SpaceMarine.getFieldsCount();
    }

    public String getValueAt(int index, int fieldIndex) {
        var marine = marines.get(index);
        Object value;
        var splitMarine = splitMarine(marine);
        if (fieldIndex > 8 && fieldIndex < 13 && splitMarine[9] == null) {
            value = "NoValue";
        } else value = splitMarine[fieldIndex];
        return value == null ? "NoValue" : value.toString();
    }

    private Object[] splitMarine(SpaceMarine marine) {
        var splitChapter = splitChapter(marine.getChapter());
        return new Object[]{
                marine.getId(),
                marine.getName(),
                marine.getCoordinates().getX(),
                marine.getCoordinates().getY(),
                marine.getCreationDate(),
                marine.getHealth(),
                marine.getHeartCount(),
                marine.getCategory(),
                marine.getMeleeWeapon(),
                splitChapter[0],
                splitChapter[1],
                splitChapter[2],
                splitChapter[3],
                marine.getOwnerUser()
        };
    }

    private Object[] splitChapter(Chapter chapter) {
        if (chapter == null) {
            return new Object[]{null, null, null, null};
        }
        return new Object[]{
                chapter.getName(),
                chapter.getParentLegion(),
                chapter.getMarinesCount(),
                chapter.getWorld()
        };
    }

    public String getFieldName(int index) {
        return FIELDS_NAME[index];
    }

    public ArrayList<SpaceMarine> getMarines() {
        return marines;
    }
}
