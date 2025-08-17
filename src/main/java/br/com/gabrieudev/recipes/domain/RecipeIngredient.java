package br.com.gabrieudev.recipes.domain;

import java.util.UUID;

public class RecipeIngredient {
    private UUID id;
    private Recipe recipe;
    private Ingredient ingredient;
    private String quantity;
    
    public Recipe getRecipe() {
      return recipe;
    }
    
    public void setRecipe(Recipe recipe) {
      this.recipe = recipe;
    }
    public Ingredient getIngredient() {
      return ingredient;
    }
    public void setIngredient(Ingredient ingredient) {
      this.ingredient = ingredient;
    }
    public String getQuantity() {
      return quantity;
    }
    public void setQuantity(String quantity) {
      this.quantity = quantity;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public RecipeIngredient(UUID id, Recipe recipe, Ingredient ingredient, String quantity) {
        this.id = id;
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public RecipeIngredient() {
    }
}
