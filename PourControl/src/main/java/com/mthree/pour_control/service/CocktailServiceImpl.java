package com.mthree.pour_control.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mthree.pour_control.dto.Cocktail;
import com.mthree.pour_control.dto.RecipeIngredient;
import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.CocktailRepository;
import com.mthree.pour_control.model.DailySaleRepository;
import com.mthree.pour_control.model.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CocktailServiceImpl implements CocktailService {

    @Value("${spoonacular.api.key}")
    private String apiKey;

    @Value("${spoonacular.api.url}")
    private String baseUrl;

    private final DailySaleRepository dailySaleRepository;
    private final IngredientRepository ingredientRepository;
    private final CocktailRepository cocktailRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CocktailServiceImpl(IngredientRepository ingredientRepository,
                               CocktailRepository cocktailRepository,
                               DailySaleRepository dailySaleRepository,
                               RestTemplateBuilder restTemplateBuilder,
                               ObjectMapper objectMapper) {
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.dailySaleRepository = dailySaleRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Cocktail addCocktail(String cocktail) {
        if (cocktail == null || cocktail.trim().isEmpty()) {
            throw new IllegalArgumentException("Please provide a cocktail name.");
        }

        int cocktailId = getCocktailRecipeFromApi(cocktail.trim());
        if (cocktailId == 0) {
            throw new IllegalArgumentException("Could not find any drink matching '" + cocktail + "'. Please check the spelling.");
        }

        // Fetch managed entity or instantiate new one
        Cocktail currentCocktail = cocktailRepository.findById(cocktailId)
                .orElseGet(() -> {
                    Cocktail c = new Cocktail();
                    c.setId(cocktailId);
                    return c;
                });

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

                    // Fetch or persist stock ingredient
                    StockIngredient stockIngredient = getOrCreateIngredient(id, name);

                    double mlRequired = convertToMl(unit) * amount;
                    BigDecimal mlBigDecimal = BigDecimal.valueOf(mlRequired)
                            .setScale(2, RoundingMode.HALF_UP);

                    // Combine amounts if duplicate ingredient nodes exist
                    RecipeIngredient existing = recipeIngredients.stream()
                            .filter(ri -> ri.getStockIngredient().getId() == stockIngredient.getId())
                            .findFirst()
                            .orElse(null);

                    if (existing != null) {
                        existing.setMlRequired(existing.getMlRequired().add(mlBigDecimal));
                    } else {
                        RecipeIngredient ri = new RecipeIngredient(
                                currentCocktail,
                                stockIngredient,
                                mlBigDecimal
                        );
                        recipeIngredients.add(ri);
                    }
                }
            }
        } catch (Exception e) {
            // Throw user-friendly message caught by GlobalExceptionHandler
            throw new IllegalArgumentException("Failed to retrieve recipe details for '" + cocktail + "'. " + e.getMessage());
        }

        currentCocktail.setInstructions(instructions);

        currentCocktail.getRecipeIngredients().clear();
        currentCocktail.getRecipeIngredients().addAll(recipeIngredients);

        return cocktailRepository.save(currentCocktail);
    }

    @Override
    @Transactional
    public boolean removeCocktail(Integer id) {
        if (id != null) {
            Optional<Cocktail> cocktailOptional = cocktailRepository.findById(id);
            if (cocktailOptional.isPresent()) {
                Cocktail cocktail = cocktailOptional.get();

                // Delete foreign key dependents first
                dailySaleRepository.deleteByCocktailId(cocktail);

                cocktailRepository.delete(cocktail);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll();
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
}