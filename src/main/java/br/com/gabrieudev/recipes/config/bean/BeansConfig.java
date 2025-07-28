package br.com.gabrieudev.recipes.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.gabrieudev.recipes.application.ports.input.AuthInputPort;
import br.com.gabrieudev.recipes.application.ports.input.RoleInputPort;
import br.com.gabrieudev.recipes.application.ports.input.UserInputPort;
import br.com.gabrieudev.recipes.application.ports.input.UserRoleInputPort;
import br.com.gabrieudev.recipes.application.ports.output.AuthOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.CacheOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.EmailOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.EnvironmentOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.RoleOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserRoleOutputPort;
import br.com.gabrieudev.recipes.application.services.AuthService;
import br.com.gabrieudev.recipes.application.services.RoleService;
import br.com.gabrieudev.recipes.application.services.UserRoleService;
import br.com.gabrieudev.recipes.application.services.UserService;

@Configuration
public class BeansConfig {
    @Bean
    UserService userService(UserOutputPort userOutputPort, RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort, EmailOutputPort emailOutputPort, AuthOutputPort authOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        return new UserService(userOutputPort, roleOutputPort, userRoleOutputPort, emailOutputPort, authOutputPort, cacheOutputPort, environmentOutputPort);
    }

    @Bean
    UserInputPort userInputPort(UserOutputPort userOutputPort, RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort, EmailOutputPort emailOutputPort, AuthOutputPort authOutputPort, CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        return new UserService(userOutputPort, roleOutputPort, userRoleOutputPort, emailOutputPort, authOutputPort, cacheOutputPort, environmentOutputPort);
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
}
