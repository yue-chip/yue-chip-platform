package com.yue.chip.core.tenant.mybatis;

import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.yue.chip.utils.ConnectionSwitchoverUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/20 下午2:24
 */
@Component
@ConditionalOnProperty(prefix = "spring",name = "mybatis.multiTenant",havingValue = "enabled")
public class MultiTenantInterceptor extends JsqlParserSupport implements InnerInterceptor {

    @Override
    public void beforePrepare(StatementHandler sh, Connection conn, Integer transactionTimeout) {
        ConnectionSwitchoverUtil.switchoverDatabase(conn);
    }

}
