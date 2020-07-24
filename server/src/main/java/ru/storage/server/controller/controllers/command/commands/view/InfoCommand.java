package ru.storage.server.controller.controllers.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class InfoCommand extends ViewCommand {
  public static final String BEGINNING =
      "-----------------------------< INFO >-----------------------------";
  public static final String SEPARATOR =
      "------------------------------------------------------------------";

  private final String INFO_PREFIX;
  private final String TYPE_PREFIX;
  private final String INIT_TIME_PREFIX;
  private final String SIZE_PREFIX;

  private final Logger logger;

  public InfoCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator, arguments, locale, workerRepository);
    logger = LogManager.getLogger(InfoCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.InfoCommand", locale);

    INFO_PREFIX = resourceBundle.getString("answers.info");
    TYPE_PREFIX = resourceBundle.getString("answers.type");
    INIT_TIME_PREFIX = resourceBundle.getString("answers.initTime");
    SIZE_PREFIX = resourceBundle.getString("answers.size");
  }

  @Override
  public Response executeCommand() {
    String result =
        BEGINNING
            + System.lineSeparator()
            + INFO_PREFIX
            + System.lineSeparator()
            + SEPARATOR
            + System.lineSeparator()
            + String.format("%s: %s", TYPE_PREFIX, workerRepository.getType())
            + System.lineSeparator()
            + String.format(
                "%s: %s", INIT_TIME_PREFIX, dateFormat.format(workerRepository.getInitTime()))
            + System.lineSeparator()
            + String.format("%s: %d", SIZE_PREFIX, workerRepository.getSize())
            + System.lineSeparator()
            + SEPARATOR;

    logger.info(() -> "Information was formed.");
    return new Response(Status.OK, result);
  }
}
