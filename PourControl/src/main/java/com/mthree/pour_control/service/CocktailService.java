package com.mthree.pour_control.service;

public interface CocktailService {
    public <T> T addCocktail(String cocktail);

    public boolean removeCocktail(Integer id);
}
