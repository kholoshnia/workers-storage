package ru.storage.server.model.domain.repository.repositories.userRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.userRepository.exceptions.UserRepositoryException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

public final class UserRepository implements Repository<User> {
  private static final String USER_NOT_FOUND_IN_COLLECTION_EXCEPTION_MESSAGE;
  private static final String USER_NOT_FOUND_IN_DAO_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.UserRepository");

    USER_NOT_FOUND_IN_COLLECTION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.userNotFoundInCollection");
    USER_NOT_FOUND_IN_DAO_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.userNotFoundInDAO");
  }

  private final Logger logger;
  private final DAO<String, User> userDAO;
  private final List<User> users;

  public UserRepository(@Nonnull DAO<String, User> userDAO) throws UserRepositoryException {
    this.logger = LogManager.getLogger(UserRepository.class);
    this.userDAO = userDAO;
    this.users = initUsersList();
  }

  /**
   * Initializes list of users. Loads data from DAO. If there is no data in the DAO, creates an
   * empty list.
   *
   * @return list of users.
   * @throws UserRepositoryException - in case of corrupted data or exceptions while loading data.
   */
  private List<User> initUsersList() throws UserRepositoryException {
    List<User> users = new CopyOnWriteArrayList<>();

    try {
      List<User> allUsers = userDAO.getAll();

      if (allUsers.isEmpty()) {
        logger.warn(() -> "No users were found in DAO, the collection was not filled.");
        return users;
      }

      for (User user : allUsers) {
        users.add(user);
        logger.debug(() -> "Added user from DAO.");
      }

      return users;
    } catch (DAOException | DataSourceException exception) {
      throw new UserRepositoryException(exception);
    }
  }

  @Override
  public synchronized List<User> get(@Nonnull Query<User> query) throws RepositoryException {
    if (users.isEmpty()) {
      return new CopyOnWriteArrayList<>();
    }

    List<User> result = query.execute(users);

    logger.debug("WorkerQuery: {} was executed.", () -> query);
    return result;
  }

  @Override
  public synchronized void insert(@Nonnull User user) throws UserRepositoryException {
    User result;

    try {
      result = userDAO.insert(user);
      logger.info(() -> "Got user with id from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot add new user to the collection.", e);
      throw new UserRepositoryException(e);
    }

    users.add(result);

    logger.info(() -> "User was added to the collection.");
  }

  @Override
  public synchronized void update(@Nonnull User user) throws UserRepositoryException {
    try {
      User result = userDAO.getByKey(user.getLogin());

      if (result == null) {
        logger.error(() -> "Cannot update user, no such user in DAO.");
        throw new UserRepositoryException(USER_NOT_FOUND_IN_DAO_EXCEPTION_MESSAGE);
      }

      userDAO.update(user);
      logger.info(() -> "User was updated in DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot update user in DAO.", e);
      throw new UserRepositoryException(e);
    }

    if (users.remove(user)) {
      logger.error(() -> "Cannot delete user, no such user in the collection.");
      throw new UserRepositoryException(USER_NOT_FOUND_IN_COLLECTION_EXCEPTION_MESSAGE);
    }

    users.add(user);

    logger.info(() -> "User was updated in the collection.");
  }

  @Override
  public synchronized void delete(@Nonnull User user) throws UserRepositoryException {
    try {
      User result = userDAO.getByKey(user.getLogin());

      if (result == null) {
        logger.error(() -> "Cannot delete worker, no such worker in DAO.");
        throw new UserRepositoryException(USER_NOT_FOUND_IN_DAO_EXCEPTION_MESSAGE);
      }

      userDAO.delete(user);
      logger.info(() -> "User was deleted from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot delete user using DAO.");
      throw new UserRepositoryException(e);
    }

    if (users.remove(user)) {
      logger.error(() -> "Cannot delete user, no such user in the collection.");
      throw new UserRepositoryException(USER_NOT_FOUND_IN_COLLECTION_EXCEPTION_MESSAGE);
    }

    logger.info(() -> "User was deleted from the collection.");
  }
}
