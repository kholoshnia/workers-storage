package ru.storage.server.controller.command.commands.view;

import com.google.gson.Gson;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.queries.GetAllWorkers;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public final class ShowCommand extends ViewCommand {
  private final String COLLECTION_IS_EMPTY_ANSWER;

  private final Logger logger;

  public ShowCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Gson gson,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments, gson, workerRepository);
    this.logger = LogManager.getLogger(ShowCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.ShowCommand");

    COLLECTION_IS_EMPTY_ANSWER = resourceBundle.getString("answers.collectionIsEmpty");
  }

  @Override
  public Response executeCommand() {
    Query<Worker> query = new GetAllWorkers();
    List<Worker> allWorkers;

    try {
      allWorkers = workerRepository.get(query);
    } catch (RepositoryException e) {
      logger.error("Cannot get all workers.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    if (allWorkers.isEmpty()) {
      logger.info("Workers not found.");
      return new Response(Status.NO_CONTENT, COLLECTION_IS_EMPTY_ANSWER);
    }

    String result = gson.toJson(allWorkers);

    logger.info("All workers were converted SUCCESSFULLY.");
    return new Response(Status.OK, result);
  }
}
