package com.yue.chip.utils;

import com.yue.chip.constant.DubboConstant;
import com.yue.chip.core.ICurrentUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 获取当前登陆用户信息
 * @author: Mr.Liu
 * @create: 2020-02-20 20:03
 */
@Component("currentUser")
@ConditionalOnClass({DubboComponentScan.class})
public class CurrentUser {

    @DubboReference(cluster = DubboConstant.CLUSTER_FAILOVER, retries = DubboConstant.RETRIES)
    private ICurrentUser iCurrentUser;

    public Map<String, Object> findUserToMap(String username) {
        try {
            return iCurrentUser.findUserToMap(username);
        }catch (Exception ex){
            return null;
        }
    }
}