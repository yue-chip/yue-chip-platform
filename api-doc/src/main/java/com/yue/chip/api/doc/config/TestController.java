package com.yue.chip.api.doc.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.Liu
 * @date 2023/7/7 上午9:30
 */
@RestController
@Tag(name = "测试")
public class TestController {
    @PostMapping("/test")
    @Operation(description = "上传文件(支持多文件)",summary = "上传文件(支持多文件)")
    public void upload()  {

    }
}
