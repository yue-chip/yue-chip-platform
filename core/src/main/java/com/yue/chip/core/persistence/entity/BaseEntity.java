package com.yue.chip.core.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yue.chip.core.persistence.JpaInterceptor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

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
@JsonIgnoreProperties(ignoreUnknown = true,value = {"createDateTime","updateDateTime","createUserId","updateUserId"})
public abstract class BaseEntity implements Serializable,IBaseEntity {

    private static final long serialVersionUID = -90000050L;

    @Schema(description = "ID")
    private Long id;

//    @JsonIgnore
//    @Builder.Default
//    private Delete isDelete = Delete.FALSE;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createDateTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后修改时间")
    private LocalDateTime updateDateTime;

    private Long createUserId;

    private Long updateUserId;

//    @Schema(description = "版本号（修改需要传version,新增不需要传）")
//    @Builder.Default
//    private Long version = 0L;

    public BaseEntity() {
    }

    @Id()
//    @GeneratedValue(generator = "snowFlakeId")
//    @GenericGenerator(name = "snowFlakeId", strategy = "com.yue.chip.utils.id.YueChipIdGenerator")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

//    @Convert(converter = Delete.DeleteConverter.class)
//    public Delete getIsDelete() {
//        return isDelete;
//    }

    @CreatedDate
    @Column(updatable = false,columnDefinition = "datetime")
    @Comment("创建时间")
    @ColumnDefault("NOW()")
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    @LastModifiedDate
    @Column(insertable = false,columnDefinition = "datetime")
    @Comment("修改时间")
    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    @CreatedBy
    @Column(updatable = false)
    @Comment("创建人")
    public Long getCreateUserId() {
        return createUserId;
    }

    @LastModifiedBy
    @Column(insertable = false)
    @Comment("修改人")
    public Long getUpdateUserId() {
        return updateUserId;
    }

//    @Version
//    public Long getVersion() {
//        return version;
//    }

}
