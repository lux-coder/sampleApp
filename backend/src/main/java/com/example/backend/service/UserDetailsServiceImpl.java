package com.example.backend.service;

//import com.example.backend.model.User;
import com.example.backend.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, SQLException {
        logger.info("In loadUserByUsername");
        com.example.backend.model.User user = userService.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username " + username + "not found!");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //UserRole userRole = user.getUserRoles();

        //authorities.add(new SimpleGrantedAuthority(userRole.toString()));

        //return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
