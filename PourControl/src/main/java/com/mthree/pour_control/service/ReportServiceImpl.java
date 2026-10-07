package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.*;
import com.mthree.pour_control.model.DailyAuditRepository;
import com.mthree.pour_control.model.DailySaleRepository;
import com.mthree.pour_control.model.IngredientRepository;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private IngredientRepository ingredientRepository;
    private DailyAuditRepository dailyAuditRepository;
    private DailySaleRepository dailySaleRepository;

    public ReportServiceImpl(IngredientRepository ingredientRepository, DailyAuditRepository dailyAuditRepository, DailySaleRepository dailySaleRepository) {
        this.ingredientRepository = ingredientRepository;
        this.dailyAuditRepository = dailyAuditRepository;
        this.dailySaleRepository = dailySaleRepository;
    }


    @Override
    public String generateVarianceReport(LocalDate date) {
        List<DailyAudit> auditsForDate = dailyAuditRepository.findByDate(date);
        List<DailySale> salesForDate = dailySaleRepository.findByDate(date);

        // Maps ingredient id to its expected use
        Map<Integer, BigDecimal> expectedIngredientUse = new HashMap<>();
        for (DailySale sale : salesForDate) {
            Cocktail cocktail = sale.getCocktailId();
            List<RecipeIngredient> cocktailIngredients = cocktail.getRecipeIngredients();

            // Iterate through and add ingredient amounts to expected use
            for (RecipeIngredient i : cocktailIngredients) {
                Integer id = i.getId();
                BigDecimal totalMl = i.getMlRequired().multiply(new BigDecimal(sale.getQuantitySold()));
                // Adds mls required to existing map entry
                if (expectedIngredientUse.containsKey(id)) {
                    expectedIngredientUse.replace(i.getId(),
                            expectedIngredientUse.get(id)
                                                .add(totalMl));
                }
                // Adds new entry
                expectedIngredientUse.put(i.getId(), totalMl);
            }
        }
        // for the daily sales, calculate how much of each ingredient should have been used

        Map<Integer, BigDecimal> actualUsage = new HashMap<>();

        for (Integer iid : expectedIngredientUse.keySet()) {
            DailyAudit ingredientAudit = auditsForDate.stream()
                    .filter(a -> a.getIngredient().getId().equals(iid))
                    .findFirst()
                    .orElse(null); // TODO: throw custom error or something?
            BigDecimal actualUsed = ingredientAudit.getStartMl().subtract(ingredientAudit.getEndMl());
            actualUsage.put(iid, actualUsed);
        }


        // Calculate the difference between expected and actual

        String varianceReport = "";

        for (Integer iid : expectedIngredientUse.keySet()) {
            BigDecimal expected = expectedIngredientUse.get(iid);
            BigDecimal actual = actualUsage.get(iid);

            // Add expected + actual
            String ingredientReport = ingredientRepository.findById(iid).get().getName() + "\n"; // start off with name
            ingredientReport += "Expected usage: " + expected + "ml \n";
            ingredientReport += "Actual usage: " + actual + "ml \n";


            // Calculate difference and percentage difference
            BigDecimal diff = actual.subtract(expected);
            BigDecimal percentDiff;

            if (expected.equals(new BigDecimal(0))) {
                percentDiff = new BigDecimal(0);
            } else {
                percentDiff = diff.divide(expected, 2, RoundingMode.FLOOR);
            }

            // Define report line based on whether ingredients were over- or underused
            String diffLine = "";
            switch (diff.compareTo(new BigDecimal(0))) {
                case 1:
                    diffLine = "Used " + diff + "ml (" + percentDiff + "%) more than expected";
                    break;
                case 0:
                    diffLine = "Used exactly as much as expected";
                    break;
                case -1:
                    diffLine = "Used " + diff.abs() + "ml (" + percentDiff + "%) less than expected";
            }
            ingredientReport += diffLine + "\n";

            // Add ingredient report to variance report
            varianceReport += ingredientReport;
        }

        return varianceReport;
    }

    @Override
    public Map<StockIngredient, BigDecimal> calculateReorder() {
        return Map.of();
    }

    /***
     * Returns number of ml used of a single ingredient according to the audit
     * @param iid id of ingredient to calculate usage of
     * @return actual value of ml used of ingredient
     */
    private BigDecimal calculateActualUsage(Integer iid) {
        
        return null;
    }
}
