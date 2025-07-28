package br.com.gabrieudev.recipes.config.database;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.entities.JpaUserRoleEntity;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaRoleRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRepository;
import br.com.gabrieudev.recipes.adapters.output.persistence.repositories.jpa.JpaUserRoleRepository;

@Configuration
@Profile("dev")
public class DatabaseLoader implements CommandLineRunner {
    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaUserRoleRepository jpaUserRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DatabaseLoader(JpaUserRepository jpaUserRepository, JpaRoleRepository jpaRoleRepository,
            JpaUserRoleRepository jpaUserRoleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaRoleRepository = jpaRoleRepository;
        this.jpaUserRoleRepository = jpaUserRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!jpaUserRepository.existsByEmail("admin@gmail.com")) {
            JpaUserEntity user = new JpaUserEntity();
            user.setFirstName("admin");
            user.setLastName("admin");
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("adminpassword"));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setIsActive(Boolean.TRUE);

            JpaRoleEntity adminRole = new JpaRoleEntity();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Role de administrador");
            JpaRoleEntity userRole = new JpaRoleEntity();
            userRole.setName("USER");
            userRole.setDescription("Role de usuário comum");

            JpaUserRoleEntity adminUsersRoles = new JpaUserRoleEntity();
            adminUsersRoles.setRole(adminRole);
            adminUsersRoles.setUser(user);
            JpaUserRoleEntity userUsersRoles = new JpaUserRoleEntity();
            userUsersRoles.setRole(userRole);
            userUsersRoles.setUser(user);

            jpaUserRepository.save(user);
            jpaRoleRepository.save(userRole);
            jpaRoleRepository.save(adminRole);
            jpaUserRoleRepository.save(userUsersRoles);
            jpaUserRoleRepository.save(adminUsersRoles);
        }
    }
}
