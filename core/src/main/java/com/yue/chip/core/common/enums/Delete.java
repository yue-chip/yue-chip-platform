package com.yue.chip.core.common.enums;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.yue.chip.core.IEnum;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: Delete
 * @description: 是否删除标识（逻辑删除）
 * @date 2020/8/14上午9:43
 */
public enum Delete implements IEnum {

    FALSE(0, "未删除"), TRUE(1, "已删除");

    private final int key;

    private final String desc;

    private Delete(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public String code() {
        return "commonDelete";
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
    public static Delete instance(Object value){
        if (Objects.isNull(value)){
            return null;
        }
        if (NumberUtil.isInteger(String.valueOf(value))) {
            return instance(Integer.valueOf(String.valueOf(value)));
        }
        return instance(String.valueOf(value));
    }

    private static Delete instance(Integer key){
        for(Delete item : values()){
            if (item.getKey()==key){
                return item;
            }
        }
        return null;
    }

    private static Delete instance(String name){
        for(Delete item : values()){
            if(Objects.equals(item.getName(),name)){
                return item;
            }
        }
        return null;
    }



    public static class DeleteConverter extends EnumConverter<Delete,Integer> {
    }
}
