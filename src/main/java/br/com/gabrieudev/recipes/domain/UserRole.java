package br.com.gabrieudev.recipes.domain;

import java.io.Serializable;
import java.util.UUID;

public class UserRole implements Serializable {
    private UUID id;
    private User user;
    private Role role;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    
    public UserRole(UUID id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public UserRole() {
    }
}
