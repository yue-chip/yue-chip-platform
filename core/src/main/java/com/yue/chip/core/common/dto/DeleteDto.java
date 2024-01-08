package com.yue.chip.core.common.dto;

//import io.swagger.v3.oas.annotations.media.Schem
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Mr.Liu
 * @Description:
 * @date 2021/3/23上午11:37
 */
@Data
//@Schema
public class DeleteDto {
    //@Schema(description = "id")
    @NotNull(message = "id不能未空")
    private Long id;
}
