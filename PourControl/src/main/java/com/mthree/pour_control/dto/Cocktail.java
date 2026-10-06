package com.mthree.pour_control.dto;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cocktail")
public class Cocktail {

    @Id
    @Column(name = "cid")
    private int id;

    @Column(name = "cocktail_name", nullable = false, length = 30)
    private String name;

    @Column(name = "on_menu", nullable = false)
    private boolean onMenu;

    @Column
    private String instructions;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    public Cocktail() {}

    public int getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isOnMenu() { return onMenu; }
    public void setOnMenu(boolean onMenu) { this.onMenu = onMenu; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public List<RecipeIngredient> getRecipeIngredients() { return recipeIngredients; }
    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) { this.recipeIngredients = recipeIngredients; }
}