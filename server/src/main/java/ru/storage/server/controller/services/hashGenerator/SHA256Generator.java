package ru.storage.server.controller.services.hashGenerator;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.controller.services.hashGenerator.exceptions.HashGeneratorException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA256Generator extends HashGenerator {
  private final Logger logger;

  public SHA256Generator(@Nonnull Configuration configuration) {
    super(configuration);
    this.logger = LogManager.getLogger(SHA256Generator.class);
  }

  @Override
  protected String generateHash(@Nonnull String string) throws HashGeneratorException {
    String sha256;

    try {
      MessageDigest msdDigest = MessageDigest.getInstance("SHA-256");
      msdDigest.update(string.getBytes(StandardCharsets.UTF_8), 0, string.length());
      sha256 = DatatypeConverter.printHexBinary(msdDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      logger.fatal("An exception was caught during the work of SHA-256 hash generator.", e);
      throw new HashGeneratorException(e);
    }

    logger.info("SHA-256 hash generated SUCCESSFULLY.");
    return sha256;
  }
}
