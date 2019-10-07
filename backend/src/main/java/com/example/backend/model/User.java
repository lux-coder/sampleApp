package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class User implements Serializable {

    public Integer id;
    public String username;
    public String password;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Integer userRole;

    public User(){}

    public User(Integer id, String username, String password, String email, String firstName, String lastName, Date dateOfBirth, Integer userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.userRole = userRole;
    }

    public User(Integer id, String username, String email, String firstName, String lastName, Date dateOfBirth) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
}
