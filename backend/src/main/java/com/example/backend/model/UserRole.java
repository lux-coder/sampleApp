package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserRole implements Serializable {

    private long userRoleId;
    private User user;
    private Role role;

    public UserRole() { }

    public UserRole(long userRoleId, User user, Role role) {
        this.userRoleId = userRoleId;
        this.user = user;
        this.role = role;
    }

    public UserRole(User user, Role role){
        this.user = user;
        this.role = role;
    }
}
