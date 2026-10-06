package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Integer> {
}
