//package com.yue.chip.dubbo.filter;
//
//import com.yue.chip.dubbo.util.ObjectMapperCodec;
//import org.apache.dubbo.common.extension.Activate;
//import org.apache.dubbo.rpc.*;
//import org.apache.dubbo.spring.security.utils.SecurityNames;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import static org.apache.dubbo.spring.security.utils.SecurityNames.SECURITY_CONTEXT_HOLDER_CLASS_NAME;
//
///**
// * @author Mr.Liu
// * @date 2023/2/20 下午3:07
// */
////@Activate(group = CommonConstants.CONSUMER, order = -10000,onClass = SECURITY_CONTEXT_HOLDER_CLASS_NAME)
//@Activate(order = -10000,onClass = SECURITY_CONTEXT_HOLDER_CLASS_NAME)
//public class YueChipContextHolderAuthenticationPrepareFilter implements Filter {
//    private ObjectMapperCodec mapper = new ObjectMapperCodec();
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        setSecurityContext(invocation);
//
//        return invoker.invoke(invocation);
//    }
//
//    private void setSecurityContext(Invocation invocation) {
//        SecurityContext context = SecurityContextHolder.getContext();
//
//        Authentication authentication = context.getAuthentication();
//
//        invocation.setObjectAttachment(SecurityNames.SECURITY_AUTHENTICATION_CONTEXT_KEY, mapper.serialize(authentication.getPrincipal()));
//    }
//}
