package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class AddCommand extends ModificationCommand {
  private final String WRONG_WORKER_FORMAT_ANSWER;
  private final String WRONG_WORKER_DATA_ANSWER;
  private final String ADDED_SUCCESSFULLY_ANSWER;

  private final Logger logger;

  public AddCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments, locale, workerRepository);
    this.logger = LogManager.getLogger(AddCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.AddCommand");

    WRONG_WORKER_FORMAT_ANSWER = resourceBundle.getString("answers.wrongWorkerFormat");
    WRONG_WORKER_DATA_ANSWER = resourceBundle.getString("answers.wrongWorkerData");
    ADDED_SUCCESSFULLY_ANSWER = resourceBundle.getString("answers.wrongWorkerData");
  }

  @Override
  public Response executeCommand() {
    WorkerDTO workerDTO;

    try {
      workerDTO = createWorkerDTO(arguments);
    } catch (ValidationException e) {
      logger.warn(() -> "Cannot create workerDTO.", e);
      return new Response(Status.BAD_REQUEST, WRONG_WORKER_FORMAT_ANSWER);
    }

    try {
      workerRepository.insert(workerDTO.toEntity());
    } catch (ValidationException e) {
      logger.error(() -> "Cannot create worker.", e);
      return new Response(Status.BAD_REQUEST, WRONG_WORKER_DATA_ANSWER);
    } catch (RepositoryException e) {
      logger.error(() -> "Cannot add worker.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    logger.info(() -> "Worker was added.");
    return new Response(Status.CREATED, ADDED_SUCCESSFULLY_ANSWER);
  }
}
