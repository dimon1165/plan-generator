package de.lendico.plan.generator.service;

import de.lendico.plan.generator.controller.PlanGeneratorController;
import de.lendico.plan.generator.request.PlanGeneratorRequest;
import de.lendico.plan.generator.response.MonthPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlanGeneratorService {
    private final static Logger logger = LoggerFactory.getLogger(PlanGeneratorController.class);
    private static double MONTHS_IN_YEAR = 12;
    private static double CENTS_IN_ONE_EURO = 100;
    private static double DAYS_IN_YEAR = 360;
    private static double DAYS_IN_MONTH = 30;
    private PlanGeneratorRequest request;

    public PlanGeneratorService(PlanGeneratorRequest request) {
        this.request = request;
    }

    public List<MonthPlan> generatePlan() {
        List<MonthPlan> monthPlans = new ArrayList<>();
        double borrowerPaymentAmount = formatValue(calculateBorrowerPaymentAmount(request));
        for (int i = 0; i < request.getDuration(); i++) {
            LocalDateTime monthsDateTime = request.getStartDate().plusMonths(i);
            monthPlans.add(createPlanForOneMonth(monthsDateTime, borrowerPaymentAmount, i));
        }
        return monthPlans;
    }

    private MonthPlan createPlanForOneMonth(LocalDateTime monthsDateTime, double borrowerPaymentAmount, int i) {
        double interest = calculateInterest();
        double principal = calculatePrincipal(borrowerPaymentAmount, interest);
        double remainingOutstandingPrincipal = calculateRemainingOutstandingPrincipal(principal);
        // calculate borrower payment amount for last month
        if (i == request.getDuration() - 1) {
            borrowerPaymentAmount = formatValue(principal + interest);
        }
        MonthPlan monthPlan = new MonthPlan();
        monthPlan.setBorrowerPaymentAmount(borrowerPaymentAmount);
        monthPlan.setDate(monthsDateTime);
        monthPlan.setInitialOutstandingPrincipal(formatValue(request.getLoanAmount()));
        monthPlan.setInterest(formatValue(interest));
        monthPlan.setPrincipal(formatValue(principal));
        monthPlan.setRemainingOutstandingPrincipal(formatValue(remainingOutstandingPrincipal));
        this.request.setLoanAmount(formatValue(remainingOutstandingPrincipal));
        return monthPlan;
    }

    private double calculateBorrowerPaymentAmount(PlanGeneratorRequest request) {
        double rate = (request.getNominalRate() / MONTHS_IN_YEAR) / CENTS_IN_ONE_EURO;
        double borrowerPaymentAmount = (request.getLoanAmount() * rate) / (1 - Math.pow(1 + rate, (-request.getDuration())));
        logger.info("Calculated Borrower Payment Amount is : {}", borrowerPaymentAmount);
        return borrowerPaymentAmount;
    }

    private double calculateInterest() {
        double interest = ((this.request.getNominalRate() * DAYS_IN_MONTH * this.request.getLoanAmount()) / DAYS_IN_YEAR) / CENTS_IN_ONE_EURO;
        return interest;
    }

    private double calculatePrincipal(double borrowerPaymentAmount, double interest) {
        double principal = borrowerPaymentAmount - interest;
        if (principal > this.request.getLoanAmount()) {
            principal = request.getLoanAmount();
        }
        return principal;
    }

    private double calculateRemainingOutstandingPrincipal(double principal) {
        double remainingOutstandingPrincipal = request.getLoanAmount() - principal;
        return remainingOutstandingPrincipal;
    }

    private double formatValue(double valueToFormat) {
        double formattedValue = BigDecimal.valueOf(valueToFormat)
                .setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        return formattedValue;
    }
}