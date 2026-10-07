package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.*;
import com.mthree.pour_control.model.CocktailRepository;
import com.mthree.pour_control.model.DailyAuditRepository;
import com.mthree.pour_control.model.DailySaleRepository;
import com.mthree.pour_control.model.IngredientRepository;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
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
        List<StockIngredient> ingredients = ingredientRepository.findAll();
        List<DailyAudit> auditsForDate = dailyAuditRepository.findByDate(date);
        List<DailySale> salesForDate = dailySaleRepository.findByDate(date);

        // Maps ingredient id to its predicted use
        Map<Integer, BigDecimal> predictedIngredientUse = new HashMap<>();
        for (DailySale sale : salesForDate) {
            Cocktail cocktail = sale.getCocktailId();
            List<RecipeIngredient> cocktailIngredients = cocktail.getRecipeIngredients();

            // Iterate through and add ingredient amounts to predicted use
            for (RecipeIngredient i : cocktailIngredients) {
                Integer id = i.getId();
                // Adds mls required to existing map entry
                if (predictedIngredientUse.containsKey(id)) {
                    predictedIngredientUse.replace(i.getId(), predictedIngredientUse.get(id).add(i.getMlRequired()));
                }
                // Adds new entry
                predictedIngredientUse.put(i.getId(), i.getMlRequired());
            }
        }
        // for the daily sales, calculate how much of each ingredient should have been used
        // for each ingredient, calculate the difference between expected ingredients used
        //  (through daily sales) and actual ingredients used (through audit)

        String varianceReport = "";

        for (DailyAudit audit : auditsForDate) {

            String ingredientVariance = "";
        }

        return varianceReport;
    }

    @Override
    public Map<StockIngredient, BigDecimal> calculateReorder() {
        return Map.of();
    }
}
