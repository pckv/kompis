package me.pckv.kompis;

import me.pckv.kompis.resolver.AuthorizationAttributeResolver;
import me.pckv.kompis.resolver.ListingAttributeResolver;
import me.pckv.kompis.security.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Context for adding runtime handlers. This adds:
 *    - the authorization preHandler
 *    - the @LoggedIn argument resolver
 *    - argument resolver for injecting Listing type from a {listingId} path variable
 */
@Configuration
@EnableWebMvc
public class WebMvcContext implements WebMvcConfigurer {

    private AuthorizationInterceptor authorizationInterceptor;
    private AuthorizationAttributeResolver authorizationAttributeResolver;
    private ListingAttributeResolver listingAttributeResolver;

    public WebMvcContext(AuthorizationInterceptor authorizationInterceptor, AuthorizationAttributeResolver authorizationAttributeResolver, ListingAttributeResolver listingAttributeResolver) {
        this.authorizationInterceptor = authorizationInterceptor;
        this.authorizationAttributeResolver = authorizationAttributeResolver;
        this.listingAttributeResolver = listingAttributeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationAttributeResolver);
        resolvers.add(listingAttributeResolver);
    }
}
