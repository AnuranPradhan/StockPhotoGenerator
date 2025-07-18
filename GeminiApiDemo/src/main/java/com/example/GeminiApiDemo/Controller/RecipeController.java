package com.example.GeminiApiDemo.Controller;

import com.example.GeminiApiDemo.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @GetMapping("/ask")
    public String RecipeResponse(@RequestParam String ingredients,
                                 @RequestParam(defaultValue = "any") String cuisine,
                                 @RequestParam(defaultValue = "none") String dietaryRestrictions){
        return  recipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
    }
}
