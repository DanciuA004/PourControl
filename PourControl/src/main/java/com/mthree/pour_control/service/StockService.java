package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;

import java.util.List;
import java.util.Map;

public interface StockService {
    List<StockIngredient> getAllIngredients();

    void UpdateStockQuantity(int id, String quantity);

    void UpdateStockTargetValue(int id, String quantity);

    void ProcessDailyCloseOut(Map<StockIngredient, String> ingredients);
}
