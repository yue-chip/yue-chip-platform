package com.yue.chip.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Liu
 * @description $
 * @createDateTime 2021/9/3 下午8:50
 */
@Configuration
@ConditionalOnClass({ModelMapper.class})
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
