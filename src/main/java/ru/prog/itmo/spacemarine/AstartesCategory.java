package ru.prog.itmo.spacemarine;

public enum AstartesCategory {
    SCOUT("Scout"),
    LIBRARIAN("Librarian"),
    CHAPLAIN("Chaplain"),
    APOTHECARY("Apothecary");
    private String name;
    AstartesCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}