package com.example.demo.nio;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 〈一句话功能简述〉
 *  一分钟之内的 开盘价 收盘价 最高价 最低价
 *
 * @author bf
 * @create 2018/2/1
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ShareDemo {


    /** 一分钟之内的所有数据，如果最近的一分钟没有数据，就不会覆盖之前保存过的数据*/
    private Map<String, Object> oneMinuteData = new HashMap<>();

    /** 一分钟内的数据的 临时保存位置*/
    private Map<String, Object> tempDate = new HashMap<>();




    /** 数据收集间隔 - 一分钟*/
    private static final int CONTINUE_TIME = 60 * 1000;

    /** 最后更新时间戳*/
    private long lastUpdatedTimestamp = 0;

    private final Lock updateLock = new ReentrantLock();

    /**
     * 接收推送过来的数据
     */
    public void receiveDate(){
        Map<String, Object> data = new HashMap<>();

        // 推送过来的数据 保存到库里面

        // init 推送数据，oneMinuteData 就是上一分钟的所有推送数据集合，搞个有序的,直接拿第一个和最后一个，然后再计算最大和最小
        initDate(data);

    }

    /**
     *  处理一分钟内的所有数据
     * @param data
     */
    private void initDate(Map<String, Object> data){
        // 这个模拟 股票成交量
        long curTimestamp = System.currentTimeMillis();
        long time = curTimestamp - lastUpdatedTimestamp;

        if( time <= CONTINUE_TIME && time > 0){
            // 一分钟之内
            if(updateLock.tryLock()){
                try{
                    // recheck
                    if((curTimestamp - lastUpdatedTimestamp) <= CONTINUE_TIME){

                        lastUpdatedTimestamp = curTimestamp;


                        // todo 这里的操作是将 所有进来的数据 add 到 oneMinuteData 而不是覆盖

                        tempDate = data;


                    }
                }finally {
                    updateLock.unlock();
                }
            }
        }else{
            // 不是一分钟之内的数据，英文 lastUpdatedTimestamp 被赋值过，所以新生产的肯定会大于lastUpdatedTimestamp
            // 所以肯定是 一分钟之后的，这时候添加一个状态，再次更新 lastUpdatedTimestamp 的状态，这时候进去就是 一分钟的了

            lastUpdatedTimestamp = curTimestamp;


            // 超过一分钟后，从新赋值数据，并将之前所有的添加的数据
            oneMinuteData = tempDate;
            // 临时数据归 0
            tempDate = new HashMap<>();
            // 赋值数据
            tempDate = data;

        }
    }

}
