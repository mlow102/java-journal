package cs3500.pa05.controller;

import java.security.SecureRandom;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordEncoderTest {
  static final int SEED = 413;
  static final String PASSWORD = "PASSWORD";

  @Test
  public void testEncodingMatching() {
    SecureRandom random = new SecureRandom(new byte[]{(byte) SEED});
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    String strSalt = Base64.getEncoder().encodeToString(salt);

    String encodedHash = PasswordEncoder.encode(PASSWORD, strSalt);

    Assertions.assertTrue(PasswordEncoder.matches(PASSWORD, encodedHash, strSalt));
    Assertions.assertFalse(PasswordEncoder.matches("NOT THE REAL " + PASSWORD,
        encodedHash, strSalt));
  }

}