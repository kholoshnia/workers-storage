package ru.storage.server.controller.services.hash;

import org.apache.commons.configuration2.Configuration;
import ru.storage.server.controller.services.hash.exceptions.HashGeneratorException;

import javax.annotation.Nonnull;

public abstract class HashGenerator {
  protected final String salt;
  protected final String pepper;

  protected HashGenerator(@Nonnull Configuration configuration) {
    salt = configuration.getString("hash.salt");
    pepper = configuration.getString("hash.pepper");
  }

  /**
   * Generates hash.
   *
   * @param string string to hash
   * @return hashed string
   * @throws HashGeneratorException - if the algorithm is not found
   */
  protected abstract String hash(@Nonnull String string) throws HashGeneratorException;

  /**
   * Generates hash with salt
   *
   * @param string string to hash with salt
   * @return hashed string
   * @throws HashGeneratorException - if the algorithm is not found.
   */
  public final String hashSalt(@Nonnull String string) throws HashGeneratorException {
    String stringWithSalt = string + salt;
    return hash(stringWithSalt);
  }

  /**
   * Generates hash with pepper and salt
   *
   * @param string string to hash with pepper and salt
   * @return hashed string
   * @throws HashGeneratorException - if the algorithm is not found
   */
  public final String hashPepperSalt(@Nonnull String string) throws HashGeneratorException {
    String stringWithPepperAndSalt = pepper + string + salt;
    return hash(stringWithPepperAndSalt);
  }
}
