package com.mthree.pour_control.dto;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_sale")
public class DailySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sid")
    private Integer sid;

    @ManyToOne
    @JoinColumn(name = "cocktail_id", nullable = false)
    private Cocktail cocktailId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "qty_sold", nullable = false)
    private int quantitySold;

    public DailySale() {
    }

    public DailySale(Cocktail cocktailId, LocalDate date, int quantitySold) {
        this.cocktailId = cocktailId;
        this.date = date;
        this.quantitySold = quantitySold;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Cocktail getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Cocktail cocktailId) {
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
