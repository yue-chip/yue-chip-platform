package com.yue.chip.utils.id;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Assigned;

import java.io.Serializable;

/**
 * @description: 主键ID生成器
 * @author: Mr.Liu
 * @create: 2020-01-04 10:44
 */
public class YueChipIdGenerator extends Assigned {
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        Object id = SnowflakeUtil.getId();
        return (Serializable) id;
    }
}
