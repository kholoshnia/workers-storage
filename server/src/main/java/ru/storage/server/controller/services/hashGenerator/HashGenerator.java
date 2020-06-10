package ru.storage.server.controller.services.hashGenerator;

import org.apache.commons.configuration2.Configuration;
import ru.storage.server.controller.services.hashGenerator.exceptions.HashGeneratorException;

import javax.annotation.Nonnull;

public abstract class HashGenerator {
  protected final String salt;
  protected final String pepper;

  protected HashGenerator(@Nonnull Configuration configuration) {
    salt = configuration.getString("hashGenerator.salt");
    pepper = configuration.getString("hashGenerator.pepper");
  }

  /**
   * Generates hash.
   *
   * @param string concrete string to hash
   * @return hashed string
   * @throws HashGeneratorException - if algorithm was not found
   */
  protected abstract String generateHash(@Nonnull String string) throws HashGeneratorException;

  /**
   * Generates hash with salt
   *
   * @param string concrete string to hash with salt
   * @return hashed string
   * @throws HashGeneratorException - if algorithm was not found.
   */
  public final String generateHashWithSalt(@Nonnull String string) throws HashGeneratorException {
    String stringWithSalt = string + salt;
    return generateHash(stringWithSalt);
  }

  /**
   * Generates hash with pepper and salt
   *
   * @param string concrete string to hash with pepper and salt
   * @return hashed string
   * @throws HashGeneratorException - if algorithm was not found
   */
  public final String generateHashWithPepperAndSalt(@Nonnull String string)
      throws HashGeneratorException {
    String stringWithPepperAndSalt = pepper + string + salt;
    return generateHash(stringWithPepperAndSalt);
  }
}
