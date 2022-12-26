package com.yue.chip.core.persistence.curd;

import com.yue.chip.core.persistence.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Mr.Liu
 * @time: 2021/5/26 上午11:27
 */
@Data
public class MoreEntity implements Serializable {

    private BaseEntity entity1;

    private BaseEntity entity2;

    private BaseEntity entity3;

    private BaseEntity entity4;

    private BaseEntity entity5;

    private BaseEntity entity6;

    public MoreEntity() {
    }

    public MoreEntity(BaseEntity entity1) {
        this.entity1 = entity1;
    }

    public MoreEntity(BaseEntity entity1, BaseEntity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public MoreEntity(BaseEntity entity1, BaseEntity entity2, BaseEntity entity3) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.entity3 = entity3;
    }

    public MoreEntity(BaseEntity entity1, BaseEntity entity2, BaseEntity entity3, BaseEntity entity4) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.entity3 = entity3;
        this.entity4 = entity4;
    }

    public MoreEntity(BaseEntity entity1, BaseEntity entity2, BaseEntity entity3, BaseEntity entity4, BaseEntity entity5) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.entity3 = entity3;
        this.entity4 = entity4;
        this.entity5 = entity5;
    }

    public MoreEntity(BaseEntity entity1, BaseEntity entity2, BaseEntity entity3, BaseEntity entity4, BaseEntity entity5, BaseEntity entity6) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.entity3 = entity3;
        this.entity4 = entity4;
        this.entity5 = entity5;
        this.entity6 = entity6;
    }
}
