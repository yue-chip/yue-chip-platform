package com.yue.chip.core.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yue.chip.core.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 返回结果枚举状态
 * @author: Mr.Liu
 * @create: 2020-01-13 11:39
 */
public enum ResultDataState implements IEnum {

    URI_INVALID(-100, "uri无效"),
    LOGIN_FAIL(403, "登录失败"),
    NOT_FOUND_SERVER(404, "找不到服务"),
    ERROR(-200, "错误"),
    BLOCK_REQUEST(-300, "请求拒绝"),
    SUCCESS(200, "成功"),
    NO_PERMISSION(401, "无权限");

    private final int key;

    private final String desc;

    public static final String code = "delete";

    public static final String version = "1";

    ResultDataState(int key, String desc) {

        this.key = key;
        this.desc = desc;
    }

    @JsonValue
    public Map<String, Object> jsonValue() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", getKey());
        map.put("desc", getDesc());
        map.put("name", getName());
        return map;
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
    public String getDesc() {
        return desc;
    }
}
