package ru.prog.itmo.spacemarine;

public class Coordinates {
    private Float x; //Поле не может быть null
    private Double y; //Максимальное значение поля: 431, Поле не может быть null
    public Coordinates(Float x, Double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinate_x:"+x+",Coordinate_y:"+y;
    }
}
