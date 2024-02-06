package com.yue.chip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:30
 */
@Data
@Schema
@NoArgsConstructor
public class RegisteredClientAddDTO {

    @NotBlank(message = "客户端id不能为空")
    @Schema(description = "客户端id")
    private String clientId;

    @NotBlank(message = "客户端密钥不能为空")
    @Schema(description = "客户端密钥")
    private String clientSecret;

    @Schema(description = "客户端密钥过期时间")
    @NotNull(message = "客户端密钥过期时间不能为空")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientSecretExpiresAt;

    @NotBlank(message = "客户端名称不能为空")
    @Schema(description = "客户端名称")
    private String clientName;
}
