package me.pckv.kompis.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @Getter @Setter private String secret = null;
    @Getter @Setter private String secretAlgorithm = "HS512";
    @Getter @Setter private long expirationDays = 7;
}
