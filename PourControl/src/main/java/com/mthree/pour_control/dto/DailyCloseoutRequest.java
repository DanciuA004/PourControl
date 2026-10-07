package com.mthree.pour_control.dto;

import java.util.Map;

public class DailyCloseoutRequest {

    // Maps Cocktail ID -> Quantity Sold
    private Map<Integer, Integer> cocktailCloseout;

    // Maps Stock Ingredient ID -> New Volume (mL)
    private Map<Integer, Integer> stockCloseout;

    public DailyCloseoutRequest() {}

    public Map<Integer, Integer> getCocktailCloseout() {
        return cocktailCloseout;
    }

    public void setCocktailCloseout(Map<Integer, Integer> cocktailCloseout) {
        this.cocktailCloseout = cocktailCloseout;
    }

    public Map<Integer, Integer> getStockCloseout() {
        return stockCloseout;
    }

    public void setStockCloseout(Map<Integer, Integer> stockCloseout) {
        this.stockCloseout = stockCloseout;
    }
}
