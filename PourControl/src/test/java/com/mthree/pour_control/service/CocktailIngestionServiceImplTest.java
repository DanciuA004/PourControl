package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.Cocktail;
import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.CocktailRepository;
import com.mthree.pour_control.model.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CocktailIngestionServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private CocktailRepository cocktailRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CocktailIngestionServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(service, "baseUrl", "https://api.spoonacular.com/recipes");
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
    }

    @Test
    @DisplayName("addCocktail: Success - Fetches API data and persists cocktail and ingredients")
    void addCocktail_Success() {
        String searchJsonResponse = """
            {
              "results": [
                { "id": 11000, "title": "Margarita" }
              ]
            }
            """;

        String recipeInfoJsonResponse = """
            {
              "instructions": "Shake with ice and strain into glass.",
              "extendedIngredients": [
                { "id": 1, "name": "Tequila", "unit": "oz", "amount": 2.0 },
                { "id": 2, "name": "Lime Juice", "unit": "oz", "amount": 1.0 }
              ]
            }
            """;

        when(restTemplate.getForObject(contains("/complexSearch"), eq(String.class)))
                .thenReturn(searchJsonResponse);
        when(restTemplate.getForObject(contains("/11000/information"), eq(String.class)))
                .thenReturn(recipeInfoJsonResponse);

        // Map to simulate in-memory repository storage for findById and save
        Map<Integer, StockIngredient> dbStore = new HashMap<>();

        when(ingredientRepository.findById(anyInt()))
                .thenAnswer(invocation -> {
                    Integer id = invocation.getArgument(0);
                    return Optional.ofNullable(dbStore.get(id));
                });

        when(ingredientRepository.save(any(StockIngredient.class)))
                .thenAnswer(invocation -> {
                    StockIngredient ing = invocation.getArgument(0);
                    dbStore.put(ing.getId(), ing);
                    return ing;
                });

        Double result = service.<Double>addCocktail("Margarita");

        assertNotNull(result);
        assertEquals(11000.0, result);

        ArgumentCaptor<Cocktail> cocktailCaptor = ArgumentCaptor.forClass(Cocktail.class);
        verify(cocktailRepository, times(1)).save(cocktailCaptor.capture());

        Cocktail savedCocktail = cocktailCaptor.getValue();
        assertEquals(11000, savedCocktail.getId());
        assertEquals("Margarita", savedCocktail.getName());
        assertEquals("Shake with ice and strain into glass.", savedCocktail.getInstructions());
        assertEquals(2, savedCocktail.getIngredients().size());
    }

    @Test
    @DisplayName("addCocktail: Returns 0.0 when recipe is not found on Spoonacular")
    void addCocktail_NotFound() {
        String searchJsonResponse = "{\"results\": []}";

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(searchJsonResponse);

        Double result = service.<Double>addCocktail("UnknownDrink");

        assertEquals(0.0, result);
        verifyNoInteractions(ingredientRepository);
        verifyNoInteractions(cocktailRepository);
    }

    @Test
    @DisplayName("removeCocktail: Deletes entity when ID exists")
    void removeCocktail_Success() {
        when(cocktailRepository.existsById(11000)).thenReturn(true);

        boolean result = service.removeCocktail(11000);

        assertTrue(result);
        verify(cocktailRepository, times(1)).deleteById(11000);
    }

    @Test
    @DisplayName("removeCocktail: Returns false when ID does not exist")
    void removeCocktail_NotFound() {
        when(cocktailRepository.existsById(99999)).thenReturn(false);

        boolean result = service.removeCocktail(99999);

        assertFalse(result);
        verify(cocktailRepository, never()).deleteById(anyInt());
    }
}