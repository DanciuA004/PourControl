package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.*;
import com.mthree.pour_control.model.CocktailRepository;
import com.mthree.pour_control.model.DailyAuditRepository;
import com.mthree.pour_control.model.DailySaleRepository;
import com.mthree.pour_control.model.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

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

        // Process Cocktail Sales (Upsert)
        if (request.getCocktailCloseout() != null && !request.getCocktailCloseout().isEmpty()) {
            List<DailySale> salesToSave = new ArrayList<>();

            request.getCocktailCloseout().forEach((cocktailId, qtySold) -> {

                //first confirm the existence of the cocktail we're adding a sale record for
                Cocktail cocktail = cocktailRepository.findById(cocktailId)
                        .orElseThrow(() -> new IllegalArgumentException("Cocktail not found with ID: " + cocktailId));

                //find a daily sale entry for this cocktail for toady
                DailySale sale = dailySaleRepository.findByCocktailIdAndDate(cocktail, today)
                        //if no pre-existing entry, then create a new daily sale object with the values we want to save
                        .orElseGet(() -> {
                            DailySale newSale = new DailySale();
                            newSale.setCocktailId(cocktail);
                            newSale.setDate(today);
                            return newSale;
                        });

                sale.setQuantitySold(qtySold);
                salesToSave.add(sale);
            });

            dailySaleRepository.saveAll(salesToSave);
        }

        // Process Stock Audits & Update Inventory (Upsert)
        if (request.getStockCloseout() != null && !request.getStockCloseout().isEmpty()) {
            List<StockIngredient> stockToSave = new ArrayList<>();
            List<DailyAudit> auditsToSave = new ArrayList<>();

            request.getStockCloseout().forEach((stockId, newVolume) -> {
                StockIngredient ingredient = ingredientRepository.findById(stockId)
                        .orElseThrow(() -> new IllegalArgumentException("Stock item not found with ID: " + stockId));

                BigDecimal updatedVolume = BigDecimal.valueOf(newVolume);

                // Preserve true starting baseline before updating stock level
                BigDecimal initialStartMl = ingredient.getMlInStock();

                DailyAudit audit = dailyAuditRepository.findByIngredientAndDate(ingredient, today)
                        .orElseGet(() -> {
                            DailyAudit newAudit = new DailyAudit();
                            newAudit.setDate(today);
                            newAudit.setIngredient(ingredient);
                            newAudit.setStartMl(initialStartMl); // Captured prior to stock updates
                            return newAudit;
                        });

                // Update closing volume for today's audit
                audit.setEndMl(updatedVolume);
                auditsToSave.add(audit);

                // Update live stock inventory level
                ingredient.setMlInStock(updatedVolume);
                stockToSave.add(ingredient);
            });

            dailyAuditRepository.saveAll(auditsToSave);
            ingredientRepository.saveAll(stockToSave);
        }
    }
}