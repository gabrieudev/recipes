package br.com.gabrieudev.recipes.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.gabrieudev.recipes.application.ports.input.AuthInputPort;
import br.com.gabrieudev.recipes.application.ports.input.CategoryInputPort;
import br.com.gabrieudev.recipes.application.ports.input.FavoriteInputPort;
import br.com.gabrieudev.recipes.application.ports.input.IngredientInputPort;
import br.com.gabrieudev.recipes.application.ports.input.RecipeIngredientInputPort;
import br.com.gabrieudev.recipes.application.ports.input.RecipeinputPort;
import br.com.gabrieudev.recipes.application.ports.input.RoleInputPort;
import br.com.gabrieudev.recipes.application.ports.input.UserInputPort;
import br.com.gabrieudev.recipes.application.ports.input.UserRoleInputPort;
import br.com.gabrieudev.recipes.application.ports.output.AuthOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.CacheOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.CategoryOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.EnvironmentOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.FavoriteOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.IngredientOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.RecipeIngredientOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.RecipeOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.RoleOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserRoleOutputPort;
import br.com.gabrieudev.recipes.application.services.AuthService;
import br.com.gabrieudev.recipes.application.services.CategoryService;
import br.com.gabrieudev.recipes.application.services.FavoriteService;
import br.com.gabrieudev.recipes.application.services.IngredientService;
import br.com.gabrieudev.recipes.application.services.RecipeIngredientService;
import br.com.gabrieudev.recipes.application.services.RecipeService;
import br.com.gabrieudev.recipes.application.services.RoleService;
import br.com.gabrieudev.recipes.application.services.UserRoleService;
import br.com.gabrieudev.recipes.application.services.UserService;

@Configuration
public class BeansConfig {
    @Bean
    UserService userService(UserOutputPort userOutputPort, RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort, AuthOutputPort authOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        return new UserService(userOutputPort, roleOutputPort, userRoleOutputPort, authOutputPort, cacheOutputPort, environmentOutputPort);
    }

    @Bean
    UserInputPort userInputPort(UserOutputPort userOutputPort, RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort, AuthOutputPort authOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        return new UserService(userOutputPort, roleOutputPort, userRoleOutputPort, authOutputPort, cacheOutputPort, environmentOutputPort);
    }

    @Bean
    AuthService authService(AuthOutputPort authOutputPort, UserOutputPort userOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        return new AuthService(authOutputPort, userOutputPort, cacheOutputPort, environmentOutputPort);
    }

    @Bean
    AuthInputPort authInputPort(AuthOutputPort authOutputPort, UserOutputPort userOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        return new AuthService(authOutputPort, userOutputPort, cacheOutputPort, environmentOutputPort);
    }

    @Bean
    RoleService roleService(RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort) {
        return new RoleService(roleOutputPort, userRoleOutputPort);
    }

    @Bean
    RoleInputPort roleInputPort(RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort) {
        return new RoleService(roleOutputPort, userRoleOutputPort);
    }

    @Bean
    UserRoleService userRoleService(UserRoleOutputPort userRoleOutputPort, RoleOutputPort roleOutputPort, UserOutputPort userOutputPort) {
        return new UserRoleService(userRoleOutputPort, roleOutputPort, userOutputPort);
    }

    @Bean
    UserRoleInputPort userRoleInputPort(UserRoleOutputPort userRoleOutputPort, RoleOutputPort roleOutputPort, UserOutputPort userOutputPort) {
        return new UserRoleService(userRoleOutputPort, roleOutputPort, userOutputPort);
    }

    @Bean
    CategoryService categoryService(CategoryOutputPort categoryOutputPort) {
        return new CategoryService(categoryOutputPort);
    }

    @Bean
    CategoryInputPort categoryInputPort(CategoryOutputPort categoryOutputPort) {
        return new CategoryService(categoryOutputPort);
    }

    @Bean
    FavoriteService favoriteService(FavoriteOutputPort favoriteOutputPort) {
        return new FavoriteService(favoriteOutputPort);
    }

    @Bean
    FavoriteInputPort favoriteInputPort(FavoriteOutputPort favoriteOutputPort) {
        return new FavoriteService(favoriteOutputPort);
    }

    @Bean
    IngredientService ingredientService(IngredientOutputPort ingredientOutputPort) {
        return new IngredientService(ingredientOutputPort);
    }

    @Bean
    IngredientInputPort ingredientInputPort(IngredientOutputPort ingredientOutputPort) {
        return new IngredientService(ingredientOutputPort);
    }

    @Bean
    RecipeService recipeService(RecipeOutputPort recipeOutputPort, RecipeIngredientOutputPort recipeIngredientOutputPort) {
        return new RecipeService(recipeOutputPort, recipeIngredientOutputPort);
    }

    @Bean
    RecipeinputPort recipeInputPort(RecipeOutputPort recipeOutputPort, RecipeIngredientOutputPort recipeIngredientOutputPort) {
        return new RecipeService(recipeOutputPort, recipeIngredientOutputPort);
    }

    @Bean
    RecipeIngredientService recipeIngredientService(RecipeIngredientOutputPort recipeIngredientOutputPort) {
        return new RecipeIngredientService(recipeIngredientOutputPort);
    }

    @Bean
    RecipeIngredientInputPort recipeIngredientInputPort(RecipeIngredientOutputPort recipeIngredientOutputPort) {
        return new RecipeIngredientService(recipeIngredientOutputPort);
    }
}
