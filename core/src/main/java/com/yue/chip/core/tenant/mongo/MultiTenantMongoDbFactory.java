package com.yue.chip.core.tenant.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.yue.chip.utils.MongoDatabaseUtil;
import com.yue.chip.utils.SpringContextUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.util.StringUtils;

/**
 * @author coby
 * @description: TODO
 * @date 2024/1/24 下午2:34
 */
public class MultiTenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {

    public MultiTenantMongoDbFactory(){
        super(((Environment) SpringContextUtil.getBean(Environment.class)).getProperty("spring.data.mongodb.uri"));
    }
    public MultiTenantMongoDbFactory(String connectionString) {
        super(connectionString);
    }

    public MultiTenantMongoDbFactory(ConnectionString connectionString) {
        super(connectionString);
    }

    public MultiTenantMongoDbFactory(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    @Override
    protected MongoDatabase doGetMongoDatabase(String dbName) {
        return super.doGetMongoDatabase(StringUtils.hasText(dbName)?MongoDatabaseUtil.getTenantDbName(dbName): MongoDatabaseUtil.getTenantDbName());
    }
}
