package com.yue.chip.utils.id;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * @description: 主键ID生成器
 * @author: Mr.Liu
 * @create: 2020-01-04 10:44
 */
public class LionIdGenerator extends IdentityGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor s, Object obj) {
        Object id = SnowflakeUtil.getId();
        if (id != null) {
            return (Serializable) id;
        }
        return super.generate(s, obj);
    }
}
