//package com.example.backend.service;
//
//import com.example.backend.model.Role;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, SQLException {
//        logger.info("In loadUserByUsername from userDetailService");
//        com.example.backend.model.User user = userService.findByUsername(username);
//        if(user.getId() == null){
//            throw new UsernameNotFoundException("Username " + username + "not found!");
//        }
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        Integer role_id = user.getUserRole();
//        Role role = userService.findRoleById(role_id);
//        logger.info("GOt role {}", role.getName());
//        authorities.add(new SimpleGrantedAuthority(role.getName()));
//        //return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
//        return new User(user.getUsername(), user.getPassword(), authorities);
//    }
//}
