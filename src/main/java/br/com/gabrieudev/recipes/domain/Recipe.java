package br.com.gabrieudev.recipes.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Recipe {
    private UUID id;
    private String title;
    private String instructions;
    private Integer cookTimeMinutes;
    private Integer servings;
    private String imageUrl;
    private Category category;
    private LocalDateTime createdAt;

    public UUID getId() {
      return id;
    }
    public void setId(UUID id) {
      this.id = id;
    }
    public String getTitle() {
      return title;
    }
    public void setTitle(String title) {
      this.title = title;
    }
    public String getInstructions() {
      return instructions;
    }
    public void setInstructions(String instructions) {
      this.instructions = instructions;
    }
    public Integer getCookTimeMinutes() {
      return cookTimeMinutes;
    }
    public void setCookTimeMinutes(Integer cookTimeMinutes) {
      this.cookTimeMinutes = cookTimeMinutes;
    }
    public Integer getServings() {
      return servings;
    }
    public void setServings(Integer servings) {
      this.servings = servings;
    }
    public String getImageUrl() {
      return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }
    public Category getCategory() {
      return category;
    }
    public void setCategory(Category category) {
      this.category = category;
    }
    public LocalDateTime getCreatedAt() {
      return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }

    public Recipe(UUID id, String title, String instructions, Integer cookTimeMinutes,
    Integer servings, String imageUrl, Category category, LocalDateTime createdAt) {
      this.id = id;
      this.title = title;
      this.instructions = instructions;
      this.cookTimeMinutes = cookTimeMinutes;
      this.servings = servings;
      this.imageUrl = imageUrl;
      this.category = category;
      this.createdAt = createdAt;
    }
    
    public Recipe() {
    }
}
