package com.example.backend.model.dao;

import com.example.backend.model.Role;

import java.sql.SQLException;

public interface RoleDaoRepository {

    public Role findByRoleName(String role) throws SQLException;

    public Role findById(Integer role_id);

    public Role save(Role role);
}
