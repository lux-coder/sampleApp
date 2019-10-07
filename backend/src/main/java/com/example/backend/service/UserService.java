package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.model.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface UserService {

    public User saveUser(String username, String email, String firstName, String lastName, Date dateOfBirth, Integer userRole) throws SQLException;

    public User saveUser(String username, String password, String email, String firstName, String lastName, Date dateOfBirth);

    public User saveUser(String username, String email, String firstName);

    public User saveUser(User user);

    public User findByUsername(String username);

    public User findByEmail(String email);

    public User findById(Integer id);

    public  List<User> userList();

    public User updateUser(User user, HashMap<String, String> request);

    public Role saveRole(Role role);

    public Role findUserRoleByName(String roleName) throws SQLException;

    public Role findRoleById(Integer role_id);

    public void updateUserPassword(User user, String password);

    public void deleteUser(User user);



}
