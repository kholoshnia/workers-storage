package ru.storage.client.app.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.Client;
import ru.storage.client.app.connection.ServerWorker;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.client.app.guice.exceptions.ProvidingException;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.argumentFormer.argumentFormers.NewWorkerFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NewWorkerIdFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NoArgumentsFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.WorkerIdFormer;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.localeManager.LocaleManager;
import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.ServerResponseHandler;
import ru.storage.client.controller.validator.validators.*;
import ru.storage.client.view.View;
import ru.storage.client.view.console.Console;
import ru.storage.client.view.console.exceptions.ConsoleException;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.guice.CommonModule;
import ru.storage.common.serizliser.Serializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientModule extends AbstractModule {
  private static final String CLIENT_CONFIG_PATH = "client.properties";

  private final Logger logger;

  public ClientModule(String[] args) {
    logger = LogManager.getLogger(ClientModule.class);
  }

  @Override
  protected void configure() {
    install(new CommonModule());
    logger.debug(() -> "Common module has been installed.");

    bind(ServerResponseHandler.class).in(Scopes.SINGLETON);
    bind(ResponseHandler.class).to(ServerResponseHandler.class);
    bind(FormerMediator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Controller has been configured.");

    bind(Client.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Client has been configured.");
  }

  @Provides
  @Singleton
  View provideView(
      Configuration configuration,
      ExitManager exitManager,
      ServerWorker serverWorker,
      CommandMediator commandMediator,
      LocaleManager localeManager,
      FormerMediator formerMediator,
      ResponseHandler responseHandler)
      throws ProvidingException {
    Console console;

    try {
      console =
          new Console(
              configuration,
              exitManager,
              System.in,
              System.out,
              serverWorker,
              commandMediator,
              localeManager,
              formerMediator,
              responseHandler);
    } catch (ConsoleException e) {
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided Console.");
    return console;
  }

  @Provides
  @Singleton
  LocaleManager provideLocaleManager(
      FormerMediator formerMediator, ResponseHandler responseHandler) {
    List<LocaleListener> entities =
        new ArrayList<LocaleListener>() {
          {
            add(formerMediator);
            add(responseHandler);
          }
        };

    LocaleManager localeManager = new LocaleManager(entities);
    logger.debug(() -> "Provided LocaleManager.");
    return localeManager;
  }

  @Provides
  @Singleton
  Map<String, ArgumentFormer> provideArgumentFormerMap(
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      Map<String, ArgumentValidator> argumentValidatorMap,
      Console console) {
    ArgumentFormer noArgumentsFormer = new NoArgumentsFormer();
    ArgumentFormer workerIdFormer = new WorkerIdFormer(argumentMediator);
    ArgumentFormer newWorkerFormer =
        new NewWorkerFormer(argumentValidatorMap, argumentMediator, console);
    ArgumentFormer newWorkerId = new NewWorkerIdFormer(argumentMediator);

    Map<String, ArgumentFormer> argumentFormerMap =
        new HashMap<String, ArgumentFormer>() {
          {
            put(commandMediator.LOGIN, noArgumentsFormer);
            put(commandMediator.LOGOUT, noArgumentsFormer);
            put(commandMediator.REGISTER, noArgumentsFormer);
            put(commandMediator.SHOW_HISTORY, noArgumentsFormer);
            put(commandMediator.CLEAR_HISTORY, noArgumentsFormer);
            put(commandMediator.ADD, newWorkerFormer);
            put(commandMediator.REMOVE, workerIdFormer);
            put(commandMediator.UPDATE, newWorkerId);
            put(commandMediator.EXIT, workerIdFormer);
            put(commandMediator.HELP, noArgumentsFormer);
            put(commandMediator.INFO, noArgumentsFormer);
            put(commandMediator.SHOW, noArgumentsFormer);
          }
        };

    logger.debug(() -> "Provided argument former map.");
    return argumentFormerMap;
  }

  @Provides
  @Singleton
  Map<String, ArgumentValidator> provideArgumentValidatorMap(
      ArgumentMediator argumentMediator,
      WorkerValidator workerValidator,
      CoordinatesValidator coordinatesValidator,
      PersonValidator personValidator,
      LocationValidator locationValidator,
      UserValidator userValidator) {
    Map<String, ArgumentValidator> argumentValidatorMap =
        new HashMap<String, ArgumentValidator>() {
          {
            put(argumentMediator.WORKER_SALARY, workerValidator::checkSalary);
            put(argumentMediator.WORKER_STATUS, workerValidator::checkStatus);
            put(argumentMediator.WORKER_START_DATE, workerValidator::checkStartDate);
            put(argumentMediator.WORKER_END_DATE, workerValidator::checkEndDate);
            put(argumentMediator.COORDINATES_X, coordinatesValidator::checkX);
            put(argumentMediator.COORDINATES_Y, coordinatesValidator::checkY);
            put(argumentMediator.COORDINATES_Z, coordinatesValidator::checkZ);
            put(argumentMediator.PERSON_NAME, personValidator::checkName);
            put(argumentMediator.PERSON_PASSPORT_ID, personValidator::checkPassportId);
            put(argumentMediator.LOCATION_ADDRESS, locationValidator::checkAddress);
            put(argumentMediator.LOCATION_LATITUDE, locationValidator::checkLatitude);
            put(argumentMediator.LOCATION_LONGITUDE, locationValidator::checkLongitude);
            put(argumentMediator.USER_NAME, userValidator::checkName);
            put(argumentMediator.USER_LOGIN, userValidator::checkName);
            put(argumentMediator.USER_PASSWORD, userValidator::checkPassword);
          }
        };

    logger.debug(() -> "Provided argument validator map.");
    return argumentValidatorMap;
  }

  @Provides
  @Singleton
  Configuration provideConfiguration() throws ProvidingException {
    logger.debug("Providing configuration for file: {}.", () -> CLIENT_CONFIG_PATH);

    Parameters parameters = new Parameters();

    FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure(parameters.properties().setFileName(CLIENT_CONFIG_PATH));

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
  ServerWorker provideConnection(Configuration configuration, Serializer serializer)
      throws ProvidingException {
    ServerWorker serverWorker;

    try {
      InetAddress address = InetAddress.getByName(configuration.getString("server.address"));
      int bufferSize = configuration.getInt("server.bufferSize");
      int port = configuration.getInt("server.port");
      serverWorker = new ServerWorker(bufferSize, serializer, address, port);
    } catch (UnknownHostException | ClientConnectionException e) {
      logger.fatal(() -> "Cannot provide Server.", e);
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided ServerWorker.");
    return serverWorker;
  }

  @Provides
  @Singleton
  ExitManager provideExitManager() {
    List<ExitListener> entities = new ArrayList<>();

    ExitManager exitManager = new ExitManager(entities);
    logger.debug(() -> "Provided ExitManager.");
    return exitManager;
  }
}
