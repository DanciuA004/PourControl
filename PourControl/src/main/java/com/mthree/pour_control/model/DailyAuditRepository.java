package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.DailyAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyAuditRepository extends JpaRepository<DailyAudit, Integer> {
}
