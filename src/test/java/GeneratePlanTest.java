import de.lendico.plan.generator.request.PlanGeneratorRequest;
import de.lendico.plan.generator.response.MonthPlan;
import de.lendico.plan.generator.service.PlanGeneratorService;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class GeneratePlanTest {

    @Test
    public void testPlanGeneratorService() {
        PlanGeneratorRequest request = new PlanGeneratorRequest();
        request.setLoanAmount(5000);
        request.setNominalRate(5.0);
        request.setDuration(24);
        request.setStartDate(LocalDateTime.now());
        PlanGeneratorService planGeneratorService = new PlanGeneratorService(request);
        LinkedList<MonthPlan> monthPlans = new LinkedList<>(planGeneratorService.generatePlan());
        Assert.assertEquals(24, monthPlans.size());

        //check first month
        MonthPlan firstMonth = monthPlans.getFirst();
        Assert.assertEquals(219.36, firstMonth.getBorrowerPaymentAmount(), 0);
        Assert.assertEquals(5000.0, firstMonth.getInitialOutstandingPrincipal(), 0);
        Assert.assertEquals(20.83, firstMonth.getInterest(), 0);
        Assert.assertEquals(198.53, firstMonth.getPrincipal(), 0);

        MonthPlan lastMonth = monthPlans.getLast();
        Assert.assertEquals(219.28, lastMonth.getBorrowerPaymentAmount(), 0);
        Assert.assertEquals(218.37, lastMonth.getInitialOutstandingPrincipal(), 0);
        Assert.assertEquals(0.91, lastMonth.getInterest(), 0);
        Assert.assertEquals(218.37, lastMonth.getPrincipal(), 0);
    }
}
