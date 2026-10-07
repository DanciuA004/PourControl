package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;

    }

    @PutMapping("/updateQuantity/{id}")
    public void UpdateStockQuantity(@PathVariable Integer id, @RequestBody String quantity) {
        stockService.UpdateStockQuantity(id, quantity);

    }

    @PostMapping("/updateTargetQuantity/{id}")
    public void UpdateStockTargetValue(StockIngredient ingredient, String quantity) {
        stockService.UpdateStockTargetValue(ingredient, quantity);

    }

    public void ProcessDailyCloseOut(Map<StockIngredient, Double> ingredients) {


    }
}
