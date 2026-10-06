package com.mthree.pour_control.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingredientInCocktail")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cocktail_id")
    @JsonIgnore
    private Cocktail cocktail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    private StockIngredient stockIngredient;

    @Column(name = "ml_required", precision = 10, scale = 2, nullable = false)
    private BigDecimal mlRequired;

    public RecipeIngredient() {}

    public RecipeIngredient(Cocktail cocktail, StockIngredient stockIngredient, BigDecimal mlRequired) {
        this.cocktail = cocktail;
        this.stockIngredient = stockIngredient;
        this.mlRequired = mlRequired;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cocktail getCocktail() { return cocktail; }
    public void setCocktail(Cocktail cocktail) { this.cocktail = cocktail; }

    public StockIngredient getStockIngredient() { return stockIngredient; }
    public void setStockIngredient(StockIngredient stockIngredient) { this.stockIngredient = stockIngredient; }

    public BigDecimal getMlRequired() { return mlRequired; }
    public void setMlRequired(BigDecimal mlRequired) { this.mlRequired = mlRequired; }
}