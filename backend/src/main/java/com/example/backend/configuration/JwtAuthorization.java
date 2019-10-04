package com.example.backend.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthorization extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", ApplicationEnvironment.CLIENT_DOMAIN_URL);

        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, "
                + "Content-Type, Access-Control-Request-Method, " + "Access-Control-Request-Headers, Authorization");

        httpServletResponse.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, " + "Access-Control-Allow-Credentials, " + "Authorization");

        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET," + "POST, " + "DELETE");

        if ((httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS"))) {
            try {
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String jwtToken = httpServletRequest.getHeader(ApplicationEnvironment.HEADER_TYPE);
            if (jwtToken == null || !jwtToken.startsWith(ApplicationEnvironment.TOKEN_PREFIX)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            JWT.require(Algorithm.HMAC256(ApplicationEnvironment.SECRET));
            DecodedJWT jwt = JWT.decode(jwtToken.substring(ApplicationEnvironment.TOKEN_PREFIX.length()));
            String username = jwt.getSubject();
            List<String> roles = jwt.getClaims().get("roles").asList(String.class);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(username,
                    null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
