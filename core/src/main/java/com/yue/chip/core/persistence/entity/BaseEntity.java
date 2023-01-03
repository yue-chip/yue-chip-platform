package com.yue.chip.core.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yue.chip.core.common.enums.Delete;
import com.yue.chip.core.persistence.JpaInterceptor;
import com.yue.chip.core.persistence.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@MappedSuperclass
@Data
//@DynamicInsert
//@DynamicUpdate
//@SelectBeforeUpdate
@EntityListeners({AuditingEntityListener.class, JpaInterceptor.class})
@JsonIgnoreProperties(ignoreUnknown = true,value = {"isDelete","createDateTime","updateDateTime","createUserId","updateUserId","tenantId"})
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -90000050L;

    @Id()
//    @GeneratedValue(generator = "snow_flake_id")
//    @GenericGenerator(name = "snow_flake_id", strategy = "com.lion.utils.id.LionIdGenerator")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name="assigned", strategy="assigned")
    @GeneratedValue(generator="assigned")
    @Column(name = "id")
    @NotNull(message="ID不能为空",groups= {Validator.Update.class, Validator.Delete.class})
    @Schema(description = "ID")
    protected Long id;

    @JsonIgnore
    @Column(name = "is_delete")
    @Convert(converter = Delete.DeleteConverter.class)
    private Delete isDelete = Delete.FALSE;

    @CreatedDate
    @Column(name = "create_date_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createDateTime;

    @LastModifiedDate
    @Column(name = "update_date_time", insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateDateTime;

    @CreatedBy
    @Column(name = "create_user_id", updatable = false)
    protected Long createUserId;

    @LastModifiedBy
    @Column(name = "update_user_id", insertable = false)
    protected Long updateUserId;


    @Column(name = "create_user_name", updatable = false)
    protected String createUserName;

    @Column(name = "update_user_name", insertable = false)
    protected String updateUserName;


    @NotNull(message="版本号不能为空",groups= {Validator.Update.class})
    @Schema(description = "版本号（修改需要传version,新增不需要传）")
    @Version
    protected Long version = 0L;

    @Schema(description = "租户id")
    @JsonIgnore
    @Column(name = "tenant_id", updatable = false)
    protected Long tenantId;
}
