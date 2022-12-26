package com.yue.chip.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yue.chip.core.IEnum;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author mr.liu
 * @Description:
 * @date 2020/9/15下午2:20
 */
public class EnumUtil {

    /**
     * 获得指定包下面的所有的枚举类的值
     *
     * @param packageName
     * @return
     */
    public static Map<String, Object> getAllEnumsInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends IEnum>> allClasses = reflections.getSubTypesOf(IEnum.class);
        Map<String, Object> result = new HashMap<String, Object>();
        allClasses.forEach(t -> {
            try {
                List<String> list = new ArrayList<String>();
                Method method = t.getMethod("values");
                IEnum inter[] = (IEnum[]) method.invoke(null);
                for (IEnum ienum : inter) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("key", ienum.getKey());
                    map.put("desc", ienum.getDesc());
                    map.put("name", ienum.getName());
                    list.add(objectMapper.writeValueAsString(map));
                }
//                String code = ((IEnum)t).code();
//                result.put(ienum.code(),list);
            }catch (Exception exception){

            }
        });
        return result;
    }

    public static void main(String[] args) {
        EnumUtil.getAllEnumsInPackage("com.lion.core.common.enums")
                .forEach((k, v) -> System.out.println(k + "=" + v));
        Reflections reflections = new Reflections("com.lion.core.common.enums");
    }
}
