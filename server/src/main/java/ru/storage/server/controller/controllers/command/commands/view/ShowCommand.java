package ru.storage.server.controller.controllers.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;
import ru.storage.server.model.domain.repository.repositories.workerRepository.queries.GetAllWorkers;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class ShowCommand extends ViewCommand {
  private static final Logger logger = LogManager.getLogger(ShowCommand.class);

  private final String showPrefix;
  private final String collectionIsEmptyAnswer;

  public ShowCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator, arguments, locale, workerRepository);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ShowCommand");

    showPrefix = resourceBundle.getString("prefixes.show");
    collectionIsEmptyAnswer = resourceBundle.getString("answers.collectionIsEmpty");
  }

  @Override
  public Response executeCommand() {
    Query<Worker> query = new GetAllWorkers();
    List<Worker> allWorkers;

    try {
      allWorkers = workerRepository.get(query);
    } catch (RepositoryException e) {
      logger.error(() -> "Cannot get all workers.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    if (allWorkers.isEmpty()) {
      logger.info(() -> "Workers not found.");
      return new Response(Status.NO_CONTENT, collectionIsEmptyAnswer);
    }

    StringBuilder result = new StringBuilder(showPrefix);

    for (Worker worker : allWorkers) {
      result
          .append(System.lineSeparator())
          .append(System.lineSeparator())
          .append(workerToString(worker));
    }

    logger.info(() -> "All workers were converted.");
    return new Response(Status.OK, result.toString());
  }
}
