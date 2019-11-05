package me.pckv.kompis.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import me.pckv.kompis.data.User;

public class PasswordHasher {

    public static String hash(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verify(User user, String password) {
        return BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified;
    }
}
