package com.yue.chip.security.enums;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.yue.chip.core.IEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 客户端鉴权方式
 * @author: mr.liu
 * @create: 2020-10-03 14:22
 **/
public enum GrantTypes implements IEnum {
    AUTHORIZATION_CODE(0, "授权码模式"), PASSWORD(1, "密码模式"),CLIENT_CREDENTIALS(2,"客户端模式"),IMPLICIT(3,"简化模式"),REFRESH_TOKEN(4,"刷新TOKEN模式"),EXTENSION(5,"扩展模式");

    private final int key;

    private final String desc;

    public static String code = "grantTypes";

    public static String version = "1";

    private GrantTypes(int key, String desc) {
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
    public static GrantTypes instance(Object value){
        if (Objects.isNull(value)){
            return null;
        }
        if (NumberUtil.isInteger(String.valueOf(value))) {
            return instance(Integer.valueOf(String.valueOf(value)));
        }
        return instance(String.valueOf(value));
    }

    private static GrantTypes instance(Integer key){
        for(GrantTypes item : values()){
            if (item.getKey()==key){
                return item;
            }
        }
        return null;
    }

    private static GrantTypes instance(String name){
        for(GrantTypes item : values()){
            if(Objects.equals(item.getName(),name)){
                return item;
            }
        }
        return null;
    }


}
