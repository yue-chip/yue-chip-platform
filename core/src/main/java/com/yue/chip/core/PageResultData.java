package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yue.chip.constant.ResultDataConstant;
import com.yue.chip.core.common.enums.ResultDataState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @description: 分页返回结果集
 * @author: Mr.Liu
 * @create: 2020-01-14 11:04
 */
@JsonIgnoreProperties(ignoreUnknown = true,value = {"content","pageable","sort","numberOfElements","empty","number","size"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper=false)
@Schema()
@Builder
public class PageResultData<T> extends PageImpl<T> implements IPageResultData<T>, Serializable {

    private static final long serialVersionUID = 8078379219201834984L;

    @Builder.Default
    @Schema(description = "返回消息", type="string")
    private String message = ResultDataConstant.SUCCEED_MESSAGE;

    @Schema(description = "异常信息", type="string")
    private String exceptionMessage;

    @Builder.Default
    @Schema(description = "状态编码", type="integer")
    private Integer status = ResultDataState.SUCCESS.getKey();

    @Schema(description = "结果集", type="object")
    private T data;

    public PageResultData(List content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    @Deprecated
    public PageResultData(String message, String exceptionMessage, Integer status,T data) {
        super(Collections.EMPTY_LIST, new YueChipPage(),10L);
        this.message = message;
        this.exceptionMessage = exceptionMessage;
        this.status = status;
        this.data = data;
    }

    public PageResultData(List content) {
        super(content);
    }

    public PageResultData(){
        super(Collections.EMPTY_LIST, new YueChipPage(),10L);
    }

    @JsonGetter
    public T getData() {
        return (T) this.getContent();
    }

    public PageResultData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Integer getPageNumber(){
        return this.getNumber()+1;
    }

    public Integer getPageSize(){
        return this.getSize();
    }

    public static IPageResultData<?> convert(Page page) {
        return convert(page,null);
    }

    public static IPageResultData<?> convert(Page page,List<?> content) {
        return new PageResultData(Objects.isNull(content)?page.getContent():content,new YueChipPage(page.getPageable().getPageNumber(),page.getPageable().getPageSize()),page.getTotalElements());
    }


}
