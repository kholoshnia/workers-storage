package ru.storage.server.controller.controllers.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.parser.exceptions.ParserException;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class AddCommand extends ModificationCommand {
  private static final Logger logger = LogManager.getLogger(AddCommand.class);

  private final String addedSuccessfullyAnswer;

  public AddCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository,
      Parser parser,
      User user) {
    super(configuration, argumentMediator, arguments, locale, user, workerRepository, parser);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.AddCommand");

    addedSuccessfullyAnswer = resourceBundle.getString("answers.addedSuccessfully");
  }

  @Override
  public Response executeCommand() {
    WorkerDTO workerDTO;

    try {
      workerDTO = createWorkerDTO();
    } catch (ParserException e) {
      logger.warn(() -> "Cannot create worker DTO.", e);
      return new Response(Status.BAD_REQUEST, wrongWorkerFormatAnswer);
    }

    if (workerDTO == null) {
      logger.warn(() -> "Got null worker.");
      return new Response(Status.BAD_REQUEST, wrongWorkerFormatAnswer);
    }

    try {
      Worker worker = workerDTO.toEntity();
      setOwnerId(worker);
      workerRepository.insert(worker);
    } catch (ValidationException e) {
      logger.error(() -> "Cannot create worker from DTO.", e);
      return new Response(Status.BAD_REQUEST, wrongWorkerDataAnswer);
    } catch (RepositoryException e) {
      logger.error(() -> "Cannot add worker.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    logger.info(() -> "Worker was added.");
    return new Response(Status.CREATED, addedSuccessfullyAnswer);
  }
}
