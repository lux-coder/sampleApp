package com.example.backend.model.dao;

import com.example.backend.model.Role;

public interface RoleDaoRepository {

    public Role findByRoleName(String role);

    public Role save(Role role);
}
