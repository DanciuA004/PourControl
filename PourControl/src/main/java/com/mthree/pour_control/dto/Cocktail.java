package com.mthree.pour_control.model;

import jakarta.persistence.*;

@Entity
@Table
public class Cocktail {

    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "on_menu", nullable = false)
    private boolean onMenu;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;

    public Cocktail() {
    }

    public Cocktail(String name, boolean onMenu, Recipe recipe) {
        this.name = name;
        this.onMenu = onMenu;
        this.recipe = recipe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnMenu() {
        return onMenu;
    }

    public void setOnMenu(boolean onMenu) {
        this.onMenu = onMenu;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}