package ru.prog.itmo.spacemarine;


import java.io.Serializable;

public enum AstartesCategory implements Serializable {
    CHAPLAIN("CHAPLAIN"),
    LIBRARIAN("LIBRARIAN"),   // chaplain > librarian > apothecary > scout
    APOTHECARY("APOTHECARY"),
    SCOUT("SCOUT");

    private final String name;

    AstartesCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AstartesCategory getCategory(String categoryName) throws IllegalArgumentException{
        switch (categoryName){
            case "Chaplain" -> {
                return CHAPLAIN;
            }
            case "Librarian" -> {
                return LIBRARIAN;
            }
            case "Apothecary" -> {
                return APOTHECARY;
            }
            case "Scout" -> {
                return SCOUT;
            }
            default -> throw new IllegalArgumentException();
        }
    }
}