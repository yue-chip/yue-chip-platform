package com.yue.chip.config;

import cn.hutool.crypto.SecureUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description: 密码工具自动装配Configurer
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@Configuration
@ConditionalOnClass ( {PasswordEncoderFactories.class} )
@ConditionalOnMissingBean({PasswordEncoder.class})
public class PasswordConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder () {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }

    public static void main(String args[]) {
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(SecureUtil.md5("admin")));
    }
}
