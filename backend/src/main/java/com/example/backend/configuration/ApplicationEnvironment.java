package com.example.backend.configuration;

import com.example.backend.model.dao.RoleDaoRepository;
import com.example.backend.model.dao.RoleDaoRepositoryImpl;
import com.example.backend.model.dao.UserDaoRepository;
import com.example.backend.model.dao.UserDaoRepositoryImpl;
import com.example.backend.service.UserDetailsService;
import com.example.backend.service.UserDetailsServiceImpl;
import com.example.backend.utility.EmailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan
public class ApplicationEnvironment {

    public static final long EXPIRATION_TIME = 432_000_00;;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_TYPE = "Authorization";
    public static final String SECRET = "secretKey12345678";
    public static final String CLIENT_DOMAIN_URL = "*";

//    @Bean
//    BCryptPasswordEncoder bCryptPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    DataSource dataSource;

    @Bean
    UserDaoRepository userDaoRepository() throws SQLException {
        return new UserDaoRepositoryImpl(dataSource);
    }

    @Bean
    RoleDaoRepository roleDaoRepository() throws SQLException {
        return new RoleDaoRepositoryImpl(dataSource);
    }

    @Bean
    EmailConstructor emailConstructor(){
        return new EmailConstructor();
    }

//    @Autowired
//    public ApplicationEnvironment(UserDetailsService userDetailsService){
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

}
