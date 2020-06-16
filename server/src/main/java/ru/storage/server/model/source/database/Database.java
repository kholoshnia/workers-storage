package ru.storage.server.model.source.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.database.exceptions.DatabaseException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/** Database class that initialize tables. */
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
          + User.TABLE_NAME
          + " ("
          + User.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + User.NAME_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + User.NAME_COLUMN
          + ")>=2) CHECK(LENGTH("
          + User.NAME_COLUMN
          + ")<=100), "
          + User.LOGIN_COLUMN
          + " VARCHAR NOT NULL UNIQUE CHECK(LENGTH("
          + User.LOGIN_COLUMN
          + ")>=2) CHECK(LENGTH("
          + User.LOGIN_COLUMN
          + ")<=100), "
          + User.PASSWORD_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + User.PASSWORD_COLUMN
          + ")>=8) CHECK(LENGTH("
          + User.PASSWORD_COLUMN
          + ")<=100), "
          + User.ROLE_COLUMN
          + " VARCHAR NOT NULL, "
          + User.STATE_COLUMN
          + " BOOL NOT NULL)";

  private final String CREATE_IF_NOT_EXISTS_COORDINATES_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + Coordinates.TABLE_NAME
          + " ("
          + Coordinates.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + Coordinates.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + Coordinates.X_COLUMN
          + " REAL NOT NULL CHECK("
          + Coordinates.X_COLUMN
          + ">=-500) CHECK("
          + Coordinates.X_COLUMN
          + "<=500), "
          + Coordinates.Y_COLUMN
          + " REAL NOT NULL CHECK("
          + Coordinates.Y_COLUMN
          + ">=-500) CHECK("
          + Coordinates.Y_COLUMN
          + "<=500), "
          + Coordinates.Z_COLUMN
          + " REAL NOT NULL CHECK("
          + Coordinates.Z_COLUMN
          + ">=-500) CHECK("
          + Coordinates.Z_COLUMN
          + "<=500), FOREIGN KEY ("
          + Coordinates.OWNER_ID_COLUMN
          + ") REFERENCES "
          + User.TABLE_NAME
          + "("
          + User.ID_COLUMN
          + "))";

  private final String CREATE_IF_NOT_EXISTS_LOCATIONS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + Location.TABLE_NAME
          + " ("
          + Location.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + Location.OWNER_ID_COLUMN
          + " SERIAL NOT NULL , "
          + Location.ADDRESS_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + Location.ADDRESS_COLUMN
          + ")>=10) CHECK(LENGTH("
          + Location.ADDRESS_COLUMN
          + ")<=100), "
          + Location.LATITUDE_COLUMN
          + " REAL NOT NULL CHECK("
          + Location.LATITUDE_COLUMN
          + ">=-85) CHECK("
          + Location.LATITUDE_COLUMN
          + "<=85), "
          + Location.LONGITUDE_COLUMN
          + " REAL NOT NULL CHECK("
          + Location.LONGITUDE_COLUMN
          + ">=-180) CHECK("
          + Location.LONGITUDE_COLUMN
          + "<=180), FOREIGN KEY ("
          + Location.OWNER_ID_COLUMN
          + ") REFERENCES "
          + User.TABLE_NAME
          + "("
          + User.ID_COLUMN
          + "))";

  private final String CREATE_IF_NOT_EXISTS_PERSONS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + Person.TABLE_NAME
          + " ("
          + Person.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + Person.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + Person.NAME_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + Person.NAME_COLUMN
          + ")>=10) CHECK(LENGTH("
          + Person.NAME_COLUMN
          + ")<=100), "
          + Person.PASSPORT_ID_COLUMN
          + " VARCHAR NOT NULL CHECK(LENGTH("
          + Person.PASSPORT_ID_COLUMN
          + ")>=10) CHECK(LENGTH("
          + Person.PASSPORT_ID_COLUMN
          + ")<=40), "
          + Person.LOCATION_COLUMN
          + " SERIAL NOT NULL, FOREIGN KEY ("
          + Person.LOCATION_COLUMN
          + ") REFERENCES "
          + Location.TABLE_NAME
          + "("
          + Location.ID_COLUMN
          + "), FOREIGN KEY ("
          + Person.OWNER_ID_COLUMN
          + ") REFERENCES "
          + User.TABLE_NAME
          + "("
          + User.ID_COLUMN
          + "))";

  private final String CREATE_IF_NOT_EXISTS_WORKERS_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + Worker.TABLE_NAME
          + " ("
          + Worker.ID_COLUMN
          + " SERIAL NOT NULL PRIMARY KEY, "
          + Worker.OWNER_ID_COLUMN
          + " SERIAL NOT NULL, "
          + Worker.CREATION_DATE_COLUMN
          + " DATE NOT NULL, "
          + Worker.SALARY_COLUMN
          + " REAL NOT NULL CHECK("
          + Worker.SALARY_COLUMN
          + ">0), "
          + Worker.STATUS_COLUMN
          + " VARCHAR NOT NULL, "
          + Worker.START_DATE_COLUMN
          + " DATE NOT NULL, "
          + Worker.END_DATE_COLUMN
          + " DATE NOT NULL, "
          + Worker.COORDINATES_COLUMN
          + " SERIAL NOT NULL, "
          + Worker.PERSON_COLUMN
          + " SERIAL NOT NULL, FOREIGN KEY ("
          + Worker.COORDINATES_COLUMN
          + ") REFERENCES "
          + Coordinates.TABLE_NAME
          + "("
          + Coordinates.ID_COLUMN
          + "), FOREIGN KEY ("
          + Worker.PERSON_COLUMN
          + ") REFERENCES "
          + Person.TABLE_NAME
          + "("
          + Person.ID_COLUMN
          + "), FOREIGN KEY ("
          + Worker.OWNER_ID_COLUMN
          + ") REFERENCES "
          + User.TABLE_NAME
          + "("
          + User.ID_COLUMN
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
    this.logger = LogManager.getLogger(Database.class);

    initUsersTable();
    initCoordinatesTable();
    initLocationsTable();
    initPersonsTable();
    initWorkersTable();

    logger.debug(() -> "All tables were initialized.");
  }

  /**
   * Initializes workers table. Creates new one if not exists.
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

    logger.debug(() -> "Coordinates table was initialized.");
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
   * @throws DatabaseException if initialization is incorrect
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
