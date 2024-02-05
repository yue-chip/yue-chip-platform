package com.yue.chip.authorization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:30
 */
@Data
public class RegisteredClientAddDTO {

    @NotBlank(message = "客户端id不能为空")
    @Schema(description = "客户端id")
    private String clientId;

    @NotBlank(message = "客户端密钥不能为空")
    @Schema(description = "客户端密钥")
    private String clientSecret;

    @NotBlank(message = "客户端密钥过期时间不能为空")
    @Schema(description = "客户端密钥过期时间")
    private LocalDateTime clientSecretExpiresAt;

    @NotBlank(message = "客户端名称不能为空")
    @Schema(description = "客户端名称")
    private String clientName;
}
