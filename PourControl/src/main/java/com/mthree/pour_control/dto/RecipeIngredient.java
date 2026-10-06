package com.mthree.pour_control.dto;

import java.math.BigDecimal;

public class RecipeIngredient {

    private Integer id;

    private String name;

    private BigDecimal mlRequired;

    public RecipeIngredient() {
    }

    public RecipeIngredient(Integer id, String name, BigDecimal mlRequired) {
        this.id = id;
        this.name = name;
        this.mlRequired = mlRequired;
    }


    public Integer getId() {
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

    public BigDecimal getMlRequired() {
        return mlRequired;
    }

    public void setMlRequired(BigDecimal mlRequired) {
        this.mlRequired = mlRequired;
    }
}
