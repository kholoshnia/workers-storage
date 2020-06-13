package ru.storage.server.controller.command.commands.entry;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Map;

public class LogoutCommand extends EntryCommand {
  private final Logger logger;

  public LogoutCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Repository<User> userRepository) {
    super(configuration, argumentMediator, arguments, userRepository);
    this.logger = LogManager.getLogger(LogoutCommand.class);
  }

  @Override
  public Response executeCommand() {
    return null;
  }
}
