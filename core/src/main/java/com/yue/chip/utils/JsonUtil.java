package com.yue.chip.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yue.chip.core.YueChipObjectMapper;

/**
 * @description: json序列化工具
 * @author: Mr.Liu
 * @create: 2020-01-23 10:26
 */
public class JsonUtil {

    public static String convertToJsonString(final Object object) throws JsonProcessingException {
        YueChipObjectMapper yueChipObjectMapper = YueChipObjectMapper.getInstance();
        return yueChipObjectMapper.writeValueAsString(object);
    }
}
