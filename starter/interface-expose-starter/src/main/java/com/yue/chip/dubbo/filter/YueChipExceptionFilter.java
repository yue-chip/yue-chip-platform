package com.yue.chip.dubbo.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @description: 自定义dubbo异常返回（暂未做特殊处理，此类可以删除）
 * @author: Mr.Liu
 * @create: 2020-02-14 20:51
 */
@Activate
public class YueChipExceptionFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }
}
