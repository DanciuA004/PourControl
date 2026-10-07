package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.Cocktail;
import java.util.List;

public interface CocktailService {
    Cocktail addCocktail(String cocktail);
    boolean removeCocktail(Integer id);
    List<Cocktail> getAllCocktails();
}