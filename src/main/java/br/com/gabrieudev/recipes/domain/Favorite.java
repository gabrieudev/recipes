package br.com.gabrieudev.recipes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Favorite {
    private UUID id;
    private User user;
    private Recipe recipe;
    private LocalDateTime favoritedAt;
    
    public UUID getId() {
      return id;
    }
    public void setId(UUID id) {
      this.id = id;
    }
    public User getUser() {
      return user;
    }
    public void setUser(User user) {
      this.user = user;
    }
    public Recipe getRecipe() {
      return recipe;
    }
    public void setRecipe(Recipe recipe) {
      this.recipe = recipe;
    }
    public LocalDateTime getFavoritedAt() {
      return favoritedAt;
    }
    public void setFavoritedAt(LocalDateTime favoritedAt) {
      this.favoritedAt = favoritedAt;
    }
    
    public Favorite(UUID id, User user, Recipe recipe, LocalDateTime favoritedAt) {
      this.id = id;
      this.user = user;
      this.recipe = recipe;
      this.favoritedAt = favoritedAt;
    }
    
    public Favorite() {
    }
}
