package br.com.gabrieudev.recipes.adapters.output.persistence.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.Recipe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "recipes")
public class JpaRecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "instructions", nullable = false)
    private String instructions;
    
    @Column(name = "cook_time_minutes", nullable = false)
    private Integer cookTimeMinutes;
    
    @Column(name = "servings", nullable = false)
    private Integer servings;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private JpaCategoryEntity category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Recipe toDomainObj() {
        return new ModelMapper().map(this, Recipe.class);
    }

    public static JpaRecipeEntity fromDomainObj(Recipe recipe) {
        return new ModelMapper().map(recipe, JpaRecipeEntity.class);
    }

    public void update(Recipe recipe) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(recipe, this);
    }
}
