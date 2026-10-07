package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.DailyAudit;
import com.mthree.pour_control.dto.StockIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAuditRepository extends JpaRepository<DailyAudit, Integer> {
    List<DailyAudit> findByDate(LocalDate date);

    Optional<DailyAudit> findByIngredientAndDate(StockIngredient ingredient, LocalDate date);
}
