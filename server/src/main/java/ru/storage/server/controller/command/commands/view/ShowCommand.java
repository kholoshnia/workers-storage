package ru.storage.server.controller.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.repositories.workerRepository.queries.GetAllWorkers;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class ShowCommand extends ViewCommand {
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
    this.logger = LogManager.getLogger(ShowCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.ShowCommand");

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
        new StringBuilder(SEPARATOR)
            .append(System.lineSeparator())
            .append(SHOW_PREFIX)
            .append(SEPARATOR)
            .append(System.lineSeparator());

    for (Worker worker : allWorkers) {
      appendWorker(result, worker);
      result.append(System.lineSeparator());
      result.append(SEPARATOR);
    }

    logger.info(() -> "All workers were converted.");
    return new Response(Status.OK, result.toString());
  }
}
