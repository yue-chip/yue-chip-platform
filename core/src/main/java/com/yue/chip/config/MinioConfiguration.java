package com.yue.chip.config;

import com.yue.chip.config.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-08 14:55
 **/
@Configuration
@ConditionalOnProperty(prefix = "file",name = "store.type",havingValue = "minio")
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnClass({MinioClient.class})
public class MinioConfiguration {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws IOException, InvalidKeyException, InvalidResponseException,  InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        MinioClient minioClient = MinioClient.builder()
                        .endpoint(minioProperties.getUrl())
                        .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                        .build();
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MinioProperties.PUBLIC_BUCKET).build());
        if(!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(MinioProperties.PUBLIC_BUCKET).build());
        }
        return minioClient;
    }
}
