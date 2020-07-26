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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PersonDAO implements DAO<Long, PersonDTO> {
  private static final String SELECT_ALL = "SELECT * FROM " + PersonDTO.TABLE_NAME;

  private static final String SELECT_BY_ID = SELECT_ALL + " WHERE " + PersonDTO.ID_COLUMN + " = ?";

  private static final String INSERT =
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

  private static final String UPDATE =
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

  private static final String DELETE =
      "DELETE FROM " + PersonDTO.TABLE_NAME + " WHERE " + PersonDTO.ID_COLUMN + " = ?";

  private static final String GET_ALL_PERSON_EXCEPTION;
  private static final String GET_PERSON_BY_ID_EXCEPTION;
  private static final String INSERT_PERSON_EXCEPTION;
  private static final String GET_GENERATED_PERSON_ID;
  private static final String UPDATE_PERSON_EXCEPTION;
  private static final String DELETE_PERSON_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.PersonDAO");

    GET_ALL_PERSON_EXCEPTION = resourceBundle.getString("exceptions.getAllPersons");
    GET_PERSON_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.getPersonById");
    INSERT_PERSON_EXCEPTION = resourceBundle.getString("exceptions.insertPerson");
    GET_GENERATED_PERSON_ID = resourceBundle.getString("exceptions.getGeneratedPersonId");
    UPDATE_PERSON_EXCEPTION = resourceBundle.getString("exceptions.updatePerson");
    DELETE_PERSON_EXCEPTION = resourceBundle.getString("exceptions.deletePerson");
  }

  private final Logger logger;
  private final DataSource dataSource;
  private final DAO<Long, LocationDTO> locationDAO;

  @Inject
  public PersonDAO(DataSource dataSource, DAO<Long, LocationDTO> locationDAO) {
    logger = LogManager.getLogger(PersonDAO.class);
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
        long id = resultSet.getLong(PersonDTO.ID_COLUMN);
        long ownerId = resultSet.getLong(PersonDTO.OWNER_ID_COLUMN);
        String name = resultSet.getObject(PersonDTO.NAME_COLUMN, String.class);
        String passportId = resultSet.getObject(PersonDTO.PASSPORT_ID_COLUMN, String.class);

        long locationId = resultSet.getLong(PersonDTO.LOCATION_COLUMN);
        LocationDTO locationDTO = locationDAO.getByKey(locationId);

        PersonDTO personDTO = new PersonDTO(id, ownerId, name, passportId, locationDTO);
        allPersonDTOs.add(personDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get all persons.", e);
      throw new DAOException(GET_ALL_PERSON_EXCEPTION, e);
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
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long ownerId = resultSet.getLong(PersonDTO.OWNER_ID_COLUMN);
        String name = resultSet.getString(PersonDTO.NAME_COLUMN);
        String passportId = resultSet.getString(PersonDTO.PASSPORT_ID_COLUMN);

        long locationId = resultSet.getLong(PersonDTO.LOCATION_COLUMN);
        LocationDTO locationDTO = locationDAO.getByKey(locationId);

        personDTO = new PersonDTO(id, ownerId, name, passportId, locationDTO);
      }
    } catch (SQLException e) {
      logger.error("Cannot get person by id.", e);
      throw new DAOException(GET_PERSON_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got person by id.");
    return personDTO;
  }

  @Override
  public PersonDTO insert(@Nonnull PersonDTO personDTO) throws DAOException, DataSourceException {
    long resultId;
    LocationDTO locationDTO;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, personDTO.ownerId);
      preparedStatement.setString(2, personDTO.name);
      preparedStatement.setString(3, personDTO.passportId);

      if (personDTO.locationDTO != null) {
        locationDTO = locationDAO.insert(personDTO.locationDTO);
        preparedStatement.setDouble(4, locationDTO.id);
      } else {
        locationDTO = null;
        preparedStatement.setNull(4, Types.INTEGER);
      }

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getLong(1);
      } else {
        logger.error(() -> "Cannot get generated person id.");
        throw new DAOException(GET_GENERATED_PERSON_ID);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot insert person.", e);
      throw new DAOException(INSERT_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Person was inserted.");
    return new PersonDTO(
        resultId, personDTO.ownerId, personDTO.name, personDTO.passportId, locationDTO);
  }

  @Override
  public PersonDTO update(@Nonnull PersonDTO personDTO) throws DAOException, DataSourceException {
    PersonDTO previous = getByKey(personDTO.id);
    LocationDTO locationDTO;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, personDTO.ownerId);
      preparedStatement.setString(2, personDTO.name);
      preparedStatement.setString(3, personDTO.passportId);

      if (previous.locationDTO == null && personDTO.locationDTO != null) {
        locationDTO = locationDAO.insert(personDTO.locationDTO);
        preparedStatement.setLong(4, locationDTO.id);
      } else if (previous.locationDTO != null && personDTO.locationDTO == null) {
        locationDTO = null;
        preparedStatement.setNull(4, Types.INTEGER);
      } else if (previous.locationDTO != null) {
        locationDTO = locationDAO.update(personDTO.locationDTO);
        preparedStatement.setLong(4, locationDTO.id);
      } else {
        locationDTO = null;
        preparedStatement.setNull(4, Types.INTEGER);
      }

      preparedStatement.setLong(5, personDTO.id);

      preparedStatement.execute();

      if (previous.locationDTO != null && personDTO.locationDTO == null) {
        locationDAO.delete(previous.locationDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot update person.", e);
      throw new DAOException(UPDATE_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Person was updated.");
    return new PersonDTO(
        personDTO.id, personDTO.ownerId, personDTO.name, personDTO.passportId, locationDTO);
  }

  @Override
  public void delete(@Nonnull PersonDTO personDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, personDTO.id);

      preparedStatement.execute();

      if (personDTO.locationDTO != null) {
        locationDAO.delete(personDTO.locationDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete person.", e);
      throw new DAOException(DELETE_PERSON_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Person was deleted.");
  }
}
