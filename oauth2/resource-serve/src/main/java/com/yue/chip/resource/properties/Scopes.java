package com.yue.chip.resource.properties;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-03 14:57
 **/
@Data
public class Scopes {
    private String scope;
    private List<String> url;
}
