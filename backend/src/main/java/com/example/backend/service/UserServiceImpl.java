package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserRole;
import com.example.backend.model.dao.RoleDaoRepository;
import com.example.backend.model.dao.UserDaoRepository;
import com.example.backend.utility.EmailConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    private UserDaoRepository userDaoRepository;

    @Autowired
    private RoleDaoRepository roleDaoRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public User saveUser(String username, String email, String firstName, String lastName, Date dateOfBirth, Integer userRole) throws SQLException {
        logger.info("In saveUser");
        String password = RandomStringUtils.randomAlphanumeric(8);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        logger.info("password is {}", password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        //TODO Check for role in DB table role
        user.setUserRole(userRole);
        userDaoRepository.create(user);

        //javaMailSender.send(emailConstructor.constructNewUserEmail(user, password));
        return user;
    }

    @Override
    public User saveUser(String username, String password, String email, String firstName, String lastName, Date dateOfBirth) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        logger.info("In saveUser simple");
        userDaoRepository.create(user);
        return user;
    }

    @Override
    public User findByUsername(String username)  {
        logger.info("In findByUsername");
        User user = new User();
        try {
            user = userDaoRepository.getByUsername(username);
        } catch (SQLException e) {
            logger.info("No user with that name");
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        logger.info("In findByEmail");
        return userDaoRepository.getByEmail(email);
    }

    @Override
    public User findById(Integer id) {
        logger.info("In findById");
        return userDaoRepository.findById(id);
    }

    @Override
    public List<User> userList() {
        logger.info("In userList");
        return userDaoRepository.findAll();
    }

    @Override
    public User updateUser(User user, HashMap<String, String> request) {
        logger.info("In updateUser");
        user.setEmail(request.get("email"));
        user.setFirstName(request.get("firstName"));
        user.setLastName(request.get("lastName"));
        user.setDateOfBirth(Date.valueOf(request.get("dateOfBirth")));
        userDaoRepository.update(user);
        return user;
    }

    @Override
    public Role saveRole(Role role) {
        logger.info("In saveRole");
        return roleDaoRepository.save(role);
    }

    @Override
    public Role findUserRoleByName(String roleName) throws SQLException {
        logger.info("In findUserRoleByName");
        return roleDaoRepository.findByRoleName(roleName);
    }

    @Override
    public Role findRoleById(Integer role_id) {
        logger.info("In findRoleById");
        if (roleDaoRepository.findById(role_id) != null){
            return roleDaoRepository.findById(role_id);
        }else {
            logger.info("No such role");
            return null;
        }
    }

    @Override
    public void updateUserPassword(User user, String password) {
        logger.info("In updateUserPassword");
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userDaoRepository.update(user);
    }

    @Override
    public void deleteUser(User user) {
        logger.info("In deleteUser");
        userDaoRepository.delete(user.getId());

    }
}