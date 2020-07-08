package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
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

public class PersonDAO implements DAO<Long, PersonDTO> {
  private static final String CANNOT_GET_ALL_PERSON_EXCEPTION;
  private static final String CANNOT_GET_PERSON_BY_ID_EXCEPTION;
  private static final String CANNOT_INSERT_PERSON_EXCEPTION;
  private static final String CANNOT_GET_GENERATED_PERSON_ID;
  private static final String CANNOT_UPDATE_PERSON_EXCEPTION;
  private static final String CANNOT_DELETE_PERSON_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.PersonDAO");

    CANNOT_GET_ALL_PERSON_EXCEPTION = resourceBundle.getString("exceptions.cannotGetAllPersons");
    CANNOT_GET_PERSON_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.cannotGetPersonById");
    CANNOT_INSERT_PERSON_EXCEPTION = resourceBundle.getString("exceptions.cannotInsertPerson");
    CANNOT_GET_GENERATED_PERSON_ID =
        resourceBundle.getString("exceptions.cannotGetGeneratedPersonId");
    CANNOT_UPDATE_PERSON_EXCEPTION = resourceBundle.getString("exceptions.cannotUpdatePerson");
    CANNOT_DELETE_PERSON_EXCEPTION = resourceBundle.getString("exceptions.cannotDeletePerson");
  }

  private final String SELECT_ALL = "SELECT * FROM " + PersonDTO.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + PersonDTO.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + PersonDTO.TABLE_NAME
          + " ("
          + PersonDTO.OWNER_ID_COLUMN
          + ", "
          + PersonDTO.NAME_COLUMN
          + ", "
          + PersonDTO.PASSPORT_ID_COLUMN
          + ", "
          + PersonDTO.LOCATION_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + PersonDTO.TABLE_NAME
          + " SET "
          + PersonDTO.OWNER_ID_COLUMN
          + " = ?, "
          + PersonDTO.NAME_COLUMN
          + " = ?, "
          + PersonDTO.PASSPORT_ID_COLUMN
          + " = ?, "
          + PersonDTO.LOCATION_COLUMN
          + " = ? WHERE "
          + PersonDTO.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + PersonDTO.TABLE_NAME + " WHERE " + PersonDTO.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;
  private final DAO<Long, LocationDTO> locationDAO;

  @Inject
  public PersonDAO(DataSource dataSource, DAO<Long, LocationDTO> locationDAO) {
    this.logger = LogManager.getLogger(PersonDAO.class);
    this.dataSource = dataSource;
    this.locationDAO = locationDAO;
  }

  @Override
  public List<PersonDTO> getAll() throws DAOException, DataSourceException {
    List<PersonDTO> allPersonDTOs = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getObject(PersonDTO.ID_COLUMN, Long.class);
        Long ownerId = resultSet.getObject(PersonDTO.OWNER_ID_COLUMN, Long.class);
        String name = resultSet.getObject(PersonDTO.NAME_COLUMN, String.class);
        String passportId = resultSet.getObject(PersonDTO.PASSPORT_ID_COLUMN, String.class);

        Long locationId = resultSet.getObject(PersonDTO.LOCATION_COLUMN, Long.class);
        LocationDTO locationDTO = locationDAO.getByKey(locationId);

        PersonDTO personDTO = new PersonDTO(id, ownerId, name, passportId, locationDTO);
        allPersonDTOs.add(personDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get all persons.", e);
      throw new DAOException(CANNOT_GET_ALL_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all persons.");
    return allPersonDTOs;
  }

  @Override
  public PersonDTO getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    PersonDTO personDTO = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long ownerId = resultSet.getLong(PersonDTO.OWNER_ID_COLUMN);
        String name = resultSet.getString(PersonDTO.NAME_COLUMN);
        String passportId = resultSet.getString(PersonDTO.PASSPORT_ID_COLUMN);

        Long locationId = resultSet.getObject(PersonDTO.LOCATION_COLUMN, Long.class);
        LocationDTO locationDTO = locationDAO.getByKey(locationId);

        personDTO = new PersonDTO(id, ownerId, name, passportId, locationDTO);
      }
    } catch (SQLException e) {
      logger.error("Cannot get person by id.", e);
      throw new DAOException(CANNOT_GET_PERSON_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got person by id.");
    return personDTO;
  }

  @Override
  public PersonDTO insert(@Nonnull PersonDTO personDTO) throws DAOException, DataSourceException {
    Long resultId;
    LocationDTO locationDTO;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, personDTO.ownerId);
      preparedStatement.setString(2, personDTO.name);
      preparedStatement.setString(3, personDTO.passportId);

      locationDTO = locationDAO.insert(personDTO.locationDTO);
      preparedStatement.setDouble(4, locationDTO.id);

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getObject(1, Long.class);
      } else {
        logger.error(() -> "Cannot get generated person id.");
        throw new DAOException(CANNOT_GET_GENERATED_PERSON_ID);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot insert person.", e);
      throw new DAOException(CANNOT_INSERT_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Person has been inserted.");
    return new PersonDTO(
        resultId, personDTO.ownerId, personDTO.name, personDTO.passportId, locationDTO);
  }

  @Override
  public PersonDTO update(@Nonnull PersonDTO personDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, personDTO.ownerId);
      preparedStatement.setString(2, personDTO.name);
      preparedStatement.setString(3, personDTO.passportId);

      LocationDTO locationDTO = locationDAO.insert(personDTO.locationDTO);
      preparedStatement.setDouble(4, locationDTO.id);

      preparedStatement.setLong(5, personDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot update person.", e);
      throw new DAOException(CANNOT_UPDATE_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Person has been updated.");
    return personDTO;
  }

  @Override
  public void delete(@Nonnull PersonDTO personDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, personDTO.id);

      locationDAO.delete(personDTO.locationDTO);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete person.", e);
      throw new DAOException(CANNOT_DELETE_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Person has been deleted.");
  }
}
