package com.item;

import java.awt.*;

public class Bed {
    private int x;
    private int y;
    private Person person;
    private Color showColor=Color.cyan;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        if(person==null){
            this.showColor=Color.cyan;
            return;
        }
        if(person.getType()==Person.DEATH){
            this.showColor=Color.black;
            return;
        }
        if(person.isObservers()){
            this.showColor = Color.yellow;
            return;
        }
        this.showColor=Color.red;
    }

    public Color getShowColor() {
        return showColor;
    }

    public void setShowColor(Color showColor) {
        this.showColor = showColor;
    }
}
