package com.yue.chip.core.common.enums;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.yue.chip.core.IEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum UserType implements IEnum {

    WEIXIN(0, "微信"), SYSTEM(1, "系统(平台)"),ORDINARY(2, "普通/APP");

    private final int key;

    private final String desc;

    public static  final String code = "userType";

    public static final String version = "1";

    private UserType(int key, String desc) {
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
    public static UserType instance(Object value){
        if (Objects.isNull(value)){
            return null;
        }
        if (NumberUtil.isInteger(String.valueOf(value))) {
            return instance(Integer.valueOf(String.valueOf(value)));
        }
        return instance(String.valueOf(value));
    }

    private static UserType instance(Integer key){
        for(UserType item : values()){
            if (item.getKey()==key){
                return item;
            }
        }
        return null;
    }

    private static UserType instance(String name){
        for(UserType item : values()){
            if(Objects.equals(item.getName(),name)){
                return item;
            }
        }
        return null;
    }

    public static class UserTypeConverter extends EnumConverter<UserType,Integer> {
    }
}
