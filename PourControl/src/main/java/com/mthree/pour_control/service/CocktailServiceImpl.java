package com.mthree.pour_control.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mthree.pour_control.dto.Cocktail;
import com.mthree.pour_control.dto.RecipeIngredient;
import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.CocktailRepository;
import com.mthree.pour_control.model.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CocktailServiceImpl implements CocktailService {

    @Value("${spoonacular.api.key}")
    private String apiKey;

    @Value("${spoonacular.api.url}")
    private String baseUrl;

    private final IngredientRepository ingredientRepository;
    private final CocktailRepository cocktailRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CocktailServiceImpl(IngredientRepository ingredientRepository,
                               CocktailRepository cocktailRepository,
                               RestTemplateBuilder restTemplateBuilder,
                               ObjectMapper objectMapper) {
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public <T> T addCocktail(String cocktail) {
        int cocktailId = getCocktailRecipeFromApi(cocktail);
        if (cocktailId == 0) {
            return (T) Double.valueOf(0.0);
        }

        Cocktail currentCocktail = new Cocktail();
        currentCocktail.setId(cocktailId);
        currentCocktail.setName(cocktail);

        String recipeUrl = String.format("%s/%d/information?apiKey=%s", baseUrl, cocktailId, apiKey);
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        String instructions = "";

        try {
            String jsonResponse = restTemplate.getForObject(recipeUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            instructions = rootNode.path("instructions").asText("No instructions provided.");

            JsonNode extendedIngredients = rootNode.path("extendedIngredients");
            if (extendedIngredients.isArray()) {
                for (JsonNode node : extendedIngredients) {
                    int id = node.path("id").asInt();
                    String name = node.path("name").asText("Unknown");
                    String unit = node.path("unit").asText("");
                    double amount = node.path("amount").asDouble(0.0);

                    // Ensure Stock Ingredient exists in DB
                    StockIngredient stockIngredient = getOrCreateIngredient(id, name);

                    // Unit conversion
                    double mlRequired = convertToMl(unit) * amount;

                    // Build join entity retaining mlRequired
                    RecipeIngredient ri = new RecipeIngredient(
                            currentCocktail,
                            stockIngredient,
                            BigDecimal.valueOf(mlRequired)
                    );

                    recipeIngredients.add(ri);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching cocktail recipe details from API", e);
        }

        // Link recipe ingredients to cocktail
        currentCocktail.setRecipeIngredients(recipeIngredients);
        double result = saveRecipeInstructions(currentCocktail, instructions);

        return (T) Double.valueOf(result);
    }

    @Override
    public boolean removeCocktail(Integer id) {
        if (id != null && cocktailRepository.existsById(id)) {
            cocktailRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private int getCocktailRecipeFromApi(String cocktailName) {
        String searchUrl = String.format("%s/complexSearch?query=%s&apiKey=%s", baseUrl, cocktailName, apiKey);
        try {
            String jsonResponse = restTemplate.getForObject(searchUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode results = rootNode.path("results");
            if (results.isArray() && !results.isEmpty()) {
                return results.get(0).path("id").asInt();
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    private StockIngredient getOrCreateIngredient(int id, String name) {
        return ingredientRepository.findById(id)
                .orElseGet(() -> {
                    StockIngredient newIngredient = new StockIngredient();
                    newIngredient.setId(id);
                    newIngredient.setName(name);
                    newIngredient.setMlInStock(BigDecimal.ZERO);
                    newIngredient.setMlTarget(BigDecimal.ZERO);
                    return ingredientRepository.save(newIngredient);
                });
    }

    private double convertToMl(String ozMeasurement) {
        if (ozMeasurement == null || ozMeasurement.trim().isEmpty()) {
            return 1.0;
        }
        String unit = ozMeasurement.toLowerCase().trim();
        if (unit.contains("oz") || unit.contains("ounce")) {
            return 29.5735;
        } else if (unit.contains("cl")) {
            return 10.0;
        } else if (unit.contains("dash")) {
            return 0.92;
        } else if (unit.contains("tbsp") || unit.contains("tablespoon")) {
            return 14.7868;
        } else if (unit.contains("tsp") || unit.contains("teaspoon")) {
            return 4.92892;
        }
        return 1.0;
    }

    private double saveRecipeInstructions(Cocktail cocktail, String instructions) {
        if (cocktail != null) {
            cocktail.setInstructions(instructions);
            cocktailRepository.save(cocktail);
            return cocktail.getId();
        }
        return 0.0;
    }
}