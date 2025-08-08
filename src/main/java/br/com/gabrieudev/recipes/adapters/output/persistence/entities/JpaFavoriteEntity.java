package br.com.gabrieudev.recipes.adapters.output.persistence.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.Favorite;
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
@Table(name = "categories")
public class JpaFavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JpaUserEntity user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private JpaRecipeEntity recipe;

    @Column(name = "favorited_at", nullable = false)
    private LocalDateTime favoritedAt;

    public Favorite toDomainObj() {
        return new ModelMapper().map(this, Favorite.class);
    }

    public static JpaFavoriteEntity fromDomainObj(Favorite favorite) {
        return new ModelMapper().map(favorite, JpaFavoriteEntity.class);
    }

    public void update(Favorite favorite) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(favorite, this);
    }
}
