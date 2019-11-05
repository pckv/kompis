package me.pckv.kompis.security;

import me.pckv.kompis.controller.Authorized;
import me.pckv.kompis.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private JwtManager jwtManager;
    private UserService userService;

    public AuthorizationInterceptor(JwtManager jwtManager, UserService userService) {
        this.jwtManager = jwtManager;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // Check if the mapping requires authorization
        Authorized authorized = handlerMethod.getMethodAnnotation(Authorized.class);
        if (authorized == null) {
            return true;
        }

        // Verify the token and parse the email
        String token = request.getHeader("Authorization");
        String email = getEmail(token);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Add the token back to the response and add the logged in user as an attribute
        response.addHeader("Authorization", "Bearer " + token);
        request.setAttribute("principal", userService.getUser(email));

        return true;
    }

    private String getEmail(String token) {
        // Check that we received a token
        if (token != null && token.startsWith("Bearer ")) {

            // Verify the token and return the username
            return jwtManager.getSubject(token.replace("Bearer ", ""));
        }

        return null;
    }
}
