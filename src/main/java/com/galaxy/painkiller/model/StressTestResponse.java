package com.galaxy.painkiller.model;

import lombok.Data;

import java.util.List;

@Data
public class StressTestResponse {

    /**
     * 总请求数
     */
    private Long totalCount;

    /**
     * 请求成功次数
     */
    private Long successCount;

    /**
     * 请求失败次数
     */
    private Long failedCount;

    /**
     * 总耗时
     */
    private Long timeTaken;

    /**
     * 实际QPS
     */
    private Long actualQps;

    /**
     * 最大耗时
     */
    private Long maxMills;

    /**
     * 最小耗时
     */
    private Long minMills;

    /**
     * 平均耗时
     */
    private Long averageMills;

    /**
     * 统计字符串
     */
    private String stats;

    /**
     * 响应抽样
     */
    private List<String> sampleResponse;


}
