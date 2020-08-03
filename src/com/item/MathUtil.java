package com.item;

import java.util.Random;

public class MathUtil {
    private static Random r=new Random();
    public static int getGaussian(int range){
        return (int)(r.nextGaussian()*range);
    }
    public static double getDistance(Person p1,Person p2){
        Point pp1=p1.getPoint();
        Point pp2=p2.getPoint();
        int x=pp1.getX()-pp2.getX();
        int y=pp1.getY()-pp2.getY();
        return Math.sqrt(x*x+y*y);
    }
}
