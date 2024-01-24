package com.yue.chip.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author coby
 * @description: TODO
 * @date 2024/1/23 上午11:12
 */
public class MongoDatabaseUtil {

    private static volatile MongoTemplate mongoTemplate;

    private static volatile ObjectMapper objectMapper;

    private static volatile String DB_NAME = "";

    private static Logger logger = LoggerFactory.getLogger(MongoDatabaseUtil.class);



    /**
     * 获取多租户数据库
     * @return
     */
    public static MongoDatabase getTenantMongoDatabase(String database){
        MongoDatabase mongoDatabase = getMongoTemplate().getMongoDatabaseFactory().getMongoDatabase(getTenantDbName(database));
        return mongoDatabase;
    }

    public static Document getDocument(Object object) {
        try {
            Document document = Document.parse(objectMapper().writeValueAsString(object));
            document.remove("_id");
            return document;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.info("bean转mongo document失败");
        }
        return null;
    }

    public static String getTenantDbName() {
        if (!StringUtils.hasText(DB_NAME)) {
            synchronized (MongoDatabaseUtil.class) {
                String mongoUri = ((Environment)SpringContextUtil.getBean(Environment.class)).getProperty("spring.data.mongodb.uri");
                if (StringUtils.hasText(mongoUri)) {
                    if (mongoUri.indexOf("?")>-1) {
                        DB_NAME = mongoUri.substring(mongoUri.lastIndexOf("/") + 1, mongoUri.indexOf("?"));
                    }else {
                        DB_NAME = mongoUri.substring(mongoUri.lastIndexOf("/") + 1);
                    }
                }
                if (!StringUtils.hasText(DB_NAME)) {
                    DB_NAME = ((Environment)SpringContextUtil.getBean(Environment.class)).getProperty("spring.data.mongodb.database");
                }
            }
        }
        return getTenantDbName(DB_NAME);
    }
    public static String getTenantDbName(String database) {
        Long tenantNumber = CurrentUserUtil.getCurrentUserTenantNumber();
        return TenantDatabaseUtil.tenantDatabaseName(database,tenantNumber);
    }

    private static MongoTemplate getMongoTemplate(){
        if(Objects.isNull(mongoTemplate)){
            synchronized(MongoDatabaseUtil.class){
                if(Objects.isNull(mongoTemplate)){
                    mongoTemplate = (MongoTemplate) SpringContextUtil.getBean(MongoTemplate.class);
                }
            }
        }
        return mongoTemplate;
    }

    private static ObjectMapper objectMapper() {
        if(Objects.isNull(objectMapper)) {
            synchronized (MongoDatabaseUtil.class) {
                objectMapper = new ObjectMapper();
                JavaTimeModule javaTimeModule = new JavaTimeModule();
                javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
                javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
                objectMapper.registerModule(javaTimeModule);
            }
        }
        return objectMapper;
    }
}
