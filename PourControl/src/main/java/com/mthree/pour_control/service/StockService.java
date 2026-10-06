package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;

import java.util.Map;

public interface StockService {
    void UpdateStockQuantity(int id, String quantity);

    void UpdateStockTargetValue(StockIngredient ingredient, String quantity);

    void ProcessDailyCloseOut(Map<StockIngredient, String> ingredients);
}
