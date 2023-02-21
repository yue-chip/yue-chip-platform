package com.yue.chip.dubbo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.security.jackson2.CoreJackson2Module;

/**
 * @author Mr.Liu
 * @date 2023/2/20 下午3:12
 */
public class ObjectMapperCodec {
    private ObjectMapper mapper = new ObjectMapper();


    public ObjectMapperCodec(){
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new CoreJackson2Module());
    }

    public <T> T deserialize(byte [] bytes,Class<T> clazz) {
        try {
            if (bytes == null || bytes.length == 0) {
                return null;
            }

            return mapper.readValue(bytes, clazz);

        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("objectMapper! deserialize error %s", exception));
        }
    }

    public <T> T deserialize(String content,Class<T> clazz) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return deserialize(content.getBytes(), clazz);
    }
    public  String serialize(Object object) {
        try {
            if (object == null) {
                return null;
            }

            return mapper.writeValueAsString(object);

        } catch (Exception ex) {
            throw new RuntimeException(String.format("objectMapper! serialize error %s", ex));
        }
    }
}
