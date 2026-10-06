package com.mthree.pour_control.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "menu_cocktails",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "cocktail_id")
    )
    @MapKeyColumn(name = "cocktail_key")
    private Map<Integer, Cocktail> cocktailList = new HashMap<>();

    public Menu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Integer, Cocktail> getCocktailList() {
        return cocktailList;
    }

    public void setCocktailList(Map<Integer, Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }
}