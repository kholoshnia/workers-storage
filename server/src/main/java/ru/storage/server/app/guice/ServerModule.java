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
import ru.storage.common.managers.exit.ExitListener;
import ru.storage.common.managers.exit.ExitManager;
import ru.storage.common.guice.CommonModule;
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

import java.util.ArrayList;
import java.util.List;

public final class ServerModule extends AbstractModule {
  private static final String SERVER_CONFIG_PATH = "server.properties";

  private final String URL;
  private final String USER;
  private final String PASSWORD;

  private final Logger logger;

  public ServerModule(String[] args) {
    this.logger = LogManager.getLogger(ServerModule.class);

    URL = args[0];
    USER = args[1];
    PASSWORD = args[2];
  }

  @Override
  public void configure() {
    install(new CommonModule());
    logger.debug("Common module has been installed.");

    bind(History.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Services have been configured.");

    bind(CommandController.class).in(Scopes.SINGLETON);
    bind(CommandFactoryMediator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Controllers have been configured.");

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
    logger.debug(() -> "DAOs have been configured.");

    bind(UserRepository.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Repository<User>>() {}).to(UserRepository.class);
    bind(WorkerRepository.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<Repository<Worker>>() {}).to(WorkerRepository.class);
    logger.debug(() -> "Repositories have been configured.");

    logger.debug(() -> "Server module has been configured.");
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
