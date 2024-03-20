package com.yue.chip.core.common.enums;

import com.yue.chip.core.IEnum;
import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description: 枚举转换器
 * @author: Mr.Liu
 * @create: 2020-04-23 15:54
 **/
public abstract class EnumConverter<T extends IEnum,K extends Serializable> implements AttributeConverter<T,K> {

    @Override
    public K convertToDatabaseColumn(T t) {
        if (Objects.equals( getKClass(), String.class)) {
            return Objects.isNull(t)?null: (K) ((IEnum)t).getName();
        }
        return Objects.isNull(t)?null: (K) ((IEnum)t).getKey();
    }

    @Override
    public T convertToEntityAttribute(K k) {
        List<IEnum> list = initialEnum(getTClass());
        for (IEnum ienum : list){
            if (Objects.equals( getKClass(), String.class)) {
                if(Objects.equals(ienum.getName(),((String) k))){
                    return (T) ienum;
                }
            }
            if(ienum.getKey()== ((Integer) k)){
                return (T) ienum;
            }
        }
        return null;
    }

    private Class<T> getTClass()
    {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    private Class<K> getKClass()
    {
        Class<K> kClass = (Class<K>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return kClass;
    }

    private List<IEnum> initialEnum(Class<?> cls) {
        List<IEnum> list = new ArrayList<>();
        try {
            Method method = cls.getMethod("values");
            IEnum inter[] = (IEnum[]) method.invoke(null);
            for (IEnum ienum : inter) {
                list.add(ienum);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

}
