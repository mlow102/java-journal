package cs3500.pa05.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Represents password encoder
 */
public final class PasswordEncoder {
  /**
   * Private constructor to prevent construction of the class
   */
  private PasswordEncoder() {  }

  /**
   * Hashes given password string
   *
   * @param password string of a password to hash with this objects salt
   * @param strSalt string salt with which the encoding is performed
   * @return hashed password
   */
  public static String encode(String password, String strSalt) {
    byte[] salt = strSalt.getBytes();
    byte[] hash;

    try {
      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
      SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      hash = f.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException("Unexpected error occurred when trying to encrypt password");
    }

    Base64.Encoder enc = Base64.getEncoder();
    return enc.encodeToString(hash);
  }

  /**
   * Checks whether the given input password matches the hashed reference password
   *
   * @param inputPassword string password against which to check
   * @param hashedPassword string hashed reference password
   * @param strSalt string salt with which the encoding is performed
   * @return true if inputPassword is the same as the password that was used for hashedPassword
   */
  public static boolean matches(String inputPassword, String hashedPassword, String strSalt) {
    return hashedPassword.equals(encode(inputPassword, strSalt));
  }
}
