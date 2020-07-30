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
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.userRepository.queries.GetEqualsLoginUsers;

import java.security.Key;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class RegisterCommand extends EntryCommand {
  private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

  private final String userAlreadyRegisteredAnswer;
  private final String registeredAnswer;

  public RegisterCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<User> userRepository,
      HashGenerator hashGenerator,
      Key key) {
    super(configuration, argumentMediator, arguments, locale, userRepository, hashGenerator, key);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.RegisterCommand", locale);

    userAlreadyRegisteredAnswer = resourceBundle.getString("answers.alreadyRegistered");
    registeredAnswer = resourceBundle.getString("answers.registered");
  }

  @Override
  public Response executeCommand() {
    String name = arguments.get(argumentMediator.userName);
    String login = arguments.get(argumentMediator.userLogin);
    String password = arguments.get(argumentMediator.userPassword);

    Query<User> query = new GetEqualsLoginUsers(login);
    List<User> equalLoginUsers;

    try {
      equalLoginUsers = userRepository.get(query);
    } catch (RepositoryException e) {
      logger.error(
          "Cannot get users with login equal to {} to continue registration.",
          (Supplier<?>) () -> login,
          e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    if (!equalLoginUsers.isEmpty()) {
      logger.warn("User with specified login: {} is already registered.", () -> login);
      return new Response(Status.NOT_FOUND, userAlreadyRegisteredAnswer);
    }

    String hashedPassword;

    try {
      hashedPassword = hashGenerator.hashPepperSalt(password);
    } catch (HashGeneratorException e) {
      logger.error(() -> "Error was caught during hash generation.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    User user;

    try {
      user = new User(User.DEFAULT_ID, name, login, hashedPassword, Role.USER);
    } catch (ValidationException e) {
      logger.warn(() -> "Validation error was caught during creating new user.", e);
      return new Response(Status.BAD_REQUEST, e.getMessage());
    }

    try {
      userRepository.insert(user);
    } catch (RepositoryException e) {
      logger.error(() -> "Cannot insert new user.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    String jws = Jwts.builder().setSubject(subject).signWith(key).compact();
    logger.info(() -> "Json web signature was created.");

    return new Response(Status.OK, registeredAnswer, jws);
  }
}
