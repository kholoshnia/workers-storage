package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.queries.GetEqualIdWorkers;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class RemoveCommand extends ModificationCommand {
  private final String REMOVED_SUCCESSFULLY_ANSWER;

  private final Logger logger;

  public RemoveCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository,
      Parser parser) {
    super(configuration, argumentMediator, arguments, locale, workerRepository, parser);
    logger = LogManager.getLogger(RemoveCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.RemoveCommand");

    REMOVED_SUCCESSFULLY_ANSWER = resourceBundle.getString("answers.removedSuccessfully");
  }

  @Override
  public Response executeCommand() {
    long id;

    try {
      id = Long.parseLong(arguments.get(argumentMediator.WORKER_ID));
    } catch (NumberFormatException e) {
      logger.warn(() -> "Got wrong id.", e);
      return new Response(Status.BAD_REQUEST, WRONG_ID_ANSWER);
    }

    Query<Worker> query = new GetEqualIdWorkers(id);
    List<Worker> equalIdWorkers;

    try {
      equalIdWorkers = workerRepository.get(query);
    } catch (RepositoryException e) {
      logger.error("Cannot get workers which ids are equal to {}.", (Supplier<?>) () -> id, e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    if (equalIdWorkers.isEmpty()) {
      logger.info("Worker with specified id: {} was not found.", () -> id);
      return new Response(Status.NOT_FOUND, WORKER_NOT_FOUND_ANSWER);
    }

    for (Worker worker : equalIdWorkers) {
      try {
        workerRepository.delete(worker);
      } catch (RepositoryException e) {
        logger.error("Cannot remove worker which id is equal to {}.", (Supplier<?>) () -> id, e);
        return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
      }
    }

    logger.info(() -> "Worker was removed.");
    return new Response(Status.OK, REMOVED_SUCCESSFULLY_ANSWER);
  }
}
