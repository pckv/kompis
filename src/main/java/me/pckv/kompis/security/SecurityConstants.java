package me.pckv.kompis.security;

class SecurityConstants {
    static final String SECRET = "JwtSecretKey";
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String AUTHORIZATION_HEADER = "Authorization";
    static final String USER_CREATE_PATH = "/users";
}
