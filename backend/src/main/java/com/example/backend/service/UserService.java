package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.model.User;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public interface UserService {

    public User saveUser(String username, String email, String firstName, String lastName, Date dateOfBirth);

    public User saveUser(String username, String password, String email, String firstName, String lastName, Date dateOfBirth);

    public User saveUser(User user);

    public User findByUsername(String username);

    public User findByEmail(String email);

    public User findById(Integer id);

    public  List<User> userList();

    public User updateUser(User user, HashMap<String, String> request);

    public Role saveRole(Role role);

    public Role findUserRoleByName(String roleName);

    public void updateUserPassword(User user, String password);

    public void deleteUser(User user);



}
