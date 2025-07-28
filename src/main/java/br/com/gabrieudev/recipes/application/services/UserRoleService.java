package br.com.gabrieudev.recipes.application.services;

import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.UserRoleInputPort;
import br.com.gabrieudev.recipes.application.ports.output.RoleOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserRoleOutputPort;
import br.com.gabrieudev.recipes.domain.Role;
import br.com.gabrieudev.recipes.domain.User;
import br.com.gabrieudev.recipes.domain.UserRole;

public class UserRoleService implements UserRoleInputPort {
    private final UserRoleOutputPort userRoleOutputPort;
    private final RoleOutputPort roleOutputPort;
    private final UserOutputPort userOutputPort;

    public UserRoleService(UserRoleOutputPort userRoleOutputPort, RoleOutputPort roleOutputPort, UserOutputPort userOutputPort) {
        this.userRoleOutputPort = userRoleOutputPort;
        this.roleOutputPort = roleOutputPort;
        this.userOutputPort = userOutputPort;
    }

    @Override
    public UserRole assign(UUID userId, UUID roleId) {
        Role roleToAssign = roleOutputPort.findById(roleId)
            .orElseThrow(() -> new NotFoundException("Papel não encontrado."));
            
        User user = userOutputPort.findById(userId)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        UserRole userRole = new UserRole(null, user, roleToAssign);

        if (userRoleOutputPort.existsByUserIdAndRoleId(userRole.getUser().getId(), userRole.getRole().getId())) {
            throw new AlreadyExistsException("Usuário já possui esse papel.");
        }

        return userRoleOutputPort.create(userRole)
                .orElseThrow(() -> new InternalErrorException("Erro ao atribuir role ao usuário."));
    }

    @Override
    public void unassign(UUID userId, UUID roleId) {
        User user = userOutputPort.findById(userId)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        Role role = roleOutputPort.findById(roleId)
            .orElseThrow(() -> new NotFoundException("Papel não encontrado."));
        
        UserRole userRole = userRoleOutputPort.findByUserIdAndRoleId(user.getId(), role.getId())
            .orElseThrow(() -> new NotFoundException("Usuário ou papel não encontrado."));

        if (!userRoleOutputPort.delete(userRole.getId())) {
            throw new InternalErrorException("Erro ao desatribuir papel ao usuário.");
        }
    }
}
