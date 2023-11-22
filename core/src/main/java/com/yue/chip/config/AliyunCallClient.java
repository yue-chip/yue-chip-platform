package com.yue.chip.config;

import com.aliyun.dyvmsapi20170525.Client;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponseBody;
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
@ConditionalOnProperty(prefix = "call",name = "provider",havingValue = "aliyun")
@ConditionalOnClass( {Client.class} )
public class AliyunCallClient {

    @Value("${call.accessKey}")
    private String accessKey;

    @Value("${call.accessKeySecret}")
    private String accessKeySecret;

    @Bean
    public Client createCallClient()
            throws Exception {
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户
        // 此处以把AccessKey 和 AccessKeySecret 保存在环境变量为例说明。您也可以根据业务需要，保存到配置文件里
        // 强烈建议不要把 AccessKey 和 AccessKeySecret 保存到代码里，会存在密钥泄漏风险
        Config config = new Config()
                .setAccessKeyId(accessKey)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dyvmsapi.aliyuncs.com";
        return new Client(config);
    }

//    public static void main(String[] args_) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
//        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
//        Config config = new Config()
//                .setAccessKeyId("")
//                .setAccessKeySecret("");
//        // 访问的域名
//        config.endpoint = "dyvmsapi.aliyuncs.com";
//        Client client = new Client(config);
//        SingleCallByTtsRequest singleCallByTtsRequest = new SingleCallByTtsRequest()
//                .setCalledNumber("")
//                .setTtsCode("")
//                .setOutId("")
//                .setTtsParam("{\"name\":\"测试烟感\",\"addres\":\"奥圆804\"}")
//                .setPlayTimes(5)
//                .setVolume(100);
//        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
//        try {
//            // 复制代码运行请自行打印 API 的返回值
//            SingleCallByTtsResponse singleCallByTtsResponse = client.singleCallByTtsWithOptions(singleCallByTtsRequest, runtime);
//            SingleCallByTtsResponseBody body = singleCallByTtsResponse.getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
