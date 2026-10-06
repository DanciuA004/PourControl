package com.mthree.pour_control.service;

public interface CocktailIngestionService {
    public <T> T addCocktail(String cocktail);

    public boolean removeCocktail(Integer id);
}
