package com.mthree.pour_control.dto;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_audit")
public class DailyAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private StockIngredient ingredient;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_ml", nullable = false)
    private BigDecimal startMl;

    @Column(name = "end_ml", nullable = false)
    private BigDecimal endMl;


    public DailyAudit() {
    }


    public DailyAudit(StockIngredient ingredient, LocalDate date, BigDecimal startMl, BigDecimal endMl) {
        this.ingredient = ingredient;
        this.date = date;
        this.startMl = startMl;
        this.endMl = endMl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StockIngredient getIngredientId() {
        return ingredient;
    }

    public void setIngredientId(StockIngredient ingredient) {
        this.ingredient = ingredient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getStartMl() {
        return startMl;
    }

    public void setStartMl(BigDecimal startMl) {
        this.startMl = startMl;
    }

    public BigDecimal getEndMl() {
        return endMl;
    }

    public void setEndMl(BigDecimal endMl) {
        this.endMl = endMl;
    }
}
