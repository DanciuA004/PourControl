package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;

import java.util.Map;

public interface StockService {
    void UpdateStockQuantity(int id, double quantity);

    void UpdateStockTargetValue(StockIngredient ingredient, double quantity);

    void ProcessDailyCloseOut(Map<StockIngredient, Double> ingredients);
}
