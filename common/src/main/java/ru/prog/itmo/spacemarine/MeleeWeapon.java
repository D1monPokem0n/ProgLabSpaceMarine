package ru.prog.itmo.spacemarine;

public enum MeleeWeapon {
    MANREAPER("Manreaper"),
    LIGHTING_CLAW("Lighting claw"),
    POWER_FIST("Power fist");
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