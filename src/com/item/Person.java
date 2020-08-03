package com.item;

import java.util.Random;

public class Person {

    //使用状态描述不同的人
    public static final int HEALTH100=0; //健康
    public static final int HEALTH90=1; //与病源接触过，未染病，疑似
    public static final int HEALTH50=2; //与病源接触过，且染病，疑似(14天潜伏期后变为病源)
    public static final int HEALTH25=3; //病源
    public static final int DEATH=4; //死亡
    public static final int FREEZE50=5; //被隔离的疑似
    public static final int FREEZE25=6; //被隔离的发病


    private Point point;
    private Virus virus;
    Random r=new Random();
    //健康状态类型
    private int type;
    //健康值，使用100作为上限
    private int health;
    //感染时间
    private int infectedTime;
    //社区人员标志
    private boolean observers;
    //是否戴口罩
    private boolean MASK=false;

    public Person(int x,int y){
        this.point=new Point(x,y);
    }

    public void move(){
        if(this.getType()==Person.DEATH){
            return;
        }
        //戴口罩意愿
        if(Parameter.TIME>35&&Math.random()<Parameter.MASK_WISH&&this.isMASK()==false){
            this.setMASK(true);
        }
        //社区防控人员检测操作
        //判断观察范围内每一个人（全部人）状态
            //如果满足条件，调用医院的接诊接口
        if(this.isObservers()&&Parameter.TIME>70){
            //遍历所有人的集合
            for(Person p:PersonPool.getPersonPool().getPersonList()){
                if(MathUtil.getDistance(this,p)<25){
                    //判断状态
                    if(p.getType()==Person.HEALTH25||p.getType()==Person.HEALTH50||p.getType()==Person.HEALTH90){
                        Hospital.getHospital().receive(p,this);
                        return;
                    }
                }
            }
        }

        //判定患病人员是否想去医院
        if(Parameter.TIME>70&&this.getType()==Person.HEALTH25&&Math.random()<Parameter.Hospital_WISH){
            //去医院就诊
            Hospital.getHospital().receive2(this);
            return;
        }

        //判定移动意愿,社区人员过滤
        if(Math.random()>Parameter.MOVE_WISH&&!this.isObservers()) {
            return;
        }
        //人的移动行为
        int x = this.getPoint().getX() + MathUtil.getGaussian(2);
        int y = this.getPoint().getY() + MathUtil.getGaussian(2);
        //控制点的移动不会超过边界
        x=x>Parameter.WIDTH?Parameter.WIDTH:x;
        x=x<0?0:x;
        y=y>Parameter.HEIGHT?Parameter.HEIGHT:y;
        y=y<0?0:y;
        this.getPoint().setX(x);
        this.getPoint().setY(y);

        //传染事件
        if(this.getType()>1){
            return;
        }
        for(Person p:PersonPool.getPersonPool().getVirusList()){
            if(MathUtil.getDistance(this,p)>Parameter.VIRUS_DISTANCE){
                continue;
            }

            //传染率40%,戴口罩传染率减半
            if(this.isMASK()==true){
                if(Math.random()<(Parameter.HITS/2.0)){
                    //传染,接触
                    if(this.getType()==Person.HEALTH100)
                        Parameter.HEALTH100_COUNT--;
                    else if(this.getType()==Person.HEALTH90)
                        Parameter.HEALTH90_COUNT--;
                    this.setType(Person.HEALTH50);
                    this.setInfectedTime(Parameter.TIME);
                    //病毒加载到病人身上
                    this.setVirus(Virus.getVirus());
                    PersonPool.getPersonPool().getVirusList().add(this);
                    Parameter.HEALTH50_COUNT++;
                    break;
                }else{
                    //未传染，接触
                    // 如果是疑似未染病，无需向下执行
                    if(this.getType()==Person.HEALTH90)
                        continue;
                    Parameter.HEALTH90_COUNT++;
                    Parameter.HEALTH100_COUNT--;
                    this.setInfectedTime(Parameter.TIME);
                    this.setType(Person.HEALTH90);
                    //继续与病患进行比对
                }
            } else{
                if(Math.random()<Parameter.HITS){
                    this.setInfectedTime(Parameter.TIME);
                    if(this.getType()==Person.HEALTH100)
                        Parameter.HEALTH100_COUNT--;
                    else if(this.getType()==Person.HEALTH90)
                        Parameter.HEALTH90_COUNT--;
                    this.setType(Person.HEALTH50);
                    this.setVirus(Virus.getVirus());
                    PersonPool.getPersonPool().getVirusList().add(this);
                    Parameter.HEALTH50_COUNT++;
                    break;
                }else{
                    if(this.getType()==Person.HEALTH90)
                        continue;
                    this.setInfectedTime(Parameter.TIME);
                    Parameter.HEALTH90_COUNT++;
                    Parameter.HEALTH100_COUNT--;
                    this.setType(Person.HEALTH90);
                }
           }

        }
    }

    //健康值结算
    public void clearHealth(){
        //接触未感染的人,潜伏期到变为健康人
        if(this.getType()==Person.HEALTH90){
            if(Parameter.TIME-this.getInfectedTime()==Parameter.SHADOW_TIME){
                this.setType(Person.HEALTH100);
                Parameter.HEALTH90_COUNT--;
                Parameter.HEALTH100_COUNT++;
            }
        }
        //不带病毒的人、已经死亡的人不结算
        if(this.getVirus()==null||this.getType()==Person.DEATH){
            return;
        }
        //接触感染的人，潜伏期未到不发病,健康值减少少量，到了变为病源
        if(this.getType()==Person.HEALTH50){
            this.setHealth(this.getHealth()-2);
            if(this.getHealth()<=0){
                this.setHealth(70);
            }
            if(Parameter.TIME-this.getInfectedTime()==Parameter.SHADOW_TIME){
                this.setType(Person.HEALTH25);
                Parameter.HEALTH50_COUNT--;
                Parameter.HEALTH25_COUNT++;
            }
        }else if(this.getType()==Person.HEALTH25){
            //病源：健康值-病毒伤害，结果保存到原始人的健康值中
            this.setHealth(this.getHealth()-this.getVirus().getDamage());
            //免疫力设定
            this.setHealth(this.getHealth()+r.nextInt(5));
            if(this.getHealth()>=100){
                this.setHealth(80);
            }
            //死亡
            if(this.getHealth()<=0){
                //病毒去掉
                this.setVirus(null);
                Parameter.HEALTH25_COUNT--;
                Parameter.DEATH_COUNT++;
                //人的状态变更
                this.setType(Person.DEATH);
                //如果社区人员死亡，移除对应的人
                if(this.isObservers()){
                    PersonPool.getPersonPool().getObserverList().remove(this);
                }
            }
        }

    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Virus getVirus() {
        return virus;
    }

    public void setVirus(Virus virus) {
        this.virus = virus;
    }

    public boolean isObservers() {
        return observers;
    }

    public void setObservers(boolean observers) {
        this.observers = observers;
    }

    public int getInfectedTime() {
        return infectedTime;
    }

    public void setInfectedTime(int infectedTime) {
        this.infectedTime = infectedTime;
    }

    public boolean isMASK() {
        return MASK;
    }

    public void setMASK(boolean MASK) {
        this.MASK = MASK;
    }
}
