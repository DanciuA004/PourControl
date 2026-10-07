package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.DailyAudit;
import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.DailyAuditRepository;
import com.mthree.pour_control.model.DailySaleRepository;
import com.mthree.pour_control.model.IngredientRepository;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        // get daily sales for date
        // for the daily sales, calculate how much of each ingredient should have been used
        // for each ingredient, calculate the difference between expected ingredients used
        //  (through daily sales) and actual ingredients used (through audit)

        return "";
    }

    @Override
    public Map<StockIngredient, BigDecimal> calculateReorder() {
        return Map.of();
    }
}
