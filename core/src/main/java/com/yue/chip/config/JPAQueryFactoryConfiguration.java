package com.yue.chip.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Liu
 * @description $
 * @createDateTime 2021/9/5 下午2:22
 */
@Configuration
@ConditionalOnClass({JPAQueryFactory.class})
public class JPAQueryFactoryConfiguration {

//    @Bean
//    @Autowired
//    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
//        return new JPAQueryFactory(entityManager);
//    }
}
