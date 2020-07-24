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
  public static final String BEGINNING =
      "-------------------------< SHOW >-------------------------";
  public static final String SEPARATOR =
      "----------------------------------------------------------";

  private final String SHOW_PREFIX;
  private final String COLLECTION_IS_EMPTY_ANSWER;

  private final Logger logger;

  public ShowCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator, arguments, locale, workerRepository);
    logger = LogManager.getLogger(ShowCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ShowCommand");

    SHOW_PREFIX = resourceBundle.getString("prefixes.show");
    COLLECTION_IS_EMPTY_ANSWER = resourceBundle.getString("answers.collectionIsEmpty");
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
      return new Response(Status.NO_CONTENT, COLLECTION_IS_EMPTY_ANSWER);
    }

    StringBuilder result =
        new StringBuilder(BEGINNING)
            .append(System.lineSeparator())
            .append(SHOW_PREFIX)
            .append(System.lineSeparator())
            .append(SEPARATOR);

    for (Worker worker : allWorkers) {
      result.append(System.lineSeparator());
      appendWorker(result, worker);
      result.append(System.lineSeparator()).append(SEPARATOR);
    }

    logger.info(() -> "All workers were converted.");
    return new Response(Status.OK, result.toString());
  }
}
