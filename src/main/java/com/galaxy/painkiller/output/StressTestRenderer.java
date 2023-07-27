package com.galaxy.painkiller.output;

import com.galaxy.painkiller.model.Action;
import com.galaxy.painkiller.model.StressTestResponse;

public class StressTestRenderer implements Renderer<StressTestResponse, Action>{

    @Override
    public StressTestResponse render(Action action) {

        StressTestResponse response = new StressTestResponse();

        response.setTotalCount(action.getStats().getTotalCount().get());
        response.setSuccessCount(action.getStats().getSuccessCount().get());
        response.setFailedCount(action.getStats().getFailedCount().get());
        response.setActualQps(action.getStats().getActualQps());
        response.setMaxMills(action.getStats().getMaxMills());
        response.setMinMills(action.getStats().getMinMills());
        response.setAverageMills(action.getStats().getAverageMills());
        response.setTimeTaken(action.getStats().getTotalMills());
        return response;
    }

}
