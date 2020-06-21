package ru.storage.server.app.guice;

import com.google.inject.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.guice.CommonModule;
import ru.storage.common.serizliser.Serializer;
import ru.storage.server.app.Server;
import ru.storage.server.app.concurrent.Executor;
import ru.storage.server.app.connection.ServerConnection;
import ru.storage.server.app.connection.ServerProcessor;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.ServerConnectionException;
import ru.storage.server.app.guice.exceptions.ProvidingException;
import ru.storage.server.controller.command.CommandController;
import ru.storage.server.controller.command.factory.CommandFactoryMediator;
import ru.storage.server.controller.services.history.History;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.daos.*;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.repositories.userRepository.UserRepository;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.database.Database;
import ru.storage.server.model.source.exceptions.DataSourceException;
import ru.storage.server.view.ServerConsole;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public final class ServerModule extends AbstractModule {
  private static final String SERVER_CONFIG_PATH = "server.properties";

  private final String USER;
  private final String PASSWORD;

  private final Logger logger;

  public ServerModule(String[] args) {
    this.logger = LogManager.getLogger(ServerModule.class);

    USER = args[0];
    PASSWORD = args[1];
  }

  @Override
  public void configure() {
    install(new CommonModule());
    logger.debug("Common module was installed.");

    bind(History.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Services were configured.");

    bind(CommandController.class).in(Scopes.SINGLETON);
    bind(CommandFactoryMediator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Controller was configured.");

    bind(UserDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<String, User>>() {}).to(UserDAO.class);
    bind(WorkerDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, Worker>>() {}).to(WorkerDAO.class);
    bind(CoordinatesDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, Coordinates>>() {}).to(CoordinatesDAO.class);
    bind(PersonDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, Person>>() {}).to(PersonDAO.class);
    bind(LocationDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, Location>>() {}).to(LocationDAO.class);
    logger.debug(() -> "DAOs were configured.");

    bind(UserRepository.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Repository<User>>() {}).to(UserRepository.class);
    bind(WorkerRepository.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Repository<Worker>>() {}).to(WorkerRepository.class);
    logger.debug(() -> "Repositories were configured.");

    bind(ServerProcessor.class).to(Server.class);
    logger.debug(() -> "Server module was configured.");
  }

  @Provides
  @Singleton
  Server provideServer(
      ServerConsole console,
      Executor executor,
      CommandController commandController,
      ServerConnection serverConnection) {
    Server server = new Server(console, executor, commandController, serverConnection);

    logger.debug(() -> "Provided Server.");
    return server;
  }

  @Provides
  @Singleton
  ServerConnection provideServerConnection(
      Configuration configuration, ServerProcessor serverProcessor, Serializer serializer)
      throws ProvidingException {
    ServerConnection serverConnection;

    try {
      InetAddress address = InetAddress.getByName(configuration.getString("server.localhost"));
      int port = configuration.getInt("server.port");
      serverConnection = new ServerConnection(address, port, serverProcessor, serializer);
    } catch (ServerConnectionException | ServerException | UnknownHostException e) {
      logger.fatal(() -> "Cannot provide Server.", e);
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided Server.");
    return serverConnection;
  }

  @Provides
  @Singleton
  Configuration provideConfiguration() throws ProvidingException {
    logger.debug("Providing configuration for file: {}.", () -> SERVER_CONFIG_PATH);

    Parameters parameters = new Parameters();

    FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure(parameters.properties().setFileName(SERVER_CONFIG_PATH));

    Configuration configuration;

    try {
      configuration = builder.getConfiguration();
    } catch (ConfigurationException e) {
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided Configuration: FileBasedConfiguration.");
    return configuration;
  }

  @Provides
  @Singleton
  Executor provideExecutors() {
    Executor executor =
        new Executor(
            ForkJoinPool.commonPool(),
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());

    logger.debug(() -> "Provided Executor.");
    return executor;
  }

  @Provides
  @Singleton
  ExitManager provideExitManager(DataSource dataSource) {
    List<ExitListener> entities = new ArrayList<>();
    entities.add(dataSource);

    ExitManager exitManager = new ExitManager(entities);
    logger.debug(() -> "Provided ExitingDirector.");
    return exitManager;
  }

  @Provides
  @Singleton
  DataSource provideDataSource(Configuration configuration) throws ProvidingException {
    String url = configuration.getString("database.url");

    DataSource dataSource;

    try {
      dataSource = new Database(url, USER, PASSWORD);
    } catch (DataSourceException e) {
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided DataSource: DataBase.");
    return dataSource;
  }
}
