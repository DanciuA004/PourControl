package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.Cocktail;
import com.mthree.pour_control.service.CocktailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cocktails")
@CrossOrigin(origins = "*")
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
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

    @GetMapping
    public List<Cocktail> getAllCocktails() {
        return cocktailService.getAllCocktails();

    }
}