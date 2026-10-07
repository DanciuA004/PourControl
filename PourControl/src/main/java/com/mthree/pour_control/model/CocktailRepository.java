package com.mthree.pour_control.model;

import com.mthree.pour_control.dto.Cocktail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Integer> {
    @Override
    @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.stockIngredient"})
    List<Cocktail> findAll();
}
