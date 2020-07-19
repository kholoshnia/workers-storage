package ru.storage.server.app.guice;

import com.google.inject.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.guice.CommonModule;
import ru.storage.common.serizliser.Serializer;
import ru.storage.server.app.Server;
import ru.storage.server.app.concurrent.ExecutorService;
import ru.storage.server.app.connection.ServerConnection;
import ru.storage.server.app.connection.ServerProcessor;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.SelectorException;
import ru.storage.server.app.guice.exceptions.ProvidingException;
import ru.storage.server.controller.Controller;
import ru.storage.server.controller.auth.AuthController;
import ru.storage.server.controller.check.CheckController;
import ru.storage.server.controller.command.CommandController;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.CommandFactoryMediator;
import ru.storage.server.controller.command.factory.factories.*;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.controller.services.hash.SHA256Generator;
import ru.storage.server.controller.services.script.ScriptExecutor;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.daos.*;
import ru.storage.server.model.domain.dto.dtos.*;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.history.History;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.repositories.userRepository.UserRepository;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.database.Database;
import ru.storage.server.model.source.exceptions.DataSourceException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public final class ServerModule extends AbstractModule {
  private static final String SERVER_CONFIG_PATH = "server.properties";

  private final String URL;
  private final String USER;
  private final String PASSWORD;

  private final Logger logger;

  public ServerModule(String[] args) {
    logger = LogManager.getLogger(ServerModule.class);

    URL = args[0];
    USER = args[1];
    PASSWORD = args[2];
  }

  @Override
  public void configure() {
    install(new CommonModule());
    logger.debug("Common module was installed.");

    bind(SHA256Generator.class).in(Scopes.SINGLETON);
    bind(HashGenerator.class).to(SHA256Generator.class);
    bind(History.class).in(Scopes.SINGLETON);
    bind(ScriptExecutor.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Services were configured.");

    bind(CheckController.class).in(Scopes.SINGLETON);
    bind(AuthController.class).in(Scopes.SINGLETON);
    bind(CommandController.class).in(Scopes.SINGLETON);
    bind(CommandFactoryMediator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Controllers were configured.");

    bind(EntryCommandFactory.class).in(Scopes.SINGLETON);
    bind(HistoryCommandFactory.class).in(Scopes.SINGLETON);
    bind(ModificationCommandFactory.class).in(Scopes.SINGLETON);
    bind(ViewCommandFactory.class).in(Scopes.SINGLETON);
    bind(SpecialCommandFactory.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Command factories were configured.");

    bind(UserDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<String, UserDTO>>() {}).to(UserDAO.class);
    bind(WorkerDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, WorkerDTO>>() {}).to(WorkerDAO.class);
    bind(CoordinatesDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, CoordinatesDTO>>() {}).to(CoordinatesDAO.class);
    bind(PersonDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, PersonDTO>>() {}).to(PersonDAO.class);
    bind(LocationDAO.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<DAO<Long, LocationDTO>>() {}).to(LocationDAO.class);
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
      ExecutorService executorService,
      List<Controller> controllers,
      ServerConnection serverConnection) {
    Server server = new Server(executorService, controllers, serverConnection);

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
      int bufferSize = configuration.getInt("server.bufferSize");
      InetAddress address = InetAddress.getByName(configuration.getString("server.localhost"));
      int port = configuration.getInt("server.port");
      serverConnection =
          new ServerConnection(bufferSize, address, port, serverProcessor, serializer);
    } catch (SelectorException | ServerException | UnknownHostException e) {
      logger.fatal(() -> "Cannot provide Server.", e);
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided ServerConnection.");
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
  ExecutorService provideExecutorService() {
    Executor readExecutor =
        Executors.newCachedThreadPool(
            new ThreadFactory() {
              private final AtomicLong index = new AtomicLong(0);

              @Override
              public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "read-" + index.getAndIncrement());
              }
            });

    Executor handleExecutor =
        new ForkJoinPool(
            Runtime.getRuntime().availableProcessors(),
            pool -> {
              ForkJoinWorkerThread worker =
                  ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
              worker.setName("handle-" + worker.getPoolIndex());
              return worker;
            },
            null,
            false);

    Executor sendExecutor =
        Executors.newCachedThreadPool(
            new ThreadFactory() {
              private final AtomicLong index = new AtomicLong(0);

              @Override
              public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "send-" + index.getAndIncrement());
              }
            });

    ExecutorService executorService =
        new ExecutorService(readExecutor, handleExecutor, sendExecutor);

    logger.debug(() -> "Provided ExecutorService.");
    return executorService;
  }

  @Provides
  @Singleton
  java.security.Key provideKey() {
    java.security.Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    logger.debug(() -> "Provided Key.");
    return key;
  }

  @Provides
  @Singleton
  List<Controller> provideControllers(
      CheckController checkController,
      AuthController authController,
      CommandController commandController) {
    List<Controller> controllers =
        new ArrayList<Controller>() {
          {
            add(checkController);
            add(authController);
            add(commandController);
          }
        };

    logger.debug(() -> "Provided Controllers list.");
    return controllers;
  }

  @Provides
  @Singleton
  Map<String, CommandFactory> provideCommandFactoryMap(
      CommandMediator commandMediator,
      EntryCommandFactory entryCommandFactory,
      HistoryCommandFactory historyCommandFactory,
      ModificationCommandFactory modificationCommandFactory,
      SpecialCommandFactory specialCommandFactory,
      ViewCommandFactory viewCommandFactory) {
    Map<String, CommandFactory> commandFactoryMap =
        new HashMap<String, CommandFactory>() {
          {
            put(commandMediator.LOGIN, entryCommandFactory);
            put(commandMediator.LOGOUT, entryCommandFactory);
            put(commandMediator.REGISTER, entryCommandFactory);
            put(commandMediator.SHOW_HISTORY, historyCommandFactory);
            put(commandMediator.CLEAR_HISTORY, historyCommandFactory);
            put(commandMediator.ADD, modificationCommandFactory);
            put(commandMediator.REMOVE, modificationCommandFactory);
            put(commandMediator.UPDATE, modificationCommandFactory);
            put(commandMediator.EXIT, specialCommandFactory);
            put(commandMediator.HELP, specialCommandFactory);
            put(commandMediator.INFO, viewCommandFactory);
            put(commandMediator.SHOW, viewCommandFactory);
          }
        };

    logger.debug(() -> "Provided command factory map.");
    return commandFactoryMap;
  }

  @Provides
  @Singleton
  ExitManager provideExitManager(DataSource dataSource) {
    List<ExitListener> entities = new ArrayList<>();
    entities.add(dataSource);

    ExitManager exitManager = new ExitManager(entities);
    logger.debug(() -> "Provided ExitManager.");
    return exitManager;
  }

  @Provides
  @Singleton
  DataSource provideDataSource() throws ProvidingException {
    DataSource dataSource;

    try {
      dataSource = new Database(URL, USER, PASSWORD);
    } catch (DataSourceException e) {
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided DataSource: Database.");
    return dataSource;
  }
}
