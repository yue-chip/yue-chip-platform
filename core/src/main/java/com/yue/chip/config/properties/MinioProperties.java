package com.yue.chip.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-08 14:54
 **/
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties  {

    public static final String PUBLIC_BUCKET ="public";

    /**
     * minio服务器
     */
    private String url;

    /**
     *
     */
    private String accessKey;

    /**
     *
     */
    private String secretKey;

    /**
     * 存储路径
     */
    private String path;
}
