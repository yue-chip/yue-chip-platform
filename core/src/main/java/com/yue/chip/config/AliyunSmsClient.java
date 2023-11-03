package com.yue.chip.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/11/1 下午2:55
 */

@Configuration
@ConditionalOnProperty(prefix = "sms",name = "provider",havingValue = "aliyun")
@ConditionalOnClass( {Client.class} )
public class AliyunSmsClient {

    @Value("${sms.accessKey}")
    private String accessKey;

    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;

    @Bean
    public Client createSmsClient()
            throws Exception {
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户
        // 此处以把AccessKey 和 AccessKeySecret 保存在环境变量为例说明。您也可以根据业务需要，保存到配置文件里
        // 强烈建议不要把 AccessKey 和 AccessKeySecret 保存到代码里，会存在密钥泄漏风险
        Config config = new Config()
                .setAccessKeyId(accessKey)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

}
