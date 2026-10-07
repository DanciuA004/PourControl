package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.StockIngredient;
import com.mthree.pour_control.model.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    private StockServiceImpl stockService;

    @BeforeEach
    void setUp() {
        stockService = new StockServiceImpl(ingredientRepository);
    }

    @Test
    @DisplayName("getAllIngredients returns list of all ingredients from repository")
    void getAllIngredients_ReturnsList() {
        StockIngredient tequila = new StockIngredient(1, "Tequila", new BigDecimal("750.00"), new BigDecimal("1000.00"));
        StockIngredient vodka = new StockIngredient(2, "Vodka", new BigDecimal("500.00"), new BigDecimal("1000.00"));

        when(ingredientRepository.findAll()).thenReturn(List.of(tequila, vodka));

        List<StockIngredient> result = stockService.getAllIngredients();

        assertThat(result).hasSize(2).containsExactly(tequila, vodka);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllIngredients returns empty list when repository is empty")
    void getAllIngredients_ReturnsEmptyList() {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        List<StockIngredient> result = stockService.getAllIngredients();

        assertThat(result).isEmpty();
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("UpdateStockQuantity successfully updates mlInStock and saves ingredient")
    void updateStockQuantity_Success() {
        int ingredientId = 1;
        StockIngredient existingIngredient = new StockIngredient(ingredientId, "Rum", new BigDecimal("100.00"), new BigDecimal("1000.00"));

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(existingIngredient));

        stockService.UpdateStockQuantity(ingredientId, "750.50");

        ArgumentCaptor<StockIngredient> captor = ArgumentCaptor.forClass(StockIngredient.class);
        verify(ingredientRepository, times(1)).save(captor.capture());

        StockIngredient savedIngredient = captor.getValue();
        assertThat(savedIngredient.getMlInStock()).isEqualTo(new BigDecimal("750.50"));
    }

    @Test
    @DisplayName("UpdateStockQuantity throws IllegalArgumentException when ingredient ID is not found")
    void updateStockQuantity_NotFound_ThrowsException() {
        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.UpdateStockQuantity(999, "500.00"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ingredient not found: 999");

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    @DisplayName("UpdateStockQuantity throws NumberFormatException when quantity string is invalid")
    void updateStockQuantity_InvalidQuantityFormat_ThrowsException() {
        StockIngredient existingIngredient = new StockIngredient(1, "Gin", BigDecimal.ZERO, BigDecimal.ZERO);
        when(ingredientRepository.findById(1)).thenReturn(Optional.of(existingIngredient));

        assertThatThrownBy(() -> stockService.UpdateStockQuantity(1, "invalid_number"))
                .isInstanceOf(NumberFormatException.class);

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    @DisplayName("UpdateStockTargetValue successfully updates mlTarget and saves ingredient")
    void updateStockTargetValue_Success() {
        int ingredientId = 2;
        StockIngredient existingIngredient = new StockIngredient(ingredientId, "Whiskey", new BigDecimal("500.00"), new BigDecimal("1000.00"));

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(existingIngredient));

        stockService.UpdateStockTargetValue(ingredientId, "2000.00");

        ArgumentCaptor<StockIngredient> captor = ArgumentCaptor.forClass(StockIngredient.class);
        verify(ingredientRepository, times(1)).save(captor.capture());

        StockIngredient savedIngredient = captor.getValue();
        assertThat(savedIngredient.getMlTarget()).isEqualTo(new BigDecimal("2000.00"));
    }

    @Test
    @DisplayName("UpdateStockTargetValue throws IllegalArgumentException when ingredient ID is not found")
    void updateStockTargetValue_NotFound_ThrowsException() {
        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.UpdateStockTargetValue(404, "1000.00"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ingredient not found: 404");

        verify(ingredientRepository, never()).save(any());
    }
}