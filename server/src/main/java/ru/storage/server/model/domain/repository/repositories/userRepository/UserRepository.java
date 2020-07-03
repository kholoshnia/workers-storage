package ru.storage.server.model.domain.repository.repositories.userRepository;

import com.google.inject.Inject;
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
  private static final String USER_NOT_FOUND_USING_DAO_EXCEPTION;
  private static final String USER_NOT_FOUND_IN_COLLECTION_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.UserRepository");

    USER_NOT_FOUND_USING_DAO_EXCEPTION =
        resourceBundle.getString("exceptions.userNotFoundUsingDAO");
    USER_NOT_FOUND_IN_COLLECTION_EXCEPTION =
        resourceBundle.getString("exceptions.userNotFoundInCollection");
  }

  private final Logger logger;
  private final DAO<String, User> userDAO;
  private final List<User> users;

  @Inject
  public UserRepository(@Nonnull DAO<String, User> userDAO) throws UserRepositoryException {
    this.logger = LogManager.getLogger(UserRepository.class);
    this.userDAO = userDAO;
    this.users = initUsersList();
  }

  /**
   * Initializes list of users. Loads data using DAO. If there is no data in the DAO, creates an
   * empty list.
   *
   * @return list of users
   * @throws UserRepositoryException - in case of corrupted data or exceptions while loading data.
   */
  private List<User> initUsersList() throws UserRepositoryException {
    List<User> users = new CopyOnWriteArrayList<>();

    try {
      List<User> allUsers = userDAO.getAll();

      if (allUsers.isEmpty()) {
        logger.warn(() -> "No users were found using DAO, the collection was not filled.");
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

    logger.debug("User query: {} has been executed.", () -> query);
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

    logger.info(() -> "User has been added to the collection.");
  }

  @Override
  public synchronized void update(@Nonnull User user) throws UserRepositoryException {
    try {
      User result = userDAO.getByKey(user.getLogin());

      if (result == null) {
        logger.error(() -> "Cannot update user, no such user found using DAO.");
        throw new UserRepositoryException(USER_NOT_FOUND_USING_DAO_EXCEPTION);
      }

      userDAO.update(user);
      logger.info(() -> "User has been updated in DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot update user in DAO.", e);
      throw new UserRepositoryException(e);
    }

    if (users.remove(user)) {
      logger.error(() -> "Cannot delete user, no such user in the collection.");
      throw new UserRepositoryException(USER_NOT_FOUND_IN_COLLECTION_EXCEPTION);
    }

    users.add(user);

    logger.info(() -> "User has been updated in the collection.");
  }

  @Override
  public synchronized void delete(@Nonnull User user) throws UserRepositoryException {
    try {
      User result = userDAO.getByKey(user.getLogin());

      if (result == null) {
        logger.error(() -> "Cannot delete worker, no such worker found using DAO.");
        throw new UserRepositoryException(USER_NOT_FOUND_USING_DAO_EXCEPTION);
      }

      userDAO.delete(user);
      logger.info(() -> "User has been deleted from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot delete user using DAO.");
      throw new UserRepositoryException(e);
    }

    if (users.remove(user)) {
      logger.error(() -> "Cannot delete user, no such user in the collection.");
      throw new UserRepositoryException(USER_NOT_FOUND_IN_COLLECTION_EXCEPTION);
    }

    logger.info(() -> "User has been deleted from the collection.");
  }
}
