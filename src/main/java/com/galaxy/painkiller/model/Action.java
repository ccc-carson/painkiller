package com.galaxy.painkiller.model;

import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;

@Data
public class Action {


    /**
     * http请求
     */
    private HttpUriRequest httpUriRequest;

    /**
     * http请求返回抽样
     */
    private List<CloseableHttpResponse> sampleResponse;


    /**
     * 期望qps
     */
    private long expectQps;


    /**
     * 线程数
     */
    private Integer threads;


    /**
     * 请求数
     */
    private long requestCount;


    /**
     * 耗时统计
     */
    private RequestStats stats;


}
