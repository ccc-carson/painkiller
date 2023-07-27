package com.galaxy.painkiller.model;

import lombok.Data;

import java.util.Map;

@Data
public class StressTestRequest {

    /**
     * 请求uri
     */
    private String uri;

    /**
     * 目标接口的方法
     */
    private String method;

    /**
     * 请求头
     */
    private Map<String,String> httpHeader;

    /**
     * 请求体
     */
    private String httpBody;

    /**
     * 预期qps
     */
    private Integer expectQps;

    /**
     * 请求次数
     */
    private Long requestCount;

    /**
     * 线程数 nullable
     */
    private Integer threads;

}
