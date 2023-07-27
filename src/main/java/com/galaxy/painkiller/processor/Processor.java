package com.galaxy.painkiller.processor;

import com.galaxy.painkiller.model.StressTestRequest;

public interface Processor<R> {

    void process(R action);

}
