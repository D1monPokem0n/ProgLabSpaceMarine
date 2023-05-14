package ru.prog.itmo.spacemarine.coordinates;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Float x; //Поле не может быть null
    private Double y; //Максимальное значение поля: 431, Поле не может быть null

    public Coordinates(Float x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinate_x:" + x + ",Coordinate_y:" + y;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getX() {
        return x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getY() {
        return y;
    }
}
