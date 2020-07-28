package ru.storage.server.controller.controllers.command.factory;

import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.controllers.command.factory.exceptions.UserNotFoundException;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.userRepository.queries.GetEqualsLoginUsers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class CommandFactory {
  /**
   * Creates new command instance.
   *
   * @param command user command
   * @param arguments command arguments
   * @param locale command response locale
   * @param login caller user
   * @return new command instance
   * @throws CommandFactoryException - in case of command creation errors
   */
  public abstract Command createCommand(
      String command, Map<String, String> arguments, Locale locale, String login)
      throws CommandFactoryException;

  /**
   * Returns user found by login.
   *
   * @param userRepository user repository
   * @param login user login
   * @return found user
   * @throws UserNotFoundException - if user was not found
   */
  protected final User getUser(Repository<User> userRepository, String login)
      throws UserNotFoundException {
    if (login.isEmpty()) {
      throw new UserNotFoundException();
    }

    Query<User> query = new GetEqualsLoginUsers(login);
    List<User> equalLoginUsers;

    try {
      equalLoginUsers = userRepository.get(query);
    } catch (RepositoryException e) {
      throw new UserNotFoundException();
    }

    for (User user : equalLoginUsers) {
      return user;
    }

    throw new UserNotFoundException();
  }
}
