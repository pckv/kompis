package me.pckv.kompis.resolver;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.service.ListingService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Attribute resolver for injecting a listing based on the path
 * variable listingId.
 */
@Component
public class ListingAttributeResolver implements HandlerMethodArgumentResolver {

    private static final String LISTING_ID = "listingId";

    private ListingService listingService;

    public ListingAttributeResolver(ListingService listingService) {
        this.listingService = listingService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // Only match Listing variables
        return methodParameter.getParameterType().equals(Listing.class);
    }

    @Override
    public Listing resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // Find the path variable containing the listing ID and return the listing from the service
        Map<String, String> pathVariables = (Map<String, String>) nativeWebRequest.getNativeRequest(HttpServletRequest.class).getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return listingService.getListing(Long.valueOf(pathVariables.get(LISTING_ID)));
    }
}
