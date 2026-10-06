package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

public class StockServiceImpl implements  StockService {

    @Autowired
    IngredientRepository ingredient;

    @Override
    public void UpdateStockQuantity(int id, double quantity) {
        StockIngredient stockIngredient = ingredient.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found: " + id));

        stockIngredient.setMlInStock(BigDecimal.valueOf(quantity));
        ingredient.save(stockIngredient);
    }

    @Override
    public void UpdateStockTargetValue(StockIngredient ingredient, double quantity) {

    }

    @Override
    public void ProcessDailyCloseOut(Map<StockIngredient, Double> ingredients) {

    }
}
