package ru.storage.server.controller.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;

public final class InfoCommand extends ViewCommand {
  private final Logger logger;

  public InfoCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments, locale, workerRepository);
    this.logger = LogManager.getLogger(InfoCommand.class);
  }

  @Override
  public Response executeCommand() {
    StringBuilder stringBuilder = new StringBuilder();
    return new Response(Status.OK, stringBuilder.toString());
  }
}
