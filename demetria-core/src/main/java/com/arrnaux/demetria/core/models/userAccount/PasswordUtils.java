package com.arrnaux.demetria.core.models.userAccount;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class PasswordUtils {

    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String TEMPORARY_SALT = "SNSalt";

    // generate a global salt
    /*
    public static Optional<String> generateSalt(final int length) {
        if (length < 1) {
            System.err.println("error in generateSalt: length must be >0");
            return Optional.empty();
        }

        byte[] salt = new byte[length];
        RAND.nextBytes(salt);

        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }
     */

    public static Optional<String> hashPassword(String password, String salt) {

        // delete this after generate a global salt
        if (salt == null) salt = TEMPORARY_SALT;

        char[] chars = password.toCharArray();

        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Exception encountered in hashPassword");
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPassword(String password, String salt, String key) {
        // delete this after generate a global salt
        if (salt == null) salt = TEMPORARY_SALT;

        Optional<String> optEncrypted = hashPassword(password, salt);
        return optEncrypted.map(s -> s.equals(key)).orElse(false);
    }
}
