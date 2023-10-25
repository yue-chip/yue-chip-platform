package com.yue.chip.dubbo.cluster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YueChipLoadBalanceMetadata {

    @Value("${dubbo.developmentIpRange:''}")
    private String developmentIpRange;
    public String getDevelopmentIpRange() {
        return developmentIpRange;
    }
}
