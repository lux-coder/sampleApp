package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Role implements Serializable {

    private Integer roleId;
    private String name;

    public Role() { }

    public Role(Integer roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }
}
