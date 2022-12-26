package com.yue.chip.core.controller.impl;


import com.yue.chip.core.controller.BaseController;
import com.yue.chip.utils.CurrentUserUtil;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-01-28 10:45
 */
public abstract class BaseControllerImpl implements BaseController {

    /**
     * 获取当前登陆用户
     * @return
     */
    public Object getCurrentUser(){
        return CurrentUserUtil.getCurrentUser();
    }

}
