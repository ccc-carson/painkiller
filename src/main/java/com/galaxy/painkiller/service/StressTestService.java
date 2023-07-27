package com.galaxy.painkiller.service;

import com.galaxy.painkiller.input.Adapter;
import com.galaxy.painkiller.model.Action;
import com.galaxy.painkiller.model.StressTestRequest;
import com.galaxy.painkiller.model.StressTestResponse;
import com.galaxy.painkiller.output.Renderer;
import com.galaxy.painkiller.processor.Processor;

public class StressTestService {

    /**
     * 处理器
     */
    private Processor<Action>[] processors;

    /**
     * 适配器
     */
    private Adapter<StressTestRequest> httpRequestAdapter;

    /**
     * 渲染器
     */
    private Renderer<StressTestResponse, Action> renderer;


    public StressTestResponse run(StressTestRequest stressTestRequest){

        Action action = httpRequestAdapter.adapt(stressTestRequest);

        for (Processor<Action> processor : processors) {
            processor.process(action);
        }
        return renderer.render(action);
    }

    public void setRequestProcessors(Processor<Action>[] requestProcessors) {
        this.processors = requestProcessors;
    }

    public void setHttpRequestAdapter(Adapter<StressTestRequest> httpRequestAdapter) {
        this.httpRequestAdapter = httpRequestAdapter;
    }

    public void setRenderer(Renderer<StressTestResponse, Action> renderer) {
        this.renderer = renderer;
    }
}
