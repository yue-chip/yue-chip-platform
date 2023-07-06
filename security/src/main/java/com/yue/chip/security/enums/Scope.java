package com.yue.chip.security.enums;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.yue.chip.core.IEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 客户端权限
 * @author: mr.liu
 * @create: 2020-10-03 14:18
 **/
public enum Scope implements IEnum {

    READ(0, "读"), WRITE(1, "写"),UPDATE(2,"更新"),DELETE(3,"删除");

    private final int key;

    private final String desc;

    public static String code = "oauth2Scope";

    public static String version = "1";

    private Scope(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getDesc(){
        return desc;
    }

    @Override
    public Map<String, Object> jsonValue() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("desc", desc);
        map.put("name", getName());
        return map;
    }

    @JsonCreator
    public static Scope instance(Object value){
        if (Objects.isNull(value)){
            return null;
        }
        if (NumberUtil.isInteger(String.valueOf(value))) {
            return instance(Integer.valueOf(String.valueOf(value)));
        }
        return instance(String.valueOf(value));
    }

    private static Scope instance(Integer key){
        for(Scope item : values()){
            if (item.getKey()==key){
                return item;
            }
        }
        return null;
    }

    private static Scope instance(String name){
        for(Scope item : values()){
            if(Objects.equals(item.getName(),name)){
                return item;
            }
        }
        return null;
    }
}
