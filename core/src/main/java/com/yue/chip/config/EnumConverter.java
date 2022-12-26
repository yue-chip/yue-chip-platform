package com.yue.chip.config;

import cn.hutool.core.util.NumberUtil;
import com.yue.chip.core.IEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @description: 枚举转换
 * @author:
 * @create: 2021-01-26 17:21
 */
@Component
@ConditionalOnWebApplication
public class EnumConverter implements ConverterFactory<String, IEnum> {
    @Override
    public <T extends IEnum> Converter<String, T> getConverter(Class<T> aClass) {
        return new StringToEnum<>(aClass);
    }

    private static class StringToEnum<T extends IEnum> implements Converter<String, T> {
        private Class<T> targerType;
        public StringToEnum(Class<T> targerType) {
            this.targerType = targerType;
        }

        @Override
        public T convert(String source) {
            if (!StringUtils.hasText(source)) {
                return null;
            }
            return (T) EnumConverter.getIEnum(this.targerType, source);
        }
    }

    public static <T extends IEnum> Object getIEnum(Class<T> targerType, String source) {
        for (T enumObj : targerType.getEnumConstants()) {
            if (NumberUtil.isLong(source) || NumberUtil.isInteger(source) ) {
                if (enumObj.getKey() == Integer.valueOf(source)){
                    return enumObj;
                }
            }else {
                if (Objects.equals(enumObj.getName(),source)) {
                    return enumObj;
                }
            }
        }
        return null;
    }
}
