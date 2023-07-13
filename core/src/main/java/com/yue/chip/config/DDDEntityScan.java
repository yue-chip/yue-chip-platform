package com.yue.chip.config;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yue.chip.annotation.YueChipDDDEntity;
import com.yue.chip.utils.SpringContextUtil;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.modelmapper.internal.bytebuddy.description.annotation.AnnotationDescription;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
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
            ReferenceAnnotationBeanPostProcessor processor = new ReferenceAnnotationBeanPostProcessor();
            processor.setApplicationContext(SpringContextUtil.getApplicationContext());
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(YueChipDDDEntity.class);
            allClasses.forEach(t -> {
                try {
                    Field[] fields = ReflectUtil.getFieldsDirectly(t,false);
                    for (Field field : fields) {
                        Resource resource = field.getAnnotation(Resource.class);
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        DubboReference dubboReference = field.getAnnotation(DubboReference.class);
                        if (Objects.nonNull(resource) || Objects.nonNull(autowired)) {
                            setFieldValue(t,field);
                        }else if (Objects.nonNull(dubboReference)){
                            try {
                                SpringContextUtil.getBean(field.getType());
                            }catch (Exception e) {
                                //获取 foo 这个代理实例所持有的 InvocationHandler
                                InvocationHandler invocationHandler = Proxy.getInvocationHandler(dubboReference);
                                // 获取 AnnotationInvocationHandler 的 memberValues 字段
                                Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
                                // 因为这个字段事 private final 修饰，所以要打开权限
                                declaredField.setAccessible(true);
                                // 获取 memberValues
                                Map<String,Object> attributes = (Map<String,Object>) declaredField.get(invocationHandler);
                                processor.registerReferenceBean(field.getType().getSimpleName(),field.getType(),attributes,field);
                            }
                            setFieldValue(t,field);
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

    private void setFieldValue(Class<?> classz,Field field){
        Object obj = SpringContextUtil.getBean(field.getType());
        if (Objects.nonNull(obj)) {
            ReflectUtil.setFieldValue(classz,field,obj);
        }
    }
}
