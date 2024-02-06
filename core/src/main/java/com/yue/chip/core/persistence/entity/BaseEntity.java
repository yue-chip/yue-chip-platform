package com.yue.chip.core.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yue.chip.core.persistence.JpaInterceptor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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

    //@Schema(description = "ID")
    private Long id;

//    @JsonIgnore
//    @Builder.Default
//    private Delete isDelete = Delete.FALSE;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Schema(description = "创建时间")
    private LocalDateTime createDateTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Schema(description = "最后修改时间")
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
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

//    @Convert(converter = Delete.DeleteConverter.class)
//    public Delete getIsDelete() {
//        return isDelete;
//    }

    @CreatedDate
    @Column(name = "CREATE_DATE_TIME", updatable = false)
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    @LastModifiedDate
    @Column(name = "UPDATE_DATE_TIME",insertable = false)
    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    @CreatedBy
    @Column( updatable = false,name= "CREATE_USER_ID")
    public Long getCreateUserId() {
        return createUserId;
    }

    @LastModifiedBy
    @Column(insertable = false,name = "UPDATE_USER_ID")
    public Long getUpdateUserId() {
        return updateUserId;
    }

//    @Version
//    public Long getVersion() {
//        return version;
//    }

}
