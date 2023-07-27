package com.galaxy.painkiller.processor;

import com.galaxy.painkiller.model.Action;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class RequestProcessor implements Processor<Action>{

    private  CloseableHttpClient httpClient;

    @Override
    public void process(Action action) {
        final RateLimiter rateLimiter = RateLimiter.create(action.getExpectQps());
        final ExecutorService worker = Executors.newFixedThreadPool(action.getThreads());

        long start = System.currentTimeMillis();
        action.getStats().setStartMillis(start);
        rateLimiter.acquire();
        for (int i = 0; i < action.getRequestCount(); i++) {
            worker.submit(() -> {
                try {
                    CloseableHttpResponse response = httpClient.execute(action.getHttpUriRequest());
                    long ts = System.currentTimeMillis() - start;
                    collectTimeCount(action.getStats().getTimeMap(), ts);
                    if(response.getStatusLine().getStatusCode() == 200){
                        action.getStats().getSuccessCount().incrementAndGet();
                        //响应成功抽样
                        if(ThreadLocalRandom.current().nextDouble() < 0.01){
                            action.getSampleResponse().add(response);
                        }
                    }
                } catch (Exception e) {
                    action.getStats().getFailedCount().incrementAndGet();
                }finally {
                    action.getStats().getTotalCount().incrementAndGet();
                }
            });
        }

        worker.shutdown();
        try {
            worker.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            //
        }
        long end = System.currentTimeMillis();
        action.getStats().setTotalMills(end-start);
    }

    private void collectTimeCount(Map<Long, AtomicLong> timeMap, Long ts){
        if(timeMap.containsKey(ts)){
            timeMap.get(ts).getAndIncrement();
        }else{
            timeMap.computeIfAbsent(ts,k -> new AtomicLong()).getAndIncrement();
        }
    }

    public void setHttpClient(CloseableHttpClient httpClient){
        this.httpClient = httpClient;
    }

}
