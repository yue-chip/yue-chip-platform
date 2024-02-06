package com.yue.chip.authorization;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.Liu
 * @date 2023/2/18 下午5:25
 */
@Data
@Builder
public class AccessToken {

    private String access_token;
    private String refresh_token;
    private String toke_type;
    private long expires_in;
}
