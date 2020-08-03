package com.item;

import java.util.Random;

public class Virus {
    private static Virus virus=new Virus();
    private Virus(){}
    public static Virus getVirus(){
        return virus;
    }

    private int damage;
    private Random r=new Random();

    public int getDamage() {
        //获取杀伤力的时候随机产生
        return r.nextInt(3)+2;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
