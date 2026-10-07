package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * This class is used for managing stock and report data
 */
@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "*")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;

    }

    /**
     * Returns a list of all ingredients
     *
     * @return list of all ingredients
     */
    @GetMapping
    public List<StockIngredient> getAllIngredients() {
        return stockService.getAllIngredients();

    }

    /**
     * Update the quantity for a listed ingredient in stock.
     *
     * @param id the id of the ingredient
     * @param quantity the new quantity to set
     */
    @PutMapping("/updateQuantity/{id}")
    public void UpdateStockQuantity(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String quantity = request.get("quantity");

        stockService.UpdateStockQuantity(id, quantity);

    }

    /**
     * Update the target quantity for a listed ingredient in stock.
     * Target quantity is amount needed for one week of use.
     *
     * @param id the id the ingredient
     * @param quantity the new target quantity to set
     */
    @PutMapping("/updateTargetQuantity/{id}")
    public void UpdateStockTargetValue(@PathVariable Integer id, @RequestBody Map<String, String> request) {

        String quantity = request.get("quantity");
        stockService.UpdateStockTargetValue(id, quantity);

    }

    public void ProcessDailyCloseOut(Map<StockIngredient, Double> ingredients) {


    }
}
