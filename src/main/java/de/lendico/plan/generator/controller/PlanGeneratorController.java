package de.lendico.plan.generator.controller;

import de.lendico.plan.generator.request.PlanGeneratorRequest;
import de.lendico.plan.generator.response.MonthPlan;
import de.lendico.plan.generator.service.PlanGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
public class PlanGeneratorController {
    private final static Logger logger = LoggerFactory.getLogger(PlanGeneratorController.class);

    @RequestMapping(value = "/generate-plan", method = RequestMethod.POST)
    public List<MonthPlan> generatePlan(@RequestBody PlanGeneratorRequest request) {
        Objects.requireNonNull(request, "Request object came as null. Check log for details");
        logger.info("Received \n {} \n request for processing", request.toString());
        List<MonthPlan> monthPlans;
        long start = System.currentTimeMillis();
        try {
            logger.info("Start processing request...");
            monthPlans = new PlanGeneratorService(request).generatePlan();
        } catch (Exception e) {
            logger.info("Exception happened during request processing!", new RuntimeException(e.getMessage()));
            return Collections.emptyList();
        }
        long finish = System.currentTimeMillis();
        logger.info("*** Proceed request successfully in {} ms***", finish - start);
        logger.info("Response is {}, " +
                "\n with months plan size {}", monthPlans.toString(), monthPlans.size());
        return monthPlans;
    }
}
