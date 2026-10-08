package com.mthree.pour_control.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AuditReorderDto {
    Integer auditId;
    Integer ingredientId;
    String ingredientName;
    BigDecimal startMl;
    BigDecimal endMl;
    BigDecimal targetMl;
    LocalDate date;

    public AuditReorderDto(Integer auditId, Integer ingredientId, String ingredientName,
                           BigDecimal startMl, BigDecimal endMl, BigDecimal targetMl, LocalDate date) {
        this.auditId = auditId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.startMl = startMl;
        this.endMl = endMl;
        this.targetMl = targetMl;
        this.date = date;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
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

    public BigDecimal getTargetMl() {
        return targetMl;
    }

    public void setTargetMl(BigDecimal targetMl) {
        this.targetMl = targetMl;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
