package com.yue.chip.core.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yue.chip.core.common.enums.Delete;
import com.yue.chip.core.persistence.JpaInterceptor;
import com.yue.chip.core.persistence.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@EntityListeners({AuditingEntityListener.class, JpaInterceptor.class})
@JsonIgnoreProperties(ignoreUnknown = true,value = {"isDelete","createUserId","updateUserId","tenantId"})
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -90000050L;

    @NotNull(message="ID不能为空",groups= {Validator.Update.class, Validator.Delete.class})
    @Schema(description = "ID")
    private Long id;

    @JsonIgnore
    @Builder.Default
    private Delete isDelete = Delete.FALSE;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createDateTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后修改时间")
    private LocalDateTime updateDateTime;

    private Long createUserId;

    private Long updateUserId;

    @NotNull(message="版本号不能为空",groups= {Validator.Update.class})
    @Schema(description = "版本号（修改需要传version,新增不需要传）")
    @Builder.Default
    private Long version = 0L;

    @Schema(description = "租户id")
    @JsonIgnore
    @Builder.Default
    private Long tenantId = 10000L;

    public BaseEntity() {
    }

    @Id()
    @GenericGenerator(name="assigned", strategy="assigned")
    @GeneratedValue(generator="assigned")
    public Long getId() {
        return id;
    }

    @Convert(converter = Delete.DeleteConverter.class)
    public Delete getIsDelete() {
        return isDelete;
    }

    @CreatedDate
    @Column( updatable = false)
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    @LastModifiedDate
    @Column(insertable = false)
    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    @CreatedBy
    @Column( updatable = false)
    public Long getCreateUserId() {
        return createUserId;
    }

    @LastModifiedBy
    @Column(insertable = false)
    public Long getUpdateUserId() {
        return updateUserId;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    @Column(updatable = false)
    public Long getTenantId() {
        return tenantId;
    }
}
