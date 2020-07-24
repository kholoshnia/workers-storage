package ru.storage.server.controller.controllers.command.commands.entry;

import io.jsonwebtoken.Jwts;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.controller.services.hash.exceptions.HashGeneratorException;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.userRepository.queries.GetEqualsLoginUsers;

import java.security.Key;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginCommand extends EntryCommand {
  private final String USER_NOT_REGISTERED_ANSWER;
  private final String WRONG_USER_PASSWORD_ANSWER;
  private final String LOGGED_IN_ANSWER;

  private final Logger logger;

  public LoginCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      HashGenerator hashGenerator,
      Repository<User> userRepository,
      Key key) {
    super(configuration, argumentMediator, arguments, locale, hashGenerator, userRepository, key);
    logger = LogManager.getLogger(LoginCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.LoginCommand", locale);

    USER_NOT_REGISTERED_ANSWER = resourceBundle.getString("answers.userNotRegistered");
    WRONG_USER_PASSWORD_ANSWER = resourceBundle.getString("answers.wrongUserPassword");
    LOGGED_IN_ANSWER = resourceBundle.getString("answers.loggedIn");
  }

  @Override
  public Response executeCommand() {
    String login = arguments.get(argumentMediator.USER_LOGIN);
    String password = arguments.get(argumentMediator.USER_PASSWORD);

    Query<User> query = new GetEqualsLoginUsers(login);
    List<User> equalLoginUsers;

    try {
      equalLoginUsers = userRepository.get(query);
    } catch (RepositoryException e) {
      logger.error("Cannot get users with login equal to {}.", (Supplier<?>) () -> login, e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    if (equalLoginUsers.isEmpty()) {
      logger.warn("User with specified login: {} was not found.", () -> login);
      return new Response(Status.NOT_FOUND, USER_NOT_REGISTERED_ANSWER);
    }

    String hashedPassword;

    try {
      hashedPassword = hashGenerator.hashPepperSalt(password);
    } catch (HashGeneratorException e) {
      logger.error(() -> "Error was caught during hash generation.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    for (User user : equalLoginUsers) {
      if (!user.getPassword().equals(hashedPassword)) {
        logger.warn("Wrong user password.");
        return new Response(Status.FORBIDDEN, WRONG_USER_PASSWORD_ANSWER);
      }
    }

    String jws = Jwts.builder().setSubject(subject).signWith(key).compact();
    logger.info(() -> "Json web signature was created.");

    return new Response(Status.OK, LOGGED_IN_ANSWER, jws);
  }
}
