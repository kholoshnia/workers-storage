package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;

public final class AddCommand extends ModificationCommand {
  private final Logger logger;

  public AddCommand(
      Configuration configuration,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, arguments, locale, workerRepository);
    this.logger = LogManager.getLogger(AddCommand.class);
  }

  @Override
  public Response executeCommand() {
    return null;
  }
}
