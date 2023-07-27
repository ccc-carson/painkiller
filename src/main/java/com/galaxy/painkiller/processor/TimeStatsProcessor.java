package com.galaxy.painkiller.processor;

import com.galaxy.painkiller.model.Action;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TimeStatsProcessor implements Processor<Action>{

    @Override
    public void process(Action action) {

        long currentMills = System.currentTimeMillis();
        long totalRequests = action.getStats().getTotalCount().get();
        long totalMillis = currentMills - action.getStats().getStartMillis();
        long curMillis = currentMills - action.getStats().getPreviousMillis();
        long currentRequests = totalRequests - action.getStats().getPreviousTotal();
        totalMillis = Math.max(totalMillis, 1);
        curMillis = Math.max(curMillis, 1);
        action.getStats().setPreviousTotal(totalRequests);
        action.getStats().setPreviousMillis(currentMills);

        try {
            long totalQPS = totalRequests / (totalMillis / 1000);
            long currentQPS = currentRequests / (curMillis / 1000);
            action.getStats().setActualQps(totalQPS);

            System.out.println("总QPS: " + totalQPS
                    + " 当前QPS: " + currentQPS
                    + " 总请求数: " + totalRequests + "\n"
                    + " 成功请求数: " + action.getStats().getSuccessCount().get() + "\n"
                    + " 失败请求数: " + action.getStats().getFailedCount().get() + "\n"
            );
            metrics(action);
            for (CloseableHttpResponse response : action.getSampleResponse()) {
                try {
                    String s = EntityUtils.toString(response.getEntity());
                    System.out.println(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (ArithmeticException e) {
            System.out.println("出了点问题");
        }
    }

    /**
     * 计算平均耗时、最大耗时、最小耗时
     * @param action
     */
    private void metrics(Action action){

        long totalTime = 0L;
        long totalRequest = 0L;

        long maxMills = 0L;
        long minMills = 0L;
        Map<Long, AtomicLong> responseTimes =  action.getStats().getTimeMap();
        for (Map.Entry<Long, AtomicLong> entry : responseTimes.entrySet()) {
            Long responseTime = entry.getKey();
            Long requests = entry.getValue().get();

            maxMills = Math.max(responseTime,maxMills);
            minMills = Math.min(responseTime,minMills);

            totalTime += responseTime * requests;
            totalRequest += requests;
        }
        long average = totalTime / totalRequest;
        action.getStats().setAverageMills(average);
        action.getStats().setMaxMills(maxMills);
        action.getStats().setMinMills(minMills);
    }

}
