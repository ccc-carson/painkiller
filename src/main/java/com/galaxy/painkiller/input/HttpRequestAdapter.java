package com.galaxy.painkiller.input;


import com.galaxy.painkiller.model.Action;
import com.galaxy.painkiller.model.RequestStats;
import com.galaxy.painkiller.model.StressTestRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HttpRequestAdapter implements Adapter<StressTestRequest> {

    @Override
    public Action adapt(StressTestRequest request) {

        Action action = new Action();

        HttpUriRequest httpUriRequest;
        if(request.getMethod().equals("GET")){
            HttpGet httpGet = new HttpGet(request.getUri());
            httpUriRequest = httpGet;
        }else {
            HttpPost httpPost = new HttpPost(request.getUri());
            httpPost.setEntity(new StringEntity(request.getHttpBody(), StandardCharsets.UTF_8));
            httpUriRequest = httpPost;
        }

        action.setHttpUriRequest(httpUriRequest);
        action.setExpectQps(request.getExpectQps());
        action.setThreads(request.getThreads());
        action.setRequestCount(request.getRequestCount());
        action.setStats(new RequestStats());
        action.setSampleResponse(new ArrayList<>());
        return action;
    }
}
