package com.mthree.pour_control.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cocktail_id", nullable = false)
    private Integer cocktailId;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @MapKeyColumn(name = "ingredient_id_key")
    private Map<Integer, Ingredient> ingredient = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    public Recipe() {
    }

    public Recipe(Integer cocktailId, String description) {
        this.cocktailId = cocktailId;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Integer cocktailId) {
        this.cocktailId = cocktailId;
    }

    public Map<Integer, Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(Map<Integer, Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}