package com.yue.chip.config;

import cn.hutool.core.util.ReflectUtil;
import com.yue.chip.annotation.YueChipDDDEntity;
import com.yue.chip.utils.SpringContextUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Mr.Liu
 * @date 2023/6/11 上午11:21
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnWebApplication
@ConditionalOnClass({Reflections.class, ReferenceAnnotationBeanPostProcessor.class})
@Log4j2
public class DDDEntityScan implements CommandLineRunner {


    private String[] packageNames = {"com.yue.chip"};
    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("开始扫描DDDEntity");
            for (String packageName : packageNames) {
                ReferenceAnnotationBeanPostProcessor processor = new ReferenceAnnotationBeanPostProcessor();
                processor.setApplicationContext(SpringContextUtil.getApplicationContext());
//                Reflections reflections = new Reflections(packageName,new TypeAnnotationsScanner());
                Reflections reflections = new Reflections(
                        new ConfigurationBuilder()
                                .setScanners(
                                        new TypeAnnotationsScanner(), // 设置Annotation的Scanner.
                                        new SubTypesScanner(false) // 设置扫描子类型的scanner.
                                )
                                .setUrls(ClasspathHelper.forPackage(packageName)) // 设置需要扫描的包，虽然指定了包路径，但是其实还是扫描整个root路径.
                                .filterInputsBy(new FilterBuilder().includePackage(packageName)) // 因为上面的原因，所以这里加上了inputs过滤器
                );
                Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(YueChipDDDEntity.class);
                allClasses.forEach(t -> {
                    try {
                        Field[] fields = ReflectUtil.getFieldsDirectly(t, false);
                        for (Field field : fields) {
                            Resource resource = field.getAnnotation(Resource.class);
                            Autowired autowired = field.getAnnotation(Autowired.class);
                            DubboReference dubboReference = field.getAnnotation(DubboReference.class);
                            if (Objects.nonNull(resource) || Objects.nonNull(autowired)) {
                                setFieldValue(t, field);
                            } else if (Objects.nonNull(dubboReference)) {
                                try {
                                    SpringContextUtil.getBean(field.getType());
                                } catch (Exception e) {
                                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(dubboReference);
                                    Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
                                    declaredField.setAccessible(true);
                                    Map<String, Object> attributes = (Map<String, Object>) declaredField.get(invocationHandler);
                                    processor.registerReferenceBean(field.getType().getSimpleName(), field.getType(), attributes, field);
                                }
                                setFieldValue(t, field);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void setFieldValue(Class<?> classz,Field field){
        Object obj = SpringContextUtil.getBean(field.getType());
        if (Objects.nonNull(obj)) {
            log.info("注入"+classz.getSimpleName()+"---"+field.getName());
            ReflectUtil.setFieldValue(classz,field,obj);
        }
    }
}
