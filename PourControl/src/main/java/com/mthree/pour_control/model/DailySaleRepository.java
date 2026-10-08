package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.Cocktail;
import com.mthree.pour_control.dto.DailyAudit;
import com.mthree.pour_control.dto.DailySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySaleRepository extends JpaRepository<DailySale, Integer> {
    List<DailySale> findByDate(LocalDate date);

    Optional<DailySale> findByCocktailIdAndDate(Cocktail cocktail, LocalDate date);

    void deleteByCocktailId(Cocktail cocktail);
}