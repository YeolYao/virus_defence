package com.item;

import java.awt.*;

public class Print {
    public static void print(int time,String msg,Graphics g){
        //构造信息发布
        g.setColor(Color.red);
        String publicMsg="                    "+msg+"                     ";
        //降低滚动频率，每3单位时间刷新一次
        //控制在字符串显示完毕后，停止该操作
        if((Parameter.TIME-time)/Parameter.MSG_SHOW_SPEED+Parameter.MSG_SHOW_WIDTH<publicMsg.length()){
            g.drawString(publicMsg.substring((Parameter.TIME-time)/Parameter.MSG_SHOW_SPEED,(Parameter.TIME-time)/Parameter.MSG_SHOW_SPEED+20),Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+240);
        }
    }
    public static void print2(int time,String msg,Graphics g){
        //构造信息发布
        g.setColor(Color.red);
        String publicMsg="                                             "+msg+"                     ";
        //降低滚动频率，每3单位时间刷新一次
        //控制在字符串显示完毕后，停止该操作
        if((Parameter.TIME-time)/Parameter.MSG_SHOW_SPEED+Parameter.MSG_SHOW_WIDTH<publicMsg.length()){
            g.drawString(publicMsg.substring((Parameter.TIME-time)/Parameter.MSG_SHOW_SPEED,(Parameter.TIME-time)/Parameter.MSG_SHOW_SPEED+20),Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+260);
        }
    }
}
