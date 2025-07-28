package br.com.gabrieudev.recipes.adapters.output.persistence.entities;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.gabrieudev.recipes.domain.UserRole;
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
@Table(name = "users_roles")
public class JpaUserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JpaUserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private JpaRoleEntity role;

    public static JpaUserRoleEntity from(UserRole userRole) {
        return new ModelMapper().map(userRole, JpaUserRoleEntity.class);
    }

    public UserRole toDomainObj() {
        return new ModelMapper().map(this, UserRole.class);
    }
}
