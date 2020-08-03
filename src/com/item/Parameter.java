package com.item;

public class Parameter {
    //总人口
    public static final int TOTAL_PERSON_SIZE=8000;
    //初始发病病人群数量
    public static final int HEALTH25=3;
    //统计健康人群数量
    public static int HEALTH100_COUNT=TOTAL_PERSON_SIZE-HEALTH25;
    //统计疑似带病人群数量
    public static int HEALTH50_COUNT=0;
    //统计疑似不带病人群数量
    public static int HEALTH90_COUNT=0;
    //统计发病病人群数量
    public static int HEALTH25_COUNT=HEALTH25;
    //统计死亡人数
    public static int DEATH_COUNT=0;

    //传染命中率
    public static final float HITS=0.4f;
    //传染距离
    public static final int VIRUS_DISTANCE=5;
    //潜伏期（14天）
    public static final int SHADOW_TIME=28;
    //移动意愿
    public static float MOVE_WISH=0.9f;
    //去医院意愿
    public static float Hospital_WISH=0.8f;
    //戴口罩意愿
    public static float MASK_WISH=0.8f;

    //定义世界时间
    public static int TIME=0;
    //区域宽度与高度
    public static final int WIDTH=800;
    public static final int HEIGHT=800;
    //消息显示基础坐标
    public static final int MSG_BASE_X=820;
    public static final int MSG_BASE_Y=40;
    //消息滚动速度 1-5,快-慢
    public static final int MSG_SHOW_SPEED=3;
    //消息显示宽度
    public static final int MSG_SHOW_WIDTH=20;
}
