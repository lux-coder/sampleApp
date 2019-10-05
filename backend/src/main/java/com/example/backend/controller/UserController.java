package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/list")
    public ResponseEntity<?> getUsersList(){
        logger.info("In /list endpoint");
        List<User> users = userService.userList();
        if (users.isEmpty()){
            return new ResponseEntity<>("No users found!", HttpStatus.OK);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) throws SQLException {
        logger.info("In getUserInfo endpoint");
        User user = userService.findByUsername(username);
        if (user == null){
            return new ResponseEntity<>("No user with that username!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody HashMap<String, String> request) throws SQLException {
        logger.info("In /register endpoint");
        String username = request.get("username");
        logger.info("Check if USERNAME exists: {}", username);
        if (userService.findByUsername(username) != null){
            return new ResponseEntity<>("Username exists!", HttpStatus.CONFLICT);
        }
        logger.info("Setting user data before try user save");

        String email = request.get("email");
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        Date dateOfBirth = java.sql.Date.valueOf(request.get("dateOfBirth"));
        //Integer userRole = Integer.valueOf(request.get("userRole"));
        Integer userRole = 1;

        try {
            User user = userService.saveUser(username, email, firstName, lastName, dateOfBirth, userRole);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error occurred during registration!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody HashMap<String, String> request){
        logger.info("In updateProfile endpoint");
        String id = request.get("id");
        User user = userService.findById(Integer.parseInt(id));
        if(user == null){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
        try {
            userService.updateUser(user, request);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e){
            logger.error("Exception due to {} with stacktrace {}", e.getMessage(), e);
            return new ResponseEntity<>("Error occurred while updating profile!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody HashMap<String, String> request) throws SQLException {
        logger.info("In changePassword endpoint");
        String username = request.get("username");
        User user = userService.findByUsername(username);
        if (user == null){
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        }
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        if (!newPassword.equals(confirmPassword)) {
            return new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);
        }
        String password = user.getPassword();
        try {
            if (newPassword != null && !newPassword.isEmpty() && !StringUtils.isEmpty(newPassword)) {
                if (bCryptPasswordEncoder.matches(currentPassword, password)) {
                    userService.updateUserPassword(user, newPassword);
                }
            } else {
                return new ResponseEntity<>("Current password incorrect", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Password changed successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody HashMap<String, String> request) throws SQLException {
        logger.info("In deleteUser endpoint");
        String username = request.get("username");
        User user = userService.findByUsername(username);
        userService.deleteUser(user);
        return new ResponseEntity<String>("User deleted!", HttpStatus.OK);
    }
}
