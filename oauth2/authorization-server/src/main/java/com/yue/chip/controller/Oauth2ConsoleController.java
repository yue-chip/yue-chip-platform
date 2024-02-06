package com.yue.chip.controller;

import com.yue.chip.dto.RegisteredClientAddDTO;
import com.yue.chip.service.RegisteredClientService;
import com.yue.chip.core.IResultData;
import com.yue.chip.core.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 上午11:07
 */
@RestController()
@RequestMapping("/oauth2")
@Validated
@Tag(name = "oauth2-pc端后台")
@Slf4j
public class Oauth2ConsoleController {

    @Resource
    private RegisteredClientService registeredClientService;

    @Operation(description = "新建oauth2鉴权信息",summary = "新建oauth2鉴权信息")
    @PostMapping("/add")
    public IResultData add(@RequestBody @Validated RegisteredClientAddDTO registeredClientAddDTO){
        registeredClientService.add(registeredClientAddDTO);
        return ResultData.builder().build();
    }
}
