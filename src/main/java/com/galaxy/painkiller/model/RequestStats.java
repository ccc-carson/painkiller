package com.galaxy.painkiller.model;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class RequestStats {

    private AtomicLong totalCount = new AtomicLong(0);

    private AtomicLong successCount = new AtomicLong(0);

    private AtomicLong failedCount = new AtomicLong(0);

    private ConcurrentHashMap<Long, AtomicLong> timeMap = new ConcurrentHashMap<>(64);

    private long actualQps;

    private long averageMills;

    private long totalMills;

    private long maxMills;

    private long minMills;

    /**
     * 开始时间戳
     */
    private long startMillis;

    /**
     * 前一次请求时间戳
     */
    private long previousMillis;

    /**
     *
     */
    private long previousTotal;
}
