package ru.storage.client.controller.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.exceptions.FormingException;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.common.ArgumentMediator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public final class ScriptFormer extends ArgumentFormer implements LocaleListener {
  private static final Pattern FILE_PATTERN = Pattern.compile("^(\\\\?([^/]*[/])*)([^/]+)$");
  private static final Pattern URL_PATTERN =
      Pattern.compile(
          "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)");

  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumberException;
  private String wrongFilePathException;
  private String emptyScriptException;

  @Inject
  public ScriptFormer(ArgumentMediator argumentMediator) {
    logger = LogManager.getLogger(ScriptFormer.class);
    this.argumentMediator = argumentMediator;
  }

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ScriptFormer", locale);

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    wrongFilePathException = resourceBundle.getString("exceptions.wrongFilePath");
    emptyScriptException = resourceBundle.getString("exceptions.emptyScript");
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

      if (file.exists() && file.isFile() && file.canRead()) {
        return;
      }
    } else if (URL_PATTERN.matcher(path).matches()) {
      try {
        URL url = new URL(path);
        url.openStream();
      } catch (IOException e) {
        throw new WrongArgumentsException(wrongFilePathException);
      }
    }

    throw new WrongArgumentsException(wrongFilePathException);
  }

  @Override
  public Map<String, String> form(List<String> arguments) throws FormingException {
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
          String.format("%s%d", argumentMediator.SCRIPT_LINE, counter), scanner.nextLine());
      counter++;
    }

    if (allArguments.isEmpty()) {
      throw new FormingException(emptyScriptException);
    }

    scanner.close();

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
