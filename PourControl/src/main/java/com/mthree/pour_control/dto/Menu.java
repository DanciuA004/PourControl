package com.mthree.pour_control.dto;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

public class Menu {

    private Long id;

    private Map<Integer, Cocktail> cocktailList = new HashMap<>();

    public Menu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Integer, Cocktail> getCocktailList() {
        return cocktailList;
    }

    public void setCocktailList(Map<Integer, Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }
}