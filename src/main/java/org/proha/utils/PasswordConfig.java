package org.proha.utils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Map;
import jakarta.inject.Inject;

@ApplicationScoped
public class PasswordConfig {

    String ALGORITHM = "MD5";
//    @Inject
//    private Pbkdf2PasswordHash passwordHash;
//
//    @PostConstruct
//    public void init() {
//        Map<String, String> parameters = Map.of(
//                "Pbkdf2PasswordHash.Iterations", "3072",
//                "Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512",
//                "Pbkdf2PasswordHash.SaltSizeBytes", "64"
//        );
//        passwordHash.initialize(parameters);
//    }

//    public PasswordHash getPasswordHash() {
//        return passwordHash;;
//    }

    public String hashPassword(String password) {
        if(ALGORITHM.equals("MD5")) {
            return hashWithMD5(password);
        }
        if(ALGORITHM.equals("PBKDF2")) {
//            PasswordHash passwordHash = getPasswordHash();
//            return passwordHash.generate(password.toCharArray());
        }
        throw new IllegalArgumentException("Invalid hashing algorithm");
    }

    public boolean verifyPassword(String password, String hashedPassword) {

        if(ALGORITHM.equals("MD5")) {
           String passwordAfterHashing = hashWithMD5(password);
              return passwordAfterHashing.equals(hashedPassword);
        }
        if(ALGORITHM.equals("PBKDF2")){
//            PasswordHash passwordHash = getPasswordHash();
//            return passwordHash.verify(password.toCharArray(), hashedPassword);
        }

        throw new IllegalArgumentException("Invalid hashing algorithm");
    }

    public String hashWithMD5(String password) {
        String generatedPassword = null;
        try {
            String salt = getSalt();
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(salt.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest(password.getBytes());

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return generatedPassword;

    }

    private String getSalt()
            throws NoSuchAlgorithmException, NoSuchProviderException
    {
        // Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

        // Create array for salt
        byte[] salt = new byte[16];

        // Get a random salt
        sr.nextBytes(salt);

        // return salt
        return salt.toString();
    }
}
