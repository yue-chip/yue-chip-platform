package com.yue.chip.core;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-16 15:10
 */
public interface IResultData<T> {

    @Schema(description = "提示信息")
    public String getMessage();

    @Schema(description = "-100:uri无效,403:登录失败,404:找不到服务,-200:错误,-300:请求拒绝,200:成功,401:无权限")
    public Integer getStatus();

    @Schema(description = "异常信息")
    public String getExceptionMessage();

    @Schema(description = "返回的数据")
    public T getData();
}