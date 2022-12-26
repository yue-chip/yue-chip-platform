package com.yue.chip.utils.id;

/**
 * @description: 雪花算法工具类
 * @author: Mr.Liu
 * @create: 2020-01-04 10:59
 */
public class SnowflakeUtil {

    /**
     *
     */
    private static class SnowflakeHolder{
        private static Snowflake snowflake = new Snowflake(0, 0);
    }

    /**
     * 获取ID
     * @return
     */
    public static synchronized long getId(){
        return SnowflakeHolder.snowflake.nextId();
    }

}
