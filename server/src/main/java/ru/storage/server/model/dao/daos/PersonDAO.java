package ru.storage.server.model.dao.daos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PersonDAO implements DAO<Long, Person> {
  private static final String CANNOT_GET_ALL_PERSON_EXCEPTION_MESSAGE;
  private static final String CANNOT_GET_PERSON_BY_ID_EXCEPTION_MESSAGE;
  private static final String CANNOT_INSERT_PERSON_EXCEPTION_MESSAGE;
  private static final String CANNOT_UPDATE_PERSON_EXCEPTION_MESSAGE;
  private static final String CANNOT_DELETE_PERSON_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.PersonDAO");

    CANNOT_GET_ALL_PERSON_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetAllPersons");
    CANNOT_GET_PERSON_BY_ID_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetPersonById");
    CANNOT_INSERT_PERSON_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotInsertPerson");
    CANNOT_UPDATE_PERSON_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotUpdatePerson");
    CANNOT_DELETE_PERSON_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotDeletePerson");
  }

  private final String SELECT_ALL = "SELECT * FROM " + Person.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + Person.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + Person.TABLE_NAME
          + " ("
          + Person.OWNER_ID_COLUMN
          + ", "
          + Person.NAME_COLUMN
          + ", "
          + Person.PASSPORT_ID_COLUMN
          + ", "
          + Person.LOCATION_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + Person.TABLE_NAME
          + " SET "
          + Person.OWNER_ID_COLUMN
          + " = ?, "
          + Person.NAME_COLUMN
          + " = ?, "
          + Person.PASSPORT_ID_COLUMN
          + " = ?, "
          + Person.LOCATION_COLUMN
          + " = ? WHERE "
          + Person.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + Person.TABLE_NAME + " WHERE " + Person.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;
  private final DAO<Long, Location> locationDAO;

  public PersonDAO(DataSource dataSource, DAO<Long, Location> locationDAO) {
    this.logger = LogManager.getLogger(PersonDAO.class);
    this.dataSource = dataSource;
    this.locationDAO = locationDAO;
  }

  @Override
  public List<Person> getAll() throws DAOException, DataSourceException {
    List<Person> allPersons = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getLong(Person.ID_COLUMN);
        Long ownerId = resultSet.getLong(Person.OWNER_ID_COLUMN);
        String name = resultSet.getString(Person.NAME_COLUMN);
        String passportID = resultSet.getString(Person.PASSPORT_ID_COLUMN);
        Location location = locationDAO.getByKey(resultSet.getLong(Person.LOCATION_COLUMN));

        Person person = new Person(id, ownerId, name, passportID, location);
        allPersons.add(person);
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot get all persons.", e);
      throw new DAOException(CANNOT_GET_ALL_PERSON_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Got all persons SUCCESSFULLY.");
    return allPersons;
  }

  @Override
  public Person getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    Person person = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long ownerId = resultSet.getLong(Person.OWNER_ID_COLUMN);
        String name = resultSet.getString(Person.NAME_COLUMN);
        String passportID = resultSet.getString(Person.PASSPORT_ID_COLUMN);
        Location location = locationDAO.getByKey(resultSet.getLong(Person.LOCATION_COLUMN));

        person = new Person(id, ownerId, name, passportID, location);
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot get person by id.", e);
      throw new DAOException(CANNOT_GET_PERSON_BY_ID_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Got person by id SUCCESSFULLY.");
    return person;
  }

  @Override
  public Person insert(@Nonnull Person person) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, person.getOwnerID());
      preparedStatement.setString(2, person.getName());
      preparedStatement.setString(3, person.getPassportID());

      Location location = locationDAO.insert(person.getLocation());
      person.setLocation(location);
      preparedStatement.setDouble(4, person.getLocation().getID());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        person.setID(generatedKeys.getLong(1));
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot insert person.", e);
      throw new DAOException(CANNOT_INSERT_PERSON_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Person was inserted SUCCESSFULLY.");
    return person;
  }

  @Override
  public Person update(@Nonnull Person person) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, person.getOwnerID());
      preparedStatement.setString(2, person.getName());
      preparedStatement.setString(3, person.getPassportID());

      Location location = locationDAO.update(person.getLocation());
      person.setLocation(location);
      preparedStatement.setDouble(4, person.getLocation().getID());
      preparedStatement.setLong(5, person.getID());

      preparedStatement.execute();
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot update person.", e);
      throw new DAOException(CANNOT_UPDATE_PERSON_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Person was updated SUCCESSFULLY.");
    return person;
  }

  @Override
  public void delete(@Nonnull Person person) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, person.getID());

      locationDAO.delete(person.getLocation());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error("Cannot delete person.", e);
      throw new DAOException(CANNOT_DELETE_PERSON_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Person was deleted SUCCESSFULLY.");
  }
}
