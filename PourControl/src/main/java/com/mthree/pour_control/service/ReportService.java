package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ReportService {

    public String generateVarianceReport(LocalDate date);

    public String calculateReorder(LocalDate date);
}
