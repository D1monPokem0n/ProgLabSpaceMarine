package ru.prog.itmo.spacemarine;

public enum MeleeWeapon {
    MANREAPER("Manreaper"),
    LIGHTING_CLAW("Lighting claw"),
    POWER_FIST("Power fist");
    private final String name;

    MeleeWeapon(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }
}