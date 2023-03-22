package ru.prog.itmo.spacemarine;


public enum AstartesCategory {
    CHAPLAIN("Chaplain"),
    LIBRARIAN("Librarian"),   // chaplain > librarian > apothecary > scout
    APOTHECARY("Apothecary"),
    SCOUT("Scout");

    private final String name;

    AstartesCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}