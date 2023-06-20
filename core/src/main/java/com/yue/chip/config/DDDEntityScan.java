package com.yue.chip.config;

import cn.hutool.core.util.ReflectUtil;
import com.yue.chip.annotation.YueChipDDDEntity;
import com.yue.chip.utils.SpringContextUtil;
import jakarta.annotation.Resource;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

/**
 * @author Mr.Liu
 * @date 2023/6/11 上午11:21
 */
@Component()
@ConditionalOnWebApplication
@ConditionalOnClass({Reflections.class})
public class DDDEntityScan implements CommandLineRunner {

    private String packageName = "com.yue.chip";

    @Override
    public void run(String... args) throws Exception {
        try {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(YueChipDDDEntity.class);
            allClasses.forEach(t -> {
                try {
                    Field[] fields = ReflectUtil.getFields(t);
                    for (Field field : fields) {
                        Resource resource = field.getAnnotation(Resource.class);
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        if (Objects.nonNull(resource) || Objects.nonNull(autowired)) {
                            Object obj = SpringContextUtil.getBean(field.getType());
                            if (Objects.nonNull(obj)) {
                                ReflectUtil.setFieldValue(t,field,obj);
                            }
                        }
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            });
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
