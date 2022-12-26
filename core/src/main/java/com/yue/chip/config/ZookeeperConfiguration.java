package com.yue.chip.config;

//import org.apache.curator.CuratorZookeeperClient;
//import org.apache.curator.RetryPolicy;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @description: zookeeper
// * @author: mr.liu
// * @create: 2020-10-15 14:05
// **/
//@Configuration
//@ConditionalOnClass( {CuratorZookeeperClient.class} )
//@ConditionalOnProperty(prefix = "zookeeper",name = "enable",havingValue = "true")
//@ConditionalOnExpression("'${zookeeper.server}'!=''")
//public class ZookeeperConfiguration {
//
//    @Value("${zookeeper.server}")
//    private String server;
//
//    @Bean
//    public CuratorFramework curatorFramework(){
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
//        CuratorFramework  client = CuratorFrameworkFactory.newClient(server,retryPolicy);
//        client.start();
//        return client;
//    }
//}