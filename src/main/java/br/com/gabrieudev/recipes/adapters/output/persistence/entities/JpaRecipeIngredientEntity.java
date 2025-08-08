package br.com.gabrieudev.recipes.adapters.output.persistence.entities;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.RecipeIngredient;
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
@Table(name = "recipe_ingredient")
public class JpaRecipeIngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private JpaRecipeEntity recipe;
    
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private JpaIngredientEntity ingredient;
    
    @Column(name = "quantity", nullable = false)
    private String quantity;

    public RecipeIngredient toDomainObj() {
        return new ModelMapper().map(this, RecipeIngredient.class);
    }

    public void update(RecipeIngredient recipeIngredient) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(recipeIngredient, this);
    }

    public static JpaRecipeIngredientEntity fromDomainObj(RecipeIngredient recipeIngredient) {
        return new ModelMapper().map(recipeIngredient, JpaRecipeIngredientEntity.class);
    }
}
