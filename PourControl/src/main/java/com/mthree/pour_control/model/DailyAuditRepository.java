package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.AuditReorderDto;
import com.mthree.pour_control.dto.DailyAudit;
import com.mthree.pour_control.dto.StockIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAuditRepository extends JpaRepository<DailyAudit, Integer> {
    List<DailyAudit> findByDate(LocalDate date);

    Optional<DailyAudit> findByIngredientAndDate(StockIngredient ingredient, LocalDate date);

    @Query("""
        SELECT new com.mthree.pour_control.dto.AuditReorderDto(
            a.id,
            i.id,
            i.name,
            a.startMl,
            a.endMl,
            i.mlTarget,
            a.date
        )
        FROM DailyAudit a
        JOIN a.ingredient i
        WHERE a.date = :date
    """)
    List<AuditReorderDto> findAuditWithTargetStockByDate(@Param("date") LocalDate date);

}
