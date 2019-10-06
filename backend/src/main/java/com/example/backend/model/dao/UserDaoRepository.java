package com.example.backend.model.dao;

import com.example.backend.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDaoRepository {

    public void create(User user);

    public User register(User user);

    public void update(User user);

    public void updatePassword(Integer id, String encryptedPassword);

    public void delete(int id);

    public User findById(int id);

    public List<User> findAll();

    public User getByUsername(String username) throws SQLException;

    public User getByEmail(String email);
}
