package com.yue.chip.core.persistence;

import lombok.SneakyThrows;
import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * @description: jpa 拦截器
 * @author: mr.liu
 * @create: 2020-10-20 16:04
 **/
public class JpaSqlInterceptor implements StatementInspector {

    @SneakyThrows
    @Override
    public String inspect( String sql) {
        return sql;
    }

}
