package me.pckv.kompis;

import me.pckv.kompis.security.AuthorizationAttributeResolver;
import me.pckv.kompis.security.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Context for adding runtime handlers. This adds the authorization preHandler
 * and the @LoggedIn argument resolver.
 */
@Configuration
@EnableWebMvc
public class WebMvcContext implements WebMvcConfigurer {

    private AuthorizationInterceptor authorizationInterceptor;
    private AuthorizationAttributeResolver authorizationAttributeResolver;

    public WebMvcContext(AuthorizationInterceptor authorizationInterceptor, AuthorizationAttributeResolver authorizationAttributeResolver) {
        this.authorizationInterceptor = authorizationInterceptor;
        this.authorizationAttributeResolver = authorizationAttributeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationAttributeResolver);
    }
}
