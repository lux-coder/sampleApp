package com.example.backend.model.dao;

import com.example.backend.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        User user = new User();
        user.setId(Integer.parseInt(String.valueOf(resultSet.getInt("id"))));
        user.setUsername(String.valueOf(resultSet.getString("username")));
        user.setPassword(String.valueOf(resultSet.getString("password")));
        user.setFirstName(String.valueOf(resultSet.getString("firstName")));
        user.setLastName(String.valueOf(resultSet.getString("lastName")));
        user.setLastName(String.valueOf(resultSet.getString("lastName")));
        user.setDateOfBirth(resultSet.getDate("dateOfBirth"));

        return user;
    }
}
