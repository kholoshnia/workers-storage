package ru.storage.server.model.source.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.server.model.domain.dto.dtos.*;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.database.exceptions.DatabaseException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/** Database class is used to initialize tables. */
public final class Database extends DataSource {
  private static final String INIT_USERS_TABLE_EXCEPTION;
  private static final String INIT_WORKERS_TABLE_EXCEPTION;
  private static final String INIT_COORDINATES_TABLE_EXCEPTION;
  private static final String INIT_PERSONS_TABLE_EXCEPTION;
  private static final String INIT_LOCATIONS_TABLE_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Database");

    INIT_USERS_TABLE_EXCEPTION = resourceBundle.getString("exceptions.initUsersTable");
    INIT_WORKERS_TABLE_EXCEPTION = resourceBundle.getString("exceptions.initWorkersTable");
    INIT_COORDINATES_TABLE_EXCEPTION = resourceBundle.getString("exceptions.initCoordinatesTable");
    INIT_PERSONS_TABLE_EXCEPTION = resourceBundle.getString("exceptions.initPersonsTable");
    INIT_LOCATIONS_TABLE_EXCEPTION = resourceBundle.getString("exceptions.initLocationsTable");
  }

  private final String CREATE_IF_NOT_EXISTS_USERS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + UserDTO.TABLE_NAME
          + " ("
          + UserDTO.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + UserDTO.NAME_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + UserDTO.NAME_COLUMN
          + ")>=2) CHECK(LENGTH("
          + UserDTO.NAME_COLUMN
          + ")<=100), "
          + UserDTO.LOGIN_COLUMN
          + " VARCHAR NOT NULL UNIQUE CHECK(LENGTH("
          + UserDTO.LOGIN_COLUMN
          + ")>=2) CHECK(LENGTH("
          + UserDTO.LOGIN_COLUMN
          + ")<=100), "
          + UserDTO.PASSWORD_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + UserDTO.PASSWORD_COLUMN
          + ")>=8) CHECK(LENGTH("
          + UserDTO.PASSWORD_COLUMN
          + ")<=100), "
          + UserDTO.ROLE_COLUMN
          + " VARCHAR NOT NULL)";

  private final String CREATE_IF_NOT_EXISTS_COORDINATES_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + CoordinatesDTO.TABLE_NAME
          + " ("
          + CoordinatesDTO.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + CoordinatesDTO.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + CoordinatesDTO.X_COLUMN
          + " DOUBLE PRECISION NOT NULL CHECK("
          + CoordinatesDTO.X_COLUMN
          + ">=-500) CHECK("
          + CoordinatesDTO.X_COLUMN
          + "<=500), "
          + CoordinatesDTO.Y_COLUMN
          + " DOUBLE PRECISION NOT NULL CHECK("
          + CoordinatesDTO.Y_COLUMN
          + ">=-500) CHECK("
          + CoordinatesDTO.Y_COLUMN
          + "<=500), "
          + CoordinatesDTO.Z_COLUMN
          + " DOUBLE PRECISION NULL CHECK("
          + CoordinatesDTO.Z_COLUMN
          + ">=-500) CHECK("
          + CoordinatesDTO.Z_COLUMN
          + "<=500), FOREIGN KEY ("
          + CoordinatesDTO.OWNER_ID_COLUMN
          + ") REFERENCES "
          + UserDTO.TABLE_NAME
          + "("
          + UserDTO.ID_COLUMN
          + "))";

  private final String CREATE_IF_NOT_EXISTS_LOCATIONS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + LocationDTO.TABLE_NAME
          + " ("
          + LocationDTO.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + LocationDTO.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + LocationDTO.ADDRESS_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + LocationDTO.ADDRESS_COLUMN
          + ")>=10) CHECK(LENGTH("
          + LocationDTO.ADDRESS_COLUMN
          + ")<=100), "
          + LocationDTO.LATITUDE_COLUMN
          + " DOUBLE PRECISION NULL CHECK("
          + LocationDTO.LATITUDE_COLUMN
          + ">=-85) CHECK("
          + LocationDTO.LATITUDE_COLUMN
          + "<=85), "
          + LocationDTO.LONGITUDE_COLUMN
          + " DOUBLE PRECISION NULL CHECK("
          + LocationDTO.LONGITUDE_COLUMN
          + ">=-180) CHECK("
          + LocationDTO.LONGITUDE_COLUMN
          + "<=180), FOREIGN KEY ("
          + LocationDTO.OWNER_ID_COLUMN
          + ") REFERENCES "
          + UserDTO.TABLE_NAME
          + "("
          + UserDTO.ID_COLUMN
          + "))";

  private final String CREATE_IF_NOT_EXISTS_PERSONS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + PersonDTO.TABLE_NAME
          + " ("
          + PersonDTO.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + PersonDTO.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + PersonDTO.NAME_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + PersonDTO.NAME_COLUMN
          + ")>=2) CHECK(LENGTH("
          + PersonDTO.NAME_COLUMN
          + ")<=100), "
          + PersonDTO.PASSPORT_ID_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + PersonDTO.PASSPORT_ID_COLUMN
          + ")>=10) CHECK(LENGTH("
          + PersonDTO.PASSPORT_ID_COLUMN
          + ")<=40), "
          + PersonDTO.LOCATION_COLUMN
          + " BIGINT NULL, FOREIGN KEY ("
          + PersonDTO.LOCATION_COLUMN
          + ") REFERENCES "
          + LocationDTO.TABLE_NAME
          + "("
          + LocationDTO.ID_COLUMN
          + "), FOREIGN KEY ("
          + PersonDTO.OWNER_ID_COLUMN
          + ") REFERENCES "
          + UserDTO.TABLE_NAME
          + "("
          + UserDTO.ID_COLUMN
          + "))";

  private final String CREATE_IF_NOT_EXISTS_WORKERS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + WorkerDTO.TABLE_NAME
          + " ("
          + WorkerDTO.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + WorkerDTO.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + WorkerDTO.CREATION_DATE_COLUMN
          + " TIMESTAMP WITH TIME ZONE NOT NULL, "
          + WorkerDTO.SALARY_COLUMN
          + " REAL NOT NULL CHECK("
          + WorkerDTO.SALARY_COLUMN
          + ">0), "
          + WorkerDTO.STATUS_COLUMN
          + " VARCHAR NULL, "
          + WorkerDTO.START_DATE_COLUMN
          + " TIMESTAMP WITH TIME ZONE NOT NULL, "
          + WorkerDTO.END_DATE_COLUMN
          + " TIMESTAMP WITH TIME ZONE NULL, "
          + WorkerDTO.COORDINATES_COLUMN
          + " BIGINT NULL, "
          + WorkerDTO.PERSON_COLUMN
          + " SERIAL NOT NULL, FOREIGN KEY ("
          + WorkerDTO.COORDINATES_COLUMN
          + ") REFERENCES "
          + CoordinatesDTO.TABLE_NAME
          + "("
          + CoordinatesDTO.ID_COLUMN
          + "), FOREIGN KEY ("
          + WorkerDTO.PERSON_COLUMN
          + ") REFERENCES "
          + PersonDTO.TABLE_NAME
          + "("
          + PersonDTO.ID_COLUMN
          + "), FOREIGN KEY ("
          + WorkerDTO.OWNER_ID_COLUMN
          + ") REFERENCES "
          + UserDTO.TABLE_NAME
          + "("
          + UserDTO.ID_COLUMN
          + "))";

  private final Logger logger;

  /**
   * Initializes all tables. NOTE: order is important.
   *
   * @param url concrete database url for {@link DriverManager}
   * @param user database user
   * @param password database password
   * @throws DataSourceException - in case of data source exceptions
   * @throws DatabaseException - if initialization is incorrect
   */
  public Database(String url, String user, String password)
      throws DataSourceException, DatabaseException {
    super(url, user, password);
    logger = LogManager.getLogger(Database.class);

    initUsersTable();
    initCoordinatesTable();
    initLocationsTable();
    initPersonsTable();
    initWorkersTable();

    logger.debug(() -> "All tables were initialized.");
  }

  /**
   * Initializes users table. Creates new one if not exists.
   *
   * @throws DatabaseException - if initialization is incorrect
   */
  private void initUsersTable() throws DataSourceException {
    PreparedStatement preparedStatement =
        getPrepareStatement(CREATE_IF_NOT_EXISTS_USERS_TABLE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(
          "Cannot create users table, query: {}.",
          (Supplier<?>) () -> CREATE_IF_NOT_EXISTS_USERS_TABLE,
          e);
      throw new DatabaseException(INIT_USERS_TABLE_EXCEPTION, e);
    } finally {
      closePrepareStatement(preparedStatement);
    }

    logger.debug(() -> "Users table was initialized.");
  }

  /**
   * Initializes coordinates table. Creates new one if not exists.
   *
   * @throws DatabaseException - if initialization is incorrect
   */
  private void initCoordinatesTable() throws DataSourceException {
    PreparedStatement preparedStatement =
        getPrepareStatement(CREATE_IF_NOT_EXISTS_COORDINATES_TABLE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(
          "Cannot initialize coordinates table, query: {}.",
          (Supplier<?>) () -> CREATE_IF_NOT_EXISTS_COORDINATES_TABLE,
          e);
      throw new DatabaseException(INIT_COORDINATES_TABLE_EXCEPTION, e);
    } finally {
      closePrepareStatement(preparedStatement);
    }

    logger.debug(() -> "CoordinatesDTO table was initialized.");
  }

  /**
   * Initializes persons table. Creates new one if not exists.
   *
   * @throws DatabaseException - if initialization is incorrect
   */
  private void initPersonsTable() throws DataSourceException {
    PreparedStatement preparedStatement =
        getPrepareStatement(CREATE_IF_NOT_EXISTS_PERSONS_TABLE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(
          "Cannot initialize persons table, query: {}.",
          (Supplier<?>) () -> CREATE_IF_NOT_EXISTS_PERSONS_TABLE,
          e);
      throw new DatabaseException(INIT_PERSONS_TABLE_EXCEPTION, e);
    } finally {
      closePrepareStatement(preparedStatement);
    }

    logger.debug(() -> "Persons table was initialized.");
  }

  /**
   * Initializes locations table. Creates new one if not exists.
   *
   * @throws DatabaseException - if initialization is incorrect
   */
  private void initLocationsTable() throws DataSourceException {
    PreparedStatement preparedStatement =
        getPrepareStatement(CREATE_IF_NOT_EXISTS_LOCATIONS_TABLE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(
          "Cannot initialize locations table, query: {}.",
          (Supplier<?>) () -> CREATE_IF_NOT_EXISTS_LOCATIONS_TABLE,
          e);
      throw new DatabaseException(INIT_LOCATIONS_TABLE_EXCEPTION, e);
    } finally {
      closePrepareStatement(preparedStatement);
    }

    logger.debug(() -> "Locations table was initialized.");
  }

  /**
   * Initializes workers table. Creates new one if not exists.
   *
   * @throws DatabaseException - if initialization is incorrect
   */
  private void initWorkersTable() throws DataSourceException {
    PreparedStatement preparedStatement =
        getPrepareStatement(CREATE_IF_NOT_EXISTS_WORKERS_TABLE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(
          "Cannot create workers table, query: {}.",
          (Supplier<?>) () -> CREATE_IF_NOT_EXISTS_WORKERS_TABLE,
          e);
      throw new DatabaseException(INIT_WORKERS_TABLE_EXCEPTION, e);
    } finally {
      closePrepareStatement(preparedStatement);
    }

    logger.debug(() -> "Workers table was initialized.");
  }
}
