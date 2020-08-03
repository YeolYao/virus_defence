package com.item;

import java.awt.*;
import java.util.Random;

public class Hospital {
    private static Hospital hospital=new Hospital();
    private Hospital(){}

    public static Hospital getHospital() {
        return hospital;
    }

    private Bed[][] beds=new Bed[39][33];
    {
        for (int i = 0; i < beds.length; i++) {
            for (int j = 0; j < beds[i].length; j++) {
                beds[i][j]=new Bed();
                beds[i][j].setX(i);
                beds[i][j].setY(j);
            }
        }
    }

    public void paintHospital(Graphics g){
        g.setColor(Color.white);
        g.drawString("隔离防治医院",Parameter.MSG_BASE_X+60,Parameter.MSG_BASE_Y+290);
        g.setColor(Color.green);
        g.drawRect(820,340,200,200);
        for(int i=0;i<beds.length;i++){
            for(int j=0;j<beds[i].length;j++){
                g.setColor(beds[i][j].getShowColor());
                g.fillRect(823+j*6,344+i*5,3,2);
            }
        }

        if(Parameter.HEALTH25_COUNT>39*33){
            g.setColor(Color.red);
            g.drawString("急需大量病床!!  急需病床数："+(Parameter.HEALTH25_COUNT-39*33),Parameter.MSG_BASE_X,560);
        }
    }

    //医院接诊工作(被送诊）
    public void receive(Person target,Person src){
        //患者处理，先检验，再收治
        if(hashVirus(target)){
            treat(target);
        }
        //送诊人处理，先检验，再收治
        if(hashVirus(src)){
            treat(src);
        }
    }

    //自愿就诊
    public void receive2(Person target){
        //患者处理，先检验，再收治
        if(hashVirus(target)){
            treat(target);
        }
    }

    //检验
    private boolean hashVirus(Person p){
        if(p.getType()==Person.HEALTH50||p.getType()==Person.HEALTH25){
            return true;
        }
        else{
            return false;
        }
    }

    //治疗
    private void treat(Person target){
        //分配病床
        out:
        for (int i = 0; i < beds.length; i++) {
            for (int j = 0; j < beds[i].length; j++) {
                if(beds[i][j].getPerson()==null){
                    //找到空床
                    beds[i][j].setPerson(target);
                    //设置状态为隔离状态
                    if(target.getType()==Person.HEALTH50){
                        target.setType(Person.FREEZE50);
                    }else if(target.getType()==Person.HEALTH25){
                        target.setType(Person.FREEZE25);
                    }
                    break out;
                }
            }
        }
    }

    //治疗健康值结算
    public void clearHealth(){
        Random r=new Random();
        for (int i = 0; i < beds.length; i++) {
            for (int j = 0; j < beds[i].length; j++) {
                Bed b = beds[i][j];
                Person p = b.getPerson();

                //没有人使用的病床、已经死亡的病人不结算
                if(p==null||p.getType()==Person.DEATH){
                    continue;
                }
                int health=p.getHealth();
                //病毒杀伤力
                if(p.getVirus()!=null) {
                    health-=p.getVirus().getDamage();
                }
                //免疫力
                health+=r.nextInt(4);
                //医院治疗
                health+=r.nextInt(8);
                p.setHealth(health);

                //满血复活
                if(p.getHealth()>=100){
                    //健康值设置为100
                    p.setHealth(100);
                    //病毒去掉
                    p.setVirus(null);
                    //状态变更为健康状态
                    Parameter.HEALTH100_COUNT++;
                    if(p.getType()==Person.FREEZE25){
                        Parameter.HEALTH25_COUNT--;
                    }else if(p.getType()==Person.FREEZE50){
                        Parameter.HEALTH50_COUNT--;
                    }
                    p.setType(Person.HEALTH100);
                    //原始病源集合中去掉当前带病人员
                    PersonPool.getPersonPool().getVirusList().remove(p);
                    //病床空出来
                    b.setPerson(null);
                }
                //死亡
                if(p.getHealth()<=0){
                    //病毒去掉
                    Parameter.DEATH_COUNT++;
                    if(p.getType()==Person.FREEZE25){
                        Parameter.HEALTH25_COUNT--;
                    }else if(p.getType()==Person.FREEZE50){
                        Parameter.HEALTH50_COUNT--;
                    }
                    p.setVirus(null);
                    //人的状态变更
                    p.setType(Person.DEATH);
                    //如果社区人员死亡，移除对应的人
                    if(p.isObservers()){
                        PersonPool.getPersonPool().getObserverList().remove(p);
                    }
                    b.setPerson(p);
                }
            }
        }
    }

}
