package com.mthree.pour_control.dto;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;

@Entity
@Table
public class Cocktail {

    @Id
    private int id;

    @Column(name = "cocktail_name", nullable = false, length = 30)
    private String name;

    @Column(name = "on_menu", nullable = false)
    private boolean onMenu;

    @Column
    private String instructions;

    @ManyToMany
    @JoinTable(name = "ingredient_in_cocktail",
        joinColumns = {@JoinColumn(name = "ingredient_id")},
        inverseJoinColumns = {@JoinColumn(name = "cocktail_id")})
    private List<StockIngredient> ingredients;

    public Cocktail() {
    }

    public Cocktail(Integer id, String name, boolean onMenu, String instructions, List<StockIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.onMenu = onMenu;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnMenu() {
        return onMenu;
    }

    public void setOnMenu(boolean onMenu) {
        this.onMenu = onMenu;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<StockIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<StockIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}