package ru.storage.server.controller.controllers.command.commands.entry;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.security.Key;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LogoutCommand extends EntryCommand {
  private final String LOGGED_OUT_ANSWER;

  private final Logger logger;

  public LogoutCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      HashGenerator hashGenerator,
      Repository<User> userRepository,
      Key key) {
    super(configuration, argumentMediator, arguments, locale, hashGenerator, userRepository, key);
    logger = LogManager.getLogger(LogoutCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.LogoutCommand", locale);

    LOGGED_OUT_ANSWER = resourceBundle.getString("answers.loggedOut");
  }

  @Override
  public Response executeCommand() {
    logger.info(
        () -> "Returning unauthorized answer, user must be unauthorized on the client size.");
    return new Response(Status.OK, LOGGED_OUT_ANSWER, "");
  }
}
