package com.mthree.pour_control.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mthree.pour_control.dto.*;
import com.mthree.pour_control.model.CocktailRepository;
import com.mthree.pour_control.model.DailyAuditRepository;
import com.mthree.pour_control.model.DailySaleRepository;
import com.mthree.pour_control.model.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService{

    private final DailySaleRepository dailySaleRepository;
    private final DailyAuditRepository dailyAuditRepository;
    private final CocktailRepository cocktailRepository;
    private final IngredientRepository ingredientRepository;

    public AuditServiceImpl(DailySaleRepository dailySaleRepository,
                            DailyAuditRepository dailyAuditRepository,
                            CocktailRepository cocktailRepository,
                            IngredientRepository ingredientRepository) {
        this.dailySaleRepository = dailySaleRepository;
        this.dailyAuditRepository = dailyAuditRepository;
        this.cocktailRepository = cocktailRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public void processCloseout(DailyCloseoutRequest request) {
        LocalDate today = LocalDate.now();

        if (request.getCocktailCloseout() != null) {
            List<DailySale> salesToSave = new ArrayList<>();

            request.getCocktailCloseout().forEach((cocktailId, qtySold) -> {
                Cocktail cocktail = cocktailRepository.findById(cocktailId)
                        .orElseThrow(() -> new IllegalArgumentException("Cocktail not found: " + cocktailId));

                DailySale sale = new DailySale();
                sale.setCocktailId(cocktail);
                sale.setQuantitySold(qtySold);
                sale.setDate(today);
                salesToSave.add(sale);
            });

            batchAddDailySales(salesToSave);
        }

        // Process Stock Level Updates
        if (request.getStockCloseout() != null) {
            List<StockIngredient> stockToSave = new ArrayList<>();
            List<DailyAudit> auditsToSave = new ArrayList<>();

            request.getStockCloseout().forEach((stockId, newVolume) -> {
                StockIngredient ingredient = ingredientRepository.findById(stockId)
                        .orElseThrow(() -> new IllegalArgumentException("Stock item not found: " + stockId));

                DailyAudit audit = new DailyAudit();
                audit.setDate(today);
                audit.setStartMl(ingredient.getMlInStock());
                audit.setEndMl(BigDecimal.valueOf(newVolume));
                audit.setIngredientId(ingredient);
                auditsToSave.add(audit);



                ingredient.setMlInStock(BigDecimal.valueOf(newVolume));
                stockToSave.add(ingredient);
            });

            batchAddDailyAudit(auditsToSave);
            ingredientRepository.saveAll(stockToSave);
        }
    }


    @Transactional
    private List<DailyAudit> batchAddDailyAudit(List<DailyAudit> dailyAudits) {
        return dailyAuditRepository.saveAll(dailyAudits);
    }


    @Transactional
    private List<DailySale> batchAddDailySales(List<DailySale> dailySales) {
        return dailySaleRepository.saveAll(dailySales);
    }
}
