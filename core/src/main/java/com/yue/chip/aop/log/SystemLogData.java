package com.yue.chip.aop.log;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mr.liu
 * @title: SystemLogData
 * @description: 系统日志
 * @date 2020/8/19上午11:30
 */
@Data
public class SystemLogData implements Serializable {

    private static final long serialVersionUID = -299436691466759722L;

    /**
     * 当前登陆用户ID
     */
    private Long currentUserId;

    /**
     * track id(调用链ID 用于服务调用追踪)
     */
    private Long trackId;

    /**
     * 请求参数(spring mvc && dubbo)
     */
    private Object parameter;

    /**
     * 返回值
     */
    private Object response;

    /**
     * 异常信息
     */
    private String exception;

    /**
     *执行序号
     */
    private int sequenceNumber;

    /**
     * 执行目标
     */
    private String target;

    /**
     * 是否发生异常
     */
    private Boolean isException = false;

    /**
     * 执行时长
     */
    private long executeTime;

    /**
     * 请求时间
     */
    private LocalDateTime dateTime = LocalDateTime.now();

    /**
     * 请求url
     */
    private String uri;

    /**
     * 请求客户端地址
     */
    private String clientIp;

    /**
     * 服务所在的内网ip
     */
    private String intranetIp;

}
