package oogasalad.model.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * This class services to provide the means to hash a password for a player profile. It also decrypts to verify the password.
 * Much of this class was either assisted through online code or ChatGPT
 *
 * @author Justin Aronwald
 */
public class PasswordConfig {
  private static final int ITERATIONS = 65536;
  private static final int KEY_LENGTH = 256;
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

  public static String generateSalt() {
    byte[] salt = new byte[16];
    new SecureRandom().nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public static String hashPassword(String password, String salt) throws PasswordHashingException {
    try {
      PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
      SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
      byte[] hash = skf.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new PasswordHashingException("Error hashing password", e);
    }
  }

  public static boolean verifyPassword(String inputPassword, String salt, String storedHash)
      throws PasswordHashingException {
    return hashPassword(inputPassword, salt).equals(storedHash);
  }
}
