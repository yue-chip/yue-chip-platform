package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yue.chip.constant.ResultDataConstant;
import com.yue.chip.core.common.enums.ResultDataState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

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
public class PageResultData<T> extends PageImpl<T> implements IPageResultData<T>, Serializable {

    private static final long serialVersionUID = 8078379219201834984L;

    @Schema(description = "返回消息", type="string")
    private String message = ResultDataConstant.SUCCEED_MESSAGE;

    @Schema(description = "异常信息", type="string")
    private String exceptionMessage;

    @Schema(description = "状态编码", type="integer")
    private Integer status = ResultDataState.SUCCESS.getKey();

    @Schema(description = "结果集", type="object")
    private T data;

    public PageResultData(List content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResultData(List content) {
        super(content);
    }

    public PageResultData(){
        super(Collections.EMPTY_LIST, new LionPage(),10L);
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
        return new PageResultData(page.getContent(),new LionPage(page.getPageable().getPageNumber(),page.getPageable().getPageSize()),page.getTotalElements());
    }


}
