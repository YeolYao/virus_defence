package com.item;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        personInit();
        frameInit();
    }
    private static void personInit(){
        List<Person> personList=PersonPool.getPersonPool().getPersonList();
        Random r=new Random();
        for(int i=1;i<=Parameter.TOTAL_PERSON_SIZE;i++){
            Person p=new Person(MathUtil.getGaussian(130)+Parameter.WIDTH/2,MathUtil.getGaussian(120)+Parameter.HEIGHT/2);
            //设置人的健康值为健康
            p.setType(Person.HEALTH100);
            //初始化人的健康值为80-100
            p.setHealth(r.nextInt(10)+90);
            personList.add(p);
        }
        //添加3个病源对象
        for (int i = 0; i < Parameter.HEALTH25; i++) {
            Person person;
            do {
                person=personList.get(r.nextInt(Parameter.TOTAL_PERSON_SIZE));//随机挑选一个市民
            } while (person.getType()==Person.HEALTH25);//如果该市民已经被感染，重新挑选
            person.setType(Person.HEALTH25);
            person.setVirus(Virus.getVirus());
            //将带病人群加入病源集合
            PersonPool.getPersonPool().getVirusList().add(person);
        }

        //初始化社区人员
        //临时的set集合保存社区人员，保障不重复
        Set<Person> temp=new HashSet<Person>();
        do{
            int idx=r.nextInt(Parameter.TOTAL_PERSON_SIZE);
            Person p=personList.get(idx);
            //设置其变为社区人员
            p.setObservers(true);
            temp.add(p);
        }while(temp.size()<15);
        PersonPool.getPersonPool().getObserverList().addAll(temp);
    }

    private static void frameInit() {
        //制作一个窗口
        JFrame frame=new JFrame();
        //设置窗口大小（800+300)*800
        frame.setSize(Parameter.WIDTH+300,Parameter.HEIGHT);
        frame.setTitle("病毒传播模拟仿真程序");
        //设置窗口位置在屏幕正中间
        frame.setLocationRelativeTo(null);
        //设置窗体的默认关闭按钮的功能
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //添加一个面板对象用于绘图，添加面板到窗体内
        MyPanel panel=new MyPanel();
        frame.add(panel);

        //显示窗口
        frame.setVisible(true);

        //启动新的线程
        Thread t=new Thread(panel);
        t.start();
    }
}
