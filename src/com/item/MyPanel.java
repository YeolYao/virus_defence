package com.item;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyPanel extends JPanel implements Runnable{
    //创建人群
    private java.util.List<Person> personList=PersonPool.getPersonPool().getPersonList();
    private java.util.List<Person> observerList=PersonPool.getPersonPool().getObserverList();
    private Color[] colors={Color.white,Color.green,Color.yellow,Color.red,Color.black,null,null};

    //通过paint方法实现对Jpanel内部的绘制
    @Override
    public void paint(Graphics g){

        super.paint(g);
        this.setBackground(new Color(0x444444));

        for(Person person:personList) {
            //如果是隔离状态的人，停止一切行为并不进行绘制
            if(person.getType()==Person.FREEZE50||person.getType()==Person.FREEZE25){
                continue;
            }
            person.move();
            person.clearHealth();

            g.setColor(colors[person.getType()]);
            g.fillOval(person.getPoint().getX(), person.getPoint().getY(), 3, 3);
        }

        //绘制文字信息
        g.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g.setColor(Color.white);
        g.drawString("区域总人数："+Parameter.TOTAL_PERSON_SIZE,Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y);
        g.setColor(Color.white);
        g.drawString("健康者人数："+Parameter.HEALTH100_COUNT,Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+40);
        g.setColor(Color.yellow);
        g.drawString("疑似人数："+(Parameter.HEALTH50_COUNT+Parameter.HEALTH90_COUNT),Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+80);
        //g.drawString("疑似带病人数："+Parameter.HEALTH50_COUNT,Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+80);
        //g.drawString("疑似不带病人数："+Parameter.HEALTH90_COUNT,Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+100);
        g.setColor(Color.red);
        g.drawString("发病人数："+Parameter.HEALTH25_COUNT,Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+120);
        g.setColor(Color.black);
        g.drawString("死亡人数："+Parameter.DEATH_COUNT,Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+160);
        g.setColor(Color.white);
        g.drawString("世界时间："+(int)(Parameter.TIME/2.0),Parameter.MSG_BASE_X,Parameter.MSG_BASE_Y+200);
        if(Parameter.TIME>36) {
            Print.print(36, "通告：近期社会高发新型流行性病毒，请在家中自觉隔离，减少外出！！！", g);
            if(Parameter.TIME==42) {
                Parameter.MOVE_WISH = 0.3f;
            }
        }
        if(Parameter.TIME>56) {
            Print.print2(56, "通告：隔离防治医院已建设完成，请有明显病症人群自觉到医院就诊！！！", g);
            Parameter.MOVE_WISH = 0.001f;
        }
        if(Parameter.TIME>60) {
            //绘制医院，开始隔离
            Hospital.getHospital().paintHospital(g);
            Hospital.getHospital().clearHealth();
        }
        if(Parameter.TIME>70) {
            //绘制社区人员观察范围
            g.setColor(new Color(0x63A6996E, true));
            for (Person p : observerList) {
                if(p.getType()==Person.FREEZE50||p.getType()==Person.FREEZE25){
                    continue;
                }
                int x = p.getPoint().getX() - 25;
                int y = p.getPoint().getY() - 25;
                g.fillOval(x, y, 50, 50);
            }
        }
    }
    //自定义定时任务内部类，内部做重复刷新的动作
    class MyTask extends TimerTask{
        @Override
        public void run() {
            repaint();
            //时间累加
            Parameter.TIME++;
        }
    }
    @Override
    public void run(){
        //创建定时任务对象
        Timer t=new Timer();
        t.schedule(new MyTask(),0,100);
    }
}
