package com.galaxy.painkiller.config;

import com.galaxy.painkiller.input.HttpRequestAdapter;
import com.galaxy.painkiller.model.Action;
import com.galaxy.painkiller.output.StressTestRenderer;
import com.galaxy.painkiller.processor.Processor;
import com.galaxy.painkiller.processor.RequestProcessor;
import com.galaxy.painkiller.processor.TimeStatsProcessor;
import com.galaxy.painkiller.service.StressTestService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PainKillerContext {

    @Bean
    StressTestService stressTestService(){
        StressTestService service = new StressTestService();
        service.setRequestProcessors(painKillerProcessors());
        service.setHttpRequestAdapter(httpRequestAdapter());
        service.setRenderer(stressTestRenderer());
        return service;
    }

    @Bean
    Processor<Action>[] painKillerProcessors(){
        List<Processor> processors = new ArrayList<>(20);
        processors.add(requestProcessor());
        processors.add(timeStatsProcessor());
        return processors.toArray(new Processor[0]);
    }

    @Bean
    RequestProcessor requestProcessor(){
        RequestProcessor requestProcessor = new RequestProcessor();
        requestProcessor.setHttpClient(httpClient());
        return requestProcessor;
    }

    @Bean
    TimeStatsProcessor timeStatsProcessor(){
        TimeStatsProcessor timeStatsProcessor = new TimeStatsProcessor();
        return timeStatsProcessor;
    }

    @Bean
    HttpRequestAdapter httpRequestAdapter(){
        HttpRequestAdapter adapter = new HttpRequestAdapter();
        return adapter;
    }

    @Bean
    StressTestRenderer stressTestRenderer(){
        StressTestRenderer renderer = new StressTestRenderer();
        return renderer;
    }

    @Bean
    CloseableHttpClient httpClient(){
        CloseableHttpClient httpClient = SingletonHttpClient.getInstance();
        return httpClient;
    }

}
