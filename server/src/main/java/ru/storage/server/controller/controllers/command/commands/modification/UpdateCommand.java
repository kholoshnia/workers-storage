package ru.storage.server.controller.controllers.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.parser.exceptions.ParserException;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.queries.GetEqualIdWorkers;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class UpdateCommand extends ModificationCommand {
  private static final Logger logger = LogManager.getLogger(UpdateCommand.class);

  private final String updatedSuccessfullyAnswer;

  public UpdateCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository,
      Parser parser,
      User user) {
    super(configuration, argumentMediator, arguments, locale, user, workerRepository, parser);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.UpdateCommand");

    updatedSuccessfullyAnswer = resourceBundle.getString("answers.updatedSuccessfully");
  }

  @Override
  public Response executeCommand() {
    Long id;

    try {
      id = parser.parseLong(arguments.get(argumentMediator.workerId));
    } catch (ParserException e) {
      logger.warn(() -> "Got wrong update id.", e);
      return new Response(Status.BAD_REQUEST, wrongIdAnswer);
    }

    if (id == null) {
      logger.warn(() -> "Got null update id.");
      return new Response(Status.BAD_REQUEST, wrongIdAnswer);
    }

    Query<Worker> query = new GetEqualIdWorkers(id);
    List<Worker> equalIdWorkers;

    try {
      equalIdWorkers = workerRepository.get(query);
    } catch (RepositoryException e) {
      logger.error("Cannot get workers with id is equal to {}.", (Supplier<?>) () -> id, e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    if (equalIdWorkers.isEmpty()) {
      logger.info("Worker with specified id: {} was not found.", () -> id);
      return new Response(Status.NOT_FOUND, workerNotFoundAnswer);
    }

    WorkerDTO workerDTO;

    try {
      workerDTO = createWorkerDTO();
    } catch (ParserException e) {
      logger.warn(() -> "Cannot create workerDTO.", e);
      return new Response(Status.BAD_REQUEST, wrongWorkerFormatAnswer);
    }

    if (workerDTO == null) {
      logger.warn(() -> "Got null worker.");
      return new Response(Status.BAD_REQUEST, wrongWorkerFormatAnswer);
    }

    for (Worker worker : equalIdWorkers) {
      try {
        if (worker.getOwnerId() == user.getId()) {
          Worker newWorker = workerDTO.toEntity();
          setId(worker, newWorker);
          setOwnerId(newWorker);
          workerRepository.update(newWorker);
        } else {
          return new Response(Status.FORBIDDEN, notOwnerAnswer);
        }
      } catch (RepositoryException | ValidationException e) {
        logger.error("Cannot update worker which id is equal to {}.", (Supplier<?>) () -> id, e);
        return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
      }
    }

    logger.info(() -> "Worker was updated.");
    return new Response(Status.OK, updatedSuccessfullyAnswer);
  }
}
