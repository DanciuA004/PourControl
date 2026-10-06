package com.mthree.pour_control.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "dailySale")
public class DailySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cocktail_id", nullable = false)
    private Integer cocktailId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "qty_sold", nullable = false)
    private int quantitySold;

    public DailySale() {
    }

    public DailySale(Integer cocktailId, LocalDate date, int quantitySold) {
        this.cocktailId = cocktailId;
        this.date = date;
        this.quantitySold = quantitySold;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Integer cocktailId) {
        this.cocktailId = cocktailId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
}