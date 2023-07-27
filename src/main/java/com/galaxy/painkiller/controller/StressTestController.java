package com.galaxy.painkiller.controller;

import com.galaxy.painkiller.model.StressTestRequest;
import com.galaxy.painkiller.model.StressTestResponse;
import com.galaxy.painkiller.service.StressTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stressTest")
public class StressTestController {

    @Autowired
    private StressTestService stressTestService;

    @ResponseBody
    @PostMapping("/run")
    public StressTestResponse run(@RequestBody StressTestRequest stressTestRequest){
        StressTestResponse response = stressTestService.run(stressTestRequest);
        return response;
    }
}
