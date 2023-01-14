package com.yue.chip.core;

import com.yue.chip.core.persistence.entity.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Mr.Liu
 * @date 2023/1/10 下午3:03
 * @description BaseDefinition
 */
@Data
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
public abstract class BaseDefinition extends BaseEntity {
    public BaseDefinition() {
    }
}
