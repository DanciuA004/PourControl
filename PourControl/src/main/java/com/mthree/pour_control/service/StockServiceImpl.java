package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.IngredientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    IngredientRepository ingredient;

    public StockServiceImpl(IngredientRepository ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public List<StockIngredient> getAllIngredients() {
        return ingredient.findAll();

    }

    @Override
    public void UpdateStockQuantity(int id, String quantity) {
        StockIngredient stockIngredient = ingredient.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found: " + id));

        stockIngredient.setMlInStock(new BigDecimal(quantity));
        ingredient.save(stockIngredient);
    }

    @Override
    public void UpdateStockTargetValue(int id, String quantity) {
        StockIngredient stockIngredient = ingredient.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found: " + id));

        stockIngredient.setMlTarget(new BigDecimal(quantity));
        ingredient.save(stockIngredient);
    }

    @Override
    public void ProcessDailyCloseOut(Map<StockIngredient, String> ingredients) {

    }
}
