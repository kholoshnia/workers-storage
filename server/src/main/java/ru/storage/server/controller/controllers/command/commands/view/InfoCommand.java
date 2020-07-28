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
  private static final Logger logger = LogManager.getLogger(InfoCommand.class);

  private final String infoPrefix;
  private final String typePrefix;
  private final String initTimePrefix;
  private final String sizePrefix;

  public InfoCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator, arguments, locale, workerRepository);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.InfoCommand", locale);

    infoPrefix = resourceBundle.getString("answers.info");
    typePrefix = resourceBundle.getString("answers.type");
    initTimePrefix = resourceBundle.getString("answers.initTime");
    sizePrefix = resourceBundle.getString("answers.size");
  }

  @Override
  public Response executeCommand() {
    String result =
        infoPrefix
            + System.lineSeparator()
            + System.lineSeparator()
            + String.format("%s: %s", typePrefix, workerRepository.getType())
            + System.lineSeparator()
            + String.format(
                "%s: %s", initTimePrefix, dateFormat.format(workerRepository.getInitTime()))
            + System.lineSeparator()
            + String.format("%s: %d", sizePrefix, workerRepository.getSize());

    logger.info(() -> "Information was formed.");
    return new Response(Status.OK, result);
  }
}
