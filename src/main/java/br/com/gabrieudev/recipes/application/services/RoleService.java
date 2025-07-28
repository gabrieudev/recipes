package br.com.gabrieudev.recipes.application.services;

import java.util.List;
import java.util.UUID;

import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.BusinessRuleException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;
import br.com.gabrieudev.recipes.application.ports.input.RoleInputPort;
import br.com.gabrieudev.recipes.application.ports.output.RoleOutputPort;
import br.com.gabrieudev.recipes.application.ports.output.UserRoleOutputPort;
import br.com.gabrieudev.recipes.domain.Role;

public class RoleService implements RoleInputPort {
    private final RoleOutputPort roleOutputPort;
    private final UserRoleOutputPort userRoleOutputPort;

    public RoleService(RoleOutputPort roleOutputPort, UserRoleOutputPort userRoleOutputPort) {
        this.roleOutputPort = roleOutputPort;
        this.userRoleOutputPort = userRoleOutputPort;
    }

    @Override
    public Role create(Role role) {
        if (roleOutputPort.existsByName(role.getName())) {
            throw new AlreadyExistsException("Já existe um papel com esse nome.");
        }

        return roleOutputPort.create(role)
                .orElseThrow(() -> new InternalErrorException("Erro ao criar papel."));
    }

    @Override
    public void delete(UUID id) {
        if (!roleOutputPort.existsById(id)) {
            throw new NotFoundException("Papel não encontrado.");
        }

        if (userRoleOutputPort.existsByRoleId(id)) {
            throw new BusinessRuleException("Este papel possui usuários vinculados.");
        }

        if (!roleOutputPort.delete(id)) {
            throw new InternalErrorException("Erro ao deletar papel.");
        }
    }

    @Override
    public List<Role> findAll(UUID userId, String name) {
        return roleOutputPort.findAll(userId, name);
    }

    @Override
    public Role findById(UUID id) {
        return roleOutputPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Papel não encontrado."));
    }

    @Override
    public Role update(Role role) {
        Role roleToUpdate = findById(role.getId());

        if (!roleToUpdate.getName().equals(role.getName()) && roleOutputPort.existsByName(role.getName())) {
            throw new AlreadyExistsException("Já existe um papel com esse nome.");
        }

        return roleOutputPort.update(role)
                .orElseThrow(() -> new InternalErrorException("Erro ao atualizar papel."));
    }
}
