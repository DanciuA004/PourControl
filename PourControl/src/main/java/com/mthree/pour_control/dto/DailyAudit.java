package com.mthree.pour_control.dto;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dailyAudit")
public class DailyAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Integer ingredientId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_ml", nullable = false)
    private BigDecimal startMl;

    @Column(name = "end_ml", nullable = false)
    private BigDecimal endMl;


    public DailyAudit() {
    }


    public DailyAudit(Integer ingredientId, LocalDate date, BigDecimal startMl, BigDecimal endMl) {
        this.ingredientId = ingredientId;
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

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
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
