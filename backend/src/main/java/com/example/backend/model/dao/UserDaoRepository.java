package com.example.backend.model.dao;

import com.example.backend.model.User;

import java.util.List;

public interface UserDaoRepository {

    public void create(User user);

    public void update(User user);

    public void delete(int id);

    public User findById(int id);

    public List<User> findAll();

    public User getByUsername(String username);

    public User getByEmail(String email);
}
