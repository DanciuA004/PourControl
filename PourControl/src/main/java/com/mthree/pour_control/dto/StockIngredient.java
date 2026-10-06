package com.mthree.pour_control.dto;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingredient")
public class StockIngredient {

    @Id
    private Integer id;

    @Column(name = "ingredient_name", nullable = false, length = 30)
    private String name;

//    @Column(precision = 10, scale = 2)
//    private BigDecimal price;

    @Column(name = "ml_in_stock", precision = 10, scale = 2)
    private BigDecimal mlInStock;

    @Column(name = "ml_target", precision = 10, scale = 2)
    private BigDecimal mlTarget;

    public StockIngredient() {
    }

    public StockIngredient(Integer id, String name, BigDecimal mlInStock, BigDecimal mlTarget) {
        this.id = id;
        this.name = name;
        this.mlInStock = mlInStock;
        this.mlTarget = mlTarget;
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

    public BigDecimal getMlInStock() {
        return mlInStock;
    }

    public void setMlInStock(BigDecimal mlInStock) {
        this.mlInStock = mlInStock;
    }

    public BigDecimal getMlTarget() {
        return mlTarget;
    }

    public void setMlTarget(BigDecimal mlTarget) {
        this.mlTarget = mlTarget;
    }
}