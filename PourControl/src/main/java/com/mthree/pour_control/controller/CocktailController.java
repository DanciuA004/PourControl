package com.mthree.pour_control.controller;

import com.mthree.pour_control.service.CocktailIngestionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {
    private final CocktailIngestionService cocktailService;

    public CocktailController(CocktailIngestionService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @PostMapping
    public Object addCocktail(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        return cocktailService.addCocktail(name);
    }

    @DeleteMapping("/{id}")
    public boolean removeCocktail(@PathVariable Integer id) {
        return cocktailService.removeCocktail(id);
    }
}