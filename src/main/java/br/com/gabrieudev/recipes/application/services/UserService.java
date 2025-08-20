package br.com.gabrieudev.recipes.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.InvalidTokenException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.UserInputPort;
import br.com.gabrieudev.recipes.application.ports.output.AuthOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.CacheOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.EnvironmentOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.RoleOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserRoleOutputPort;
import br.com.gabrieudev.recipes.domain.Role;
import br.com.gabrieudev.recipes.domain.User;
import br.com.gabrieudev.recipes.domain.UserRole;

public class UserService implements UserInputPort {
    private final UserOutputPort userOutputPort;
    private final RoleOutputPort roleOutputPort;
    private final UserRoleOutputPort userRoleOutputPort;
    // private final EmailOutputPort emailOutputPort;
    private final AuthOutputPort authOutputPort;
    private final CacheOutputPort cacheOutputPort;
    private final EnvironmentOutputPort environmentOutputPort;

    public UserService(UserOutputPort userOutputPort, RoleOutputPort roleOutputPort,
            UserRoleOutputPort userRoleOutputPort, AuthOutputPort authOutputPort,
            CacheOutputPort cacheOutputPort, EnvironmentOutputPort environmentOutputPort) {
        this.userOutputPort = userOutputPort;
        this.roleOutputPort = roleOutputPort;
        this.userRoleOutputPort = userRoleOutputPort;
        // this.emailOutputPort = emailOutputPort;
        this.authOutputPort = authOutputPort;
        this.cacheOutputPort = cacheOutputPort;
        this.environmentOutputPort = environmentOutputPort;
    }

    @Override
    public User create(User user) {
        if (userOutputPort.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("Já existe um usuário com esse e-mail.");
        }

        user.setPassword(userOutputPort.hashPassword(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(Boolean.TRUE);

        User createdUser = userOutputPort.create(user)
                .orElseThrow(() -> new InternalErrorException("Erro ao criar usuário."));

        Role role = roleOutputPort.findByName("USER")
                .orElseThrow(() -> new InternalErrorException("Erro ao criar usuário."));

        userRoleOutputPort.create(new UserRole(null, createdUser, role))
                .orElseThrow(() -> new InternalErrorException("Erro ao criar usuário."));

        // sendConfirmationEmail(createdUser.getId());

        return createdUser;
    }

    @Override
    public void delete(UUID id) {
        if (!userOutputPort.existsById(id)) {
            throw new NotFoundException("Erro ao deletar usuário.");
        }

        List<UserRole> userRoles = userRoleOutputPort.findByUserId(id);

        userRoles.forEach(userRole -> userRoleOutputPort.delete(userRole.getId()));

        if (!userOutputPort.delete(id)) {
            throw new InternalErrorException("Erro ao deletar usuário.");
        }
    }

    @Override
    public List<User> findAll(String param, String email, Integer page, Integer size) {
        return userOutputPort.findAll(param, email, page, size);
    }

    @Override
    public User findById(UUID id) {
        return userOutputPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    @Override
    public User update(User user) {
        if (!userOutputPort.existsById(user.getId())) {
            throw new NotFoundException("Erro ao atualizar usuário.");
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userOutputPort.update(user)
                .orElseThrow(() -> new InternalErrorException("Erro ao atualizar usuário."));
    }

    @Override
    public void confirmEmail(UUID code) {
        String userId = cacheOutputPort.get("code:" + code.toString())
                .orElseThrow(() -> new NotFoundException("Código de confirmação inválido."));

        User user = findById(UUID.fromString(userId));

        user.setIsActive(Boolean.TRUE);
        user.setUpdatedAt(LocalDateTime.now());

        update(user);

        cacheOutputPort.delete("code:" + code.toString());

        // emailOutputPort.sendEmail(
        //         user.getEmail(),
        //         "Confirmação de e-mail",
        //         "Seu e-mail foi confirmado com sucesso.");
    }

    @Override
    public void sendConfirmationEmail(UUID id) {
        User user = findById(id);
        String code = UUID.randomUUID().toString();

        boolean isCached = cacheOutputPort.set("code:" + code, user.getId().toString(), 5);

        if (!isCached) {
            throw new InternalErrorException("Erro ao gerar código de confirmação.");
        }

        String url = environmentOutputPort.getApiBaseUrl() + "/users/confirm?code=" + code;

        String emailMessage = String.format("Olá %s, clique no link abaixo para confirmar seu e-mail: %s",
                user.getFirstName(), url);

        // boolean isSent = emailOutputPort.sendEmail(user.getEmail(), "Confirmação de e-mail", emailMessage);

        // if (!isSent) {
        //     throw new EmailException("Erro ao enviar e-mail de confirmação.");
        // }
    }

    @Override
    public User getMe(String token) {
        if (!authOutputPort.isValidToken(token)) {
            throw new InvalidTokenException("Token inválido.");
        }

        return authOutputPort.getUserByToken(token)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    @Override
    public void validateResetPassword(UUID code) {
        String userId = cacheOutputPort.get("code:" + code.toString())
                .orElseThrow(() -> new NotFoundException("Código de redefinição de senha inválido."));

        if (!userOutputPort.existsById(UUID.fromString(userId))) {
            throw new NotFoundException("Usuário não encontrado.");
        }
    }

    @Override
    public void sendResetPasswordEmail(String token) {
        User user = authOutputPort.getUserByToken(token)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        UUID code = UUID.randomUUID();

        cacheOutputPort.set("code:" + code.toString(), user.getId().toString(), 5);

        String url = environmentOutputPort.getApiBaseUrl() + "/users/validate-reset-password?code=" + code.toString();

        String emailMessage = String.format("Olá %s, clique no link abaixo para redefinir sua senha: %s",
                user.getFirstName(), url);

        // boolean isSent = emailOutputPort.sendEmail(user.getEmail(), "Redefinir senha", emailMessage);

        // if (!isSent) {
        //     throw new EmailException("Erro ao enviar e-mail de redefinição de senha.");
        // }
    }

    @Override
    public void resetPassword(UUID code, String password) {
        String userId = cacheOutputPort.get("code:" + code.toString())
                .orElseThrow(() -> new NotFoundException("Código de redefinição de senha inválido."));

        User user = userOutputPort.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        user.setPassword(userOutputPort.hashPassword(password));

        update(user);
        
        cacheOutputPort.delete("code:" + code.toString());

        // emailOutputPort.sendEmail(
        //         user.getEmail(),
        //         "Redefinição de senha",
        //         "Sua senha foi redefinida com sucesso.");
    }
}
