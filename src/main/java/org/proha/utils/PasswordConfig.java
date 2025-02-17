package org.proha.utils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;
import jakarta.inject.Inject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@ApplicationScoped
public class PasswordConfig {

    private static String ALGORITHM = "MD5";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256; // 256 bits = 32 bytes
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashPassword(String password, String salt) {

        if(ALGORITHM.equals("MD5")) {
            return hashWithMD5(password, salt);
        }
        if(ALGORITHM.equals("PBKDF2")) {
            return hashWithPBKDF2(password, salt);
        }
        throw new IllegalArgumentException("Invalid hashing algorithm");
    }

    public boolean verifyPassword(String password, String hashedPassword, String salt) {

        String newHash = "";
        if(ALGORITHM.equals("MD5")) {
            newHash  = hashWithMD5(password, salt);
        }
        if(ALGORITHM.equals("PBKDF2")){
            newHash = hashWithPBKDF2(password, salt);
        }
        return newHash.equals(hashedPassword);
    }
    private static String hashWithPBKDF2(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private static String hashWithMD5(String password, String salt) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add salt bytes to digest
            md.update(Base64.getDecoder().decode(salt));

            // Add password bytes to digest
            md.update(password.getBytes("UTF-8"));

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // Convert the byte array to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            // Return the hashed password
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
