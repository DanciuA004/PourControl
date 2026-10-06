package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.StockIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<StockIngredient, Integer> {
}
