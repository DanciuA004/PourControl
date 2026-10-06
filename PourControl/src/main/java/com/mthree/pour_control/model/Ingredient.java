package com.mthree.pour_control.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "ml_volume", nullable = false, precision = 10, scale = 2)
    private BigDecimal mlVolume;

    public Ingredient() {
    }

    public Ingredient(String name, BigDecimal price, BigDecimal mlVolume) {
        this.name = name;
        this.price = price;
        this.mlVolume = mlVolume;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMlVolume() {
        return mlVolume;
    }

    public void setMlVolume(BigDecimal mlVolume) {
        this.mlVolume = mlVolume;
    }
}