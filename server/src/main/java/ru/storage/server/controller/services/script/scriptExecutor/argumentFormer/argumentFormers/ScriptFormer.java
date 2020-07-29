package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.FormingException;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public final class ScriptFormer extends ArgumentFormer {
  private static final Logger logger = LogManager.getLogger(ScriptFormer.class);

  private static final Pattern FILE_PATTERN = Pattern.compile("^(\\\\?([^/]*[/])*)([^/]+)$");
  private static final Pattern URL_PATTERN =
      Pattern.compile(
          "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)");

  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumberException;
  private String wrongFilePathException;

  @Inject
  public ScriptFormer(ArgumentMediator argumentMediator) {
    this.argumentMediator = argumentMediator;
  }

  @Override
  protected void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ScriptFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    wrongFilePathException = resourceBundle.getString("exceptions.wrongFilePath");
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }

    String path = arguments.get(0);

    if (FILE_PATTERN.matcher(path).matches()) {
      File file = new File(path);

      if (!file.exists() || !file.isFile() || !file.canRead()) {
        throw new WrongArgumentsException(wrongFilePathException);
      }
    } else if (URL_PATTERN.matcher(path).matches()) {
      try {
        URL url = new URL(path);
        url.openStream();
      } catch (IOException e) {
        throw new WrongArgumentsException(wrongFilePathException);
      }
    }

    if (!FILE_PATTERN.matcher(path).matches() && !URL_PATTERN.matcher(path).matches()) {
      logger.warn(() -> "Got wrong argument.");
      throw new WrongArgumentsException(wrongFilePathException);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments, Script script) throws FormingException {
    String path = arguments.get(0);
    Scanner scanner;

    if (FILE_PATTERN.matcher(path).matches()) {
      try {
        scanner = new Scanner(new FileInputStream(path));
        logger.info("Got file path: {}.", () -> path);
      } catch (FileNotFoundException e) {
        logger.warn(() -> "Cannot read file.", e);
        throw new FormingException(wrongFilePathException);
      }
    } else if (URL_PATTERN.matcher(path).matches()) {
      try {
        scanner = new Scanner(new URL(path).openStream());
        logger.info("Got URL: {}.", () -> path);
      } catch (IOException e) {
        logger.warn(() -> "Cannot read URL.", e);
        throw new FormingException(wrongFilePathException);
      }
    } else {
      throw new FormingException(wrongFilePathException);
    }

    Map<String, String> allArguments = new LinkedHashMap<>();

    int counter = 0;
    while (scanner.hasNextLine()) {
      allArguments.put(
          String.format("%s_%d", argumentMediator.scriptLine, counter), scanner.nextLine());
      counter++;
    }

    scanner.close();

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
