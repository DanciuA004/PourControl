package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/{id}")
    public void UpdateStockQuantity(@PathVariable("id") Integer id, @RequestBody String quantity) {
        stockService.UpdateStockQuantity(id, quantity);

    }

    public void UpdateStockTargetValue(StockIngredient ingredient, String quantity) {
        stockService.UpdateStockTargetValue(ingredient, quantity);

    }

    public void ProcessDailyCloseOut(Map<StockIngredient, Double> ingredients) {


    }


}
