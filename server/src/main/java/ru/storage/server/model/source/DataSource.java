package ru.storage.server.model.source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.source.exceptions.DataSourceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Abstract class that encapsulates all methods to work with statements of a specific sql database.
 *
 * @see Connection
 * @see PreparedStatement
 */
public abstract class DataSource {
  private static final String SETUP_CONNECTION_EXCEPTION_MESSAGE;
  private static final String CLOSE_CONNECTION_EXCEPTION_MESSAGE;
  private static final String GET_PREPARED_STATEMENT_EXCEPTION_MESSAGE;
  private static final String CLOSE_PREPARED_STATEMENT_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.DataSource", Locale.ENGLISH);

    SETUP_CONNECTION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.setupConnection");
    CLOSE_CONNECTION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.closeConnection");
    GET_PREPARED_STATEMENT_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.getPreparedStatement");
    CLOSE_PREPARED_STATEMENT_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.closePreparedStatement");
  }

  private final Logger logger;
  private final Connection connection;

  public DataSource(String url, String user, String password) throws DataSourceException {
    this.logger = LogManager.getLogger(DataSource.class);
    this.connection = initConnection(url, user, password);
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
   * @throws DataSourceException - if connection is incorrect
   * @see Connection
   * @see DriverManager
   */
  private Connection initConnection(String url, String user, String password)
      throws DataSourceException {
    Connection connection;

    try {
      connection = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      logger.fatal("Cannot connect to database", e);
      throw new DataSourceException(SETUP_CONNECTION_EXCEPTION_MESSAGE, e);
    }

    logger.debug("Connection setup completed SUCCESSFULLY.");
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
      logger.error("Statement for request: \"" + statement + "\" was not prepared.", e);
      throw new DataSourceException(GET_PREPARED_STATEMENT_EXCEPTION_MESSAGE, e);
    }

    logger.debug("Statement for request: \"" + statement + "\" was prepared SUCCESSFULLY.");
    return preparedStatement;
  }

  /**
   * Closes prepared statement.
   *
   * <p>NOTE: all prepared statement must be closed after execution or in case of exception in
   * {@code finally} block.
   *
   * @param preparedStatement concrete prepared statement
   * @throws DataSourceException - if closure is incorrect
   */
  public final void closePrepareStatement(PreparedStatement preparedStatement)
      throws DataSourceException {
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        logger.error("Statement was not closed.", e);
        throw new DataSourceException(CLOSE_PREPARED_STATEMENT_EXCEPTION_MESSAGE, e);
      }
    }

    logger.debug("Statement was closed SUCCESSFULLY.");
  }

  /**
   * Closes connection with database.
   *
   * <p>NOTE: all database connections must be closed on program exit.
   *
   * @throws DataSourceException - in case of SQLException while closing connection
   */
  public final void closeConnection() throws DataSourceException {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.fatal("Cannot close connection.", e);
        throw new DataSourceException(CLOSE_CONNECTION_EXCEPTION_MESSAGE, e);
      }
    }

    logger.debug("Connection was closed SUCCESSFULLY.");
  }
}
