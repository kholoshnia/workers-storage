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
import ru.storage.common.chunker.ByteChunker;
import ru.storage.common.chunker.Chunker;
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
import ru.storage.server.controller.controllers.AuthController;
import ru.storage.server.controller.controllers.CheckController;
import ru.storage.server.controller.controllers.argument.ArgumentController;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;
import ru.storage.server.controller.controllers.argument.validator.validators.*;
import ru.storage.server.controller.controllers.command.CommandController;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.CommandFactoryMediator;
import ru.storage.server.controller.controllers.command.factory.factories.*;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.controller.services.hash.SHA256Generator;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.script.scriptExecutor.ScriptExecutor;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.FormerMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers.*;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.adapter.Adapter;
import ru.storage.server.model.dao.adapter.adapters.RoleAdapter;
import ru.storage.server.model.dao.adapter.adapters.StatusAdapter;
import ru.storage.server.model.dao.adapter.adapters.ZonedDateTimeAdapter;
import ru.storage.server.model.dao.daos.*;
import ru.storage.server.model.domain.dto.dtos.*;
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Status;
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
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public final class ServerModule extends AbstractModule {
  private static final Logger logger = LogManager.getLogger(ServerModule.class);

  private static final String SERVER_CONFIG_PATH = "server.properties";

  private final String url;
  private final String user;
  private final String password;

  public ServerModule(String[] args) {
    url = args[0];
    user = args[1];
    password = args[2];
  }

  @Override
  public void configure() {
    install(new CommonModule());
    logger.debug("Common module was installed.");

    bind(Parser.class).in(Scopes.SINGLETON);
    bind(SHA256Generator.class).in(Scopes.SINGLETON);
    bind(HashGenerator.class).to(SHA256Generator.class);
    bind(History.class).in(Scopes.SINGLETON);
    bind(ScriptExecutor.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Services were configured.");

    bind(IdFormer.class).in(Scopes.SINGLETON);
    bind(NewWorkerFormer.class).in(Scopes.SINGLETON);
    bind(NewWorkerIdFormer.class).in(Scopes.SINGLETON);
    bind(NoArgumentsFormer.class).in(Scopes.SINGLETON);
    bind(ScriptFormer.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Formers were configured.");

    bind(CheckController.class).in(Scopes.SINGLETON);
    bind(ArgumentController.class).in(Scopes.SINGLETON);
    bind(AuthController.class).in(Scopes.SINGLETON);
    bind(CommandController.class).in(Scopes.SINGLETON);
    bind(CommandFactoryMediator.class).in(Scopes.SINGLETON);
    bind(FormerMediator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Controllers were configured.");

    bind(IdValidator.class).in(Scopes.SINGLETON);
    bind(LoginValidator.class).in(Scopes.SINGLETON);
    bind(NewWorkerIdValidator.class).in(Scopes.SINGLETON);
    bind(NewWorkerValidator.class).in(Scopes.SINGLETON);
    bind(NoArgumentsValidator.class).in(Scopes.SINGLETON);
    bind(RegisterValidator.class).in(Scopes.SINGLETON);
    bind(ScriptValidator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Argument validators were configured.");

    bind(EntryCommandFactory.class).in(Scopes.SINGLETON);
    bind(HistoryCommandFactory.class).in(Scopes.SINGLETON);
    bind(ModificationCommandFactory.class).in(Scopes.SINGLETON);
    bind(ViewCommandFactory.class).in(Scopes.SINGLETON);
    bind(SpecialCommandFactory.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Command factories were configured.");

    bind(RoleAdapter.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Adapter<Role, String>>() {}).to(RoleAdapter.class);
    bind(StatusAdapter.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Adapter<Status, String>>() {}).to(StatusAdapter.class);
    bind(ZonedDateTimeAdapter.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Adapter<ZonedDateTime, Timestamp>>() {}).to(ZonedDateTimeAdapter.class);
    logger.debug(() -> "Adapters were configured.");

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
  ByteChunker provideChunker(Configuration configuration) {
    int bufferSize = configuration.getInt("server.bufferSize");
    String stopWord = configuration.getString("server.stopWord");

    ByteChunker chunker = new Chunker(bufferSize, stopWord);
    logger.debug(() -> "Provided ByteChunker.");
    return chunker;
  }

  @Provides
  @Singleton
  ServerConnection provideServerConnection(
      Configuration configuration,
      ServerProcessor serverProcessor,
      ByteChunker chunker,
      Serializer serializer)
      throws ProvidingException {
    ServerConnection serverConnection;

    try {
      int bufferSize = configuration.getInt("server.bufferSize");
      InetAddress address = InetAddress.getByName(configuration.getString("server.localhost"));
      int port = configuration.getInt("server.port");
      serverConnection =
          new ServerConnection(bufferSize, address, port, serverProcessor, chunker, serializer);
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
      ArgumentController argumentController,
      AuthController authController,
      CommandController commandController) {
    List<Controller> controllers =
        new ArrayList<Controller>() {
          {
            add(checkController);
            add(argumentController);
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
            put(commandMediator.login, entryCommandFactory);
            put(commandMediator.logout, entryCommandFactory);
            put(commandMediator.register, entryCommandFactory);
            put(commandMediator.showHistory, historyCommandFactory);
            put(commandMediator.clearHistory, historyCommandFactory);
            put(commandMediator.add, modificationCommandFactory);
            put(commandMediator.remove, modificationCommandFactory);
            put(commandMediator.update, modificationCommandFactory);
            put(commandMediator.exit, specialCommandFactory);
            put(commandMediator.help, specialCommandFactory);
            put(commandMediator.info, viewCommandFactory);
            put(commandMediator.show, viewCommandFactory);
            put(commandMediator.executeScript, specialCommandFactory);
          }
        };

    logger.debug(() -> "Provided command factory map.");
    return commandFactoryMap;
  }

  @Provides
  @Singleton
  Map<String, ArgumentFormer> provideArgumentFormerMap(
      CommandMediator commandMediator,
      IdFormer idFormer,
      NewWorkerFormer newWorkerFormer,
      NewWorkerIdFormer newWorkerIdFormer,
      NoArgumentsFormer noArgumentsFormer,
      ScriptFormer scriptFormer) {
    Map<String, ArgumentFormer> argumentFormerMap =
        new HashMap<String, ArgumentFormer>() {
          {
            put(commandMediator.showHistory, noArgumentsFormer);
            put(commandMediator.clearHistory, noArgumentsFormer);
            put(commandMediator.add, newWorkerFormer);
            put(commandMediator.remove, idFormer);
            put(commandMediator.update, newWorkerIdFormer);
            put(commandMediator.exit, noArgumentsFormer);
            put(commandMediator.help, noArgumentsFormer);
            put(commandMediator.info, noArgumentsFormer);
            put(commandMediator.show, noArgumentsFormer);
            put(commandMediator.executeScript, scriptFormer);
          }
        };

    logger.debug(() -> "Provided argument former map.");
    return argumentFormerMap;
  }

  @Provides
  @Singleton
  Map<String, ArgumentValidator> provideArgumentValidatorMap(
      CommandMediator commandMediator,
      IdValidator idValidator,
      LoginValidator loginValidator,
      NewWorkerIdValidator newWorkerIdValidator,
      NewWorkerValidator newWorkerValidator,
      NoArgumentsValidator noArgumentsValidator,
      RegisterValidator registerValidator,
      ScriptValidator scriptValidator) {
    Map<String, ArgumentValidator> validatorMap =
        new HashMap<String, ArgumentValidator>() {
          {
            put(commandMediator.login, loginValidator);
            put(commandMediator.logout, noArgumentsValidator);
            put(commandMediator.register, registerValidator);
            put(commandMediator.showHistory, noArgumentsValidator);
            put(commandMediator.clearHistory, noArgumentsValidator);
            put(commandMediator.add, newWorkerValidator);
            put(commandMediator.remove, idValidator);
            put(commandMediator.update, newWorkerIdValidator);
            put(commandMediator.exit, noArgumentsValidator);
            put(commandMediator.help, noArgumentsValidator);
            put(commandMediator.info, noArgumentsValidator);
            put(commandMediator.show, noArgumentsValidator);
            put(commandMediator.executeScript, scriptValidator);
          }
        };

    logger.debug(() -> "Provided argument validator map.");
    return validatorMap;
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
      dataSource = new Database(url, user, password);
    } catch (DataSourceException e) {
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided DataSource: Database.");
    return dataSource;
  }
}
