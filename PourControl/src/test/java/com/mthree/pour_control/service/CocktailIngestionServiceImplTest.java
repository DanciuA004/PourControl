package com.mthree.pour_control.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mthree.pour_control.dto.Cocktail;
import com.mthree.pour_control.dto.RecipeIngredient;
import com.mthree.pour_control.dto.StockIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Basic tests with no mocking framework.
 * Covers the pure logic: unit conversion and how the entities fit together.
 */
class CocktailIngestionLogicTest {

    private CocktailIngestionServiceImpl service;

    @BeforeEach
    void setUp() {
        // The repositories are not used by convertToMl, so null is fine here.
        // If you add RecipeIngredientRepository to the constructor, add another null.
        service = new CocktailIngestionServiceImpl(
                null,
                null,
                new RestTemplateBuilder(),
                new ObjectMapper());
    }

    /** convertToMl is private, so call it through reflection. */
    private double toMl(String unit) {
        Double result = ReflectionTestUtils.invokeMethod(service, "convertToMl", unit);
        return result;
    }

    // ---------- unit conversion ----------

    @Test
    @DisplayName("oz converts to 29.5735 ml")
    void ounces() {
        assertThat(toMl("oz")).isEqualTo(29.5735, within(0.0001));
        assertThat(toMl("ounces")).isEqualTo(29.5735, within(0.0001));
    }

    @Test
    @DisplayName("cl converts to 10 ml")
    void centilitres() {
        assertThat(toMl("cl")).isEqualTo(10.0, within(0.0001));
    }

    @Test
    @DisplayName("dash converts to 0.92 ml")
    void dash() {
        assertThat(toMl("dash")).isEqualTo(0.92, within(0.0001));
    }

    @Test
    @DisplayName("tablespoon and teaspoon convert correctly")
    void spoons() {
        assertThat(toMl("tbsp")).isEqualTo(14.7868, within(0.0001));
        assertThat(toMl("tablespoons")).isEqualTo(14.7868, within(0.0001));
        assertThat(toMl("tsp")).isEqualTo(4.92892, within(0.0001));
        assertThat(toMl("teaspoon")).isEqualTo(4.92892, within(0.0001));
    }

    @Test
    @DisplayName("units are matched case-insensitively and trimmed")
    void caseAndWhitespace() {
        assertThat(toMl("  OZ ")).isEqualTo(29.5735, within(0.0001));
    }

    @Test
    @DisplayName("amount x conversion: 2 oz is about 59.147 ml")
    void twoOunces() {
        assertThat(toMl("oz") * 2.0).isEqualTo(59.147, within(0.001));
    }

    @Test
    @DisplayName("blank, null and unknown units currently fall back to 1.0")
    void fallbackBehaviour() {
        assertThat(toMl("")).isEqualTo(1.0);
        assertThat(toMl(null)).isEqualTo(1.0);
        assertThat(toMl("lime")).isEqualTo(1.0);
    }

    // ---------- entities ----------

    @Test
    @DisplayName("RecipeIngredient keeps its cocktail, ingredient and ml amount")
    void recipeIngredientHoldsValues() {
        Cocktail cocktail = new Cocktail();
        StockIngredient tequila = new StockIngredient(101, "Tequila", BigDecimal.ZERO, BigDecimal.ZERO);

        RecipeIngredient ri = new RecipeIngredient(cocktail, tequila, new BigDecimal("59.15"));

        assertThat(ri.getCocktail()).isSameAs(cocktail);
        assertThat(ri.getStockIngredient()).isSameAs(tequila);
        assertThat(ri.getMlRequired()).isEqualByComparingTo("59.15");
    }

    @Test
    @DisplayName("a new Cocktail starts with an empty recipe")
    void newCocktailHasEmptyRecipe() {
        Cocktail cocktail = new Cocktail();

        assertThat(cocktail.getRecipeIngredients()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Cocktail stores its id, name, menu flag and recipe")
    void cocktailStoresValues() {
        Cocktail cocktail = new Cocktail();
        cocktail.setId(12345);
        cocktail.setName("Margarita");
        cocktail.setOnMenu(true);

        StockIngredient tequila = new StockIngredient(101, "Tequila", BigDecimal.ZERO, BigDecimal.ZERO);
        List<RecipeIngredient> recipe = new ArrayList<>();
        recipe.add(new RecipeIngredient(cocktail, tequila, new BigDecimal("59.15")));
        cocktail.setRecipeIngredients(recipe);

        assertThat(cocktail.getId()).isEqualTo(12345);
        assertThat(cocktail.getName()).isEqualTo("Margarita");
        assertThat(cocktail.isOnMenu()).isTrue();
        assertThat(cocktail.getRecipeIngredients()).hasSize(1);
    }
}