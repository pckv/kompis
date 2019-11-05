package me.pckv.kompis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtManager jwtManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtManager jwtManager) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtManager = jwtManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Parse User ResponseBody object from server request
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // Find the User object of the logged in user
        User user = userService.getUser(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername());

        // Generate the token and add it to the headers
        String token = jwtManager.generateToken(user.getEmail());
        response.addHeader("Authorization", "Bearer " + token);

        // Convert the User object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        // Add the User object to the response
        PrintWriter writer = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding("UTF-8");
        writer.print(json);
        writer.flush();
    }
}
