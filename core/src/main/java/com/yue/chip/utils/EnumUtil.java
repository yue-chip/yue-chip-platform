package com.yue.chip.utils;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yue.chip.core.IEnum;
import com.yue.chip.core.common.enums.EnumPersistenceBean;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
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
    public static List<EnumPersistenceBean> getAllEnumsInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends IEnum>> allClasses = reflections.getSubTypesOf(IEnum.class);
        Map<String, Object> result = new HashMap<String, Object>();
        List<EnumPersistenceBean> returnList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        allClasses.forEach(t -> {
            try {
                List<String> list = new ArrayList<String>();
                Field fieldCode = ReflectUtil.getField(t,"code");
                Field fieldVersion = ReflectUtil.getField(t,"version");
                String code = "";
                String version = "";
                if (Objects.nonNull(fieldCode)) {
                    code = (String) fieldCode.get(t);
                }
                if (Objects.nonNull(fieldVersion)) {
                    version = (String) fieldVersion.get(t);
                }
                if (StringUtils.hasText(code) && StringUtils.hasText(version)) {
                    IEnum inter[] = (IEnum[]) t.getMethod("values").invoke(null);
                    for (IEnum ienum : inter) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("key", ienum.getKey());
                        map.put("desc", ienum.getDesc());
                        map.put("name", ienum.getName());
                        list.add(objectMapper.writeValueAsString(map));
                    }
                    returnList.add(EnumPersistenceBean.builder()
                            .value(objectMapper.writeValueAsString(list))
                            .version(version)
                            .code(code)
                            .build());
                }
            }catch (Exception exception){

            }
        });
        return returnList;
    }

    public static void main(String[] args) {
        EnumUtil.getAllEnumsInPackage("com.yue.chip.core.common.enums")
                .forEach(enumPersistenceBean -> {
                    System.out.println(enumPersistenceBean);
                });
//        Reflections reflections = new Reflections("com.yue.chip.core.common.enums");
    }
}
