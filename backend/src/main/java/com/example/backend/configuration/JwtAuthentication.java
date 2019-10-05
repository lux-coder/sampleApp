package com.example.backend.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private AuthenticationManager authenticationManager;

    public JwtAuthentication(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        logger.info("In attemptAuthentication");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        com.example.backend.model.User user = null;
        try {
            user = objectMapper.readValue(httpServletRequest.getInputStream(), com.example.backend.model.User.class);
            logger.info("Creating user {}", user.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to convert Json into Java Object: " + e);
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain,
                                            Authentication authentication) throws IOException, ServletException {
        logger.info("In successfulAuthentication");
        User user = (User) authentication.getPrincipal();
        List<String> roles = new ArrayList<>();
        user.getAuthorities()
                .forEach(authority -> {
                    roles.add(authority.getAuthority());
                });
        String jwtToken = JWT.create()
                .withIssuer(httpServletRequest.getRequestURI())
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles.stream().toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis()+ ApplicationEnvironment.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(ApplicationEnvironment.SECRET));
        httpServletResponse.addHeader(ApplicationEnvironment.HEADER_TYPE, ApplicationEnvironment.TOKEN_PREFIX+jwtToken);
    }
}
