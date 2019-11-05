package me.pckv.kompis.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private JwtManager jwtManager;

    public AuthorizationFilter(AuthenticationManager authenticationManager, JwtManager jwtManager) {
        super(authenticationManager);

        this.jwtManager = jwtManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");

        // Check that we received a token
        if (token != null && token.startsWith("Bearer ")) {

            // Verify the token and retrieve the username
            String username = jwtManager.getUsername(token.replace("Bearer ", ""));

            // Set the authentication using the username if it was verified
            if (username != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()));
            }
        }

        chain.doFilter(request, response);
    }
}
