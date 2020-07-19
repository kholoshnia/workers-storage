package ru.storage.server.model.source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.exitManager.exceptions.ExitingException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Abstract class that encapsulates all methods for working with statements of a specified SQL
 * database.
 *
 * @see Connection
 * @see PreparedStatement
 */
public abstract class DataSource implements ExitListener {
  private static final String SETUP_CONNECTION_EXCEPTION;
  private static final String GET_PREPARED_STATEMENT_EXCEPTION;
  private static final String CLOSE_PREPARED_STATEMENT_EXCEPTION;
  private static final String CLOSE_CONNECTION_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.DataSource");

    SETUP_CONNECTION_EXCEPTION = resourceBundle.getString("exceptions.setupConnection");
    GET_PREPARED_STATEMENT_EXCEPTION = resourceBundle.getString("exceptions.getPreparedStatement");
    CLOSE_PREPARED_STATEMENT_EXCEPTION =
        resourceBundle.getString("exceptions.closePreparedStatement");
    CLOSE_CONNECTION_EXCEPTION = resourceBundle.getString("exceptions.closeConnection");
  }

  private final Logger logger;
  private final Connection connection;

  public DataSource(String url, String user, String password) throws DataSourceException {
    logger = LogManager.getLogger(DataSource.class);
    connection = initConnection(url, user, password);
  }

  /**
   * Initializes connection with database.
   *
   * <p>NOTE: requires database URL, user and password.
   *
   * <p>Creates and returns a new connection with database based on the specified database url, user
   * and password using {@link DriverManager}.
   *
   * @param url database url
   * @param user database user
   * @param password database password
   * @return new connection (session) with database
   * @throws DataSourceException - in case of establishing connection errors
   * @see Connection
   * @see DriverManager
   */
  private Connection initConnection(String url, String user, String password)
      throws DataSourceException {
    Connection connection;

    try {
      connection = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      logger.fatal(() -> "Cannot connect to the database", e);
      throw new DataSourceException(SETUP_CONNECTION_EXCEPTION, e);
    }

    logger.debug(() -> "Connection setup was established.");
    return connection;
  }

  /**
   * Returns prepared statement for specified sql statement and type.
   *
   * @param statement sql statement
   * @param type concrete type
   * @return prepared statement for specified sql request and type
   * @throws DataSourceException - if preparation is incorrect
   */
  public final PreparedStatement getPrepareStatement(String statement, int type)
      throws DataSourceException {
    PreparedStatement preparedStatement;

    try {
      preparedStatement = connection.prepareStatement(statement, type);
    } catch (SQLException e) {
      logger.error(
          "Statement for request: \"{}\" was not prepared", (Supplier<?>) () -> statement, e);
      throw new DataSourceException(GET_PREPARED_STATEMENT_EXCEPTION, e);
    }

    logger.info("Statement for request: \"{}\" was prepared", () -> statement);
    return preparedStatement;
  }

  /**
   * Closes prepared statement.
   *
   * <p>NOTE: all prepared statement must be closed after execution or in case of exception in
   * {@code finally} block.
   *
   * @param preparedStatement concrete prepared statement
   * @throws DataSourceException - in case of errors while closing prepared statement
   */
  public final void closePrepareStatement(PreparedStatement preparedStatement)
      throws DataSourceException {
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        logger.error(() -> "Statement was not closed.", e);
        throw new DataSourceException(CLOSE_PREPARED_STATEMENT_EXCEPTION, e);
      }
    }

    logger.debug(() -> "Statement was closed.");
  }

  /**
   * Closes connection with database.
   *
   * <p>NOTE: all database connections must be closed on program exit.
   *
   * @throws DataSourceException - in case of errors while closing connection
   */
  public final void closeConnection() throws DataSourceException {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.fatal(() -> "Cannot close connection.", e);
        throw new DataSourceException(CLOSE_CONNECTION_EXCEPTION, e);
      }
    }

    logger.debug(() -> "Connection was closed.");
  }

  @Override
  public void exit() throws ExitingException {
    try {
      closeConnection();
    } catch (DataSourceException e) {
      logger.fatal(() -> "Cannot close connection with database.", e);
      throw new ExitingException(e);
    }

    logger.debug(() -> "Connection with database was closed.");
  }
}
