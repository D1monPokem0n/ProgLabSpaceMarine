package ru.prog.itmo.spacemarine;

import java.io.Serializable;

public enum MeleeWeapon implements Serializable {
    MANREAPER("MANREAPER"),
    LIGHTING_CLAW("LIGHTING_CLAW"),
    POWER_FIST("POWER_FIST");
    private final String name;

    MeleeWeapon(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


    public static MeleeWeapon getMeleeWeapon(String weaponName) throws IllegalArgumentException{
        switch (weaponName){
            case "Manreaper" -> {
                return MANREAPER;
            }
            case "Lighting claw" -> {
                return LIGHTING_CLAW;
            }
            case "Power fist" -> {
                return POWER_FIST;
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }
    }
}