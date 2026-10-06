package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.DailySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailySaleRepository extends JpaRepository<DailySale, Integer> {
}
