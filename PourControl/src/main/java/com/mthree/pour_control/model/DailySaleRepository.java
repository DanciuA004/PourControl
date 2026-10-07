package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.DailyAudit;
import com.mthree.pour_control.dto.DailySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailySaleRepository extends JpaRepository<DailySale, Integer> {
    List<DailySale> findByDate(LocalDate date);
}
