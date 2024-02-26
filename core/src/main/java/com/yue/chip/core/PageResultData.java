package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yue.chip.constant.ResultDataConstant;
import com.yue.chip.core.common.enums.ResultDataState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
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
public class PageResultData extends PageImpl implements IPageResultData, Serializable {

    private static final long serialVersionUID = 8078379219201834984L;

    @Builder.Default
    private String message = ResultDataConstant.SUCCEED_MESSAGE;
    private String exceptionMessage;
    @Builder.Default
    private Integer status = ResultDataState.SUCCESS.getKey();
    private Object data;
    private String traceId;

    public String getTraceId() {
        return TraceContext.traceId();
    }

    public PageResultData(List content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.status = ResultDataState.SUCCESS.getKey();
        this.message = ResultDataConstant.SUCCEED_MESSAGE;
    }

    @Deprecated
    public PageResultData(String message, String exceptionMessage, Integer status,Object data,String traceId) {
        super(Collections.EMPTY_LIST, new YueChipPage(),10L);
        this.message = message;
        this.exceptionMessage = exceptionMessage;
        this.status = status;
        this.data = data;
        this.traceId = traceId;
    }

    public PageResultData(List content) {
        super(content);
    }

    public PageResultData(){
        super(Collections.EMPTY_LIST, new YueChipPage(),10L);
    }

    @JsonGetter
    public Object getData() {
        if (Objects.nonNull(data)) {
            return this.data;
        }
        return this.getContent();
    }

    public PageResultData setData(Object data) {
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
