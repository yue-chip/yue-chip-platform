package com.yue.chip.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:14
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RegisteredClientVo extends RegisteredClient {
}
