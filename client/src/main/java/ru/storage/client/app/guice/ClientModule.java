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
import ru.storage.client.app.guice.exceptions.ProvidingException;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.argumentFormer.argumentFormers.*;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.localeManager.LocaleManager;
import ru.storage.client.controller.responseHandler.MessageMediator;
import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.Formatter;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.client.controller.responseHandler.responseHandlers.*;
import ru.storage.client.controller.validator.validators.*;
import ru.storage.client.view.console.Console;
import ru.storage.client.view.console.Terminal;
import ru.storage.client.view.console.exceptions.ConsoleException;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.chunker.ByteChunker;
import ru.storage.common.chunker.Chunker;
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
  private static final Logger logger = LogManager.getLogger(ClientModule.class);

  private static final String CLIENT_CONFIG_PATH = "client.properties";

  @Override
  protected void configure() {
    install(new CommonModule());
    logger.debug(() -> "Common module was installed.");

    bind(IdFormer.class).in(Scopes.SINGLETON);
    bind(LoginFormer.class).in(Scopes.SINGLETON);
    bind(NewWorkerFormer.class).in(Scopes.SINGLETON);
    bind(NewWorkerIdFormer.class).in(Scopes.SINGLETON);
    bind(NoArgumentsFormer.class).in(Scopes.SINGLETON);
    bind(RegisterFormer.class).in(Scopes.SINGLETON);
    bind(ScriptFormer.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Formers were configured.");

    bind(WorkerValidator.class).in(Scopes.SINGLETON);
    bind(CoordinatesValidator.class).in(Scopes.SINGLETON);
    bind(PersonValidator.class).in(Scopes.SINGLETON);
    bind(LocationValidator.class).in(Scopes.SINGLETON);
    bind(RegisterValidator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Validators were configured.");

    bind(StringFormatter.class).in(Scopes.SINGLETON);
    bind(Formatter.class).to(StringFormatter.class);
    bind(MessageMediator.class).in(Scopes.SINGLETON);
    bind(FormerMediator.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Controller was configured.");

    bind(Console.class).to(Terminal.class);
    bind(Client.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Client was configured.");
  }

  @Provides
  @Singleton
  Terminal provideConsole(
      ExitManager exitManager,
      ServerWorker serverWorker,
      CommandMediator commandMediator,
      LocaleManager localeManager,
      FormerMediator formerMediator,
      List<ResponseHandler> responseHandlers)
      throws ProvidingException {
    Terminal console;

    try {
      console =
          new Terminal(
              exitManager,
              System.in,
              System.out,
              serverWorker,
              commandMediator,
              localeManager,
              formerMediator,
              responseHandlers);
    } catch (ConsoleException e) {
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided Console.");
    return console;
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
  ServerWorker provideServerWorker(
      Configuration configuration, ByteChunker chunker, Serializer serializer)
      throws ProvidingException {
    ServerWorker serverWorker;

    try {
      InetAddress address = InetAddress.getByName(configuration.getString("server.address"));
      int bufferSize = configuration.getInt("server.bufferSize");
      int port = configuration.getInt("server.port");
      serverWorker = new ServerWorker(address, port, bufferSize, chunker, serializer);
    } catch (UnknownHostException e) {
      logger.fatal(() -> "Cannot provide Server.", e);
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided ServerWorker.");
    return serverWorker;
  }

  @Provides
  @Singleton
  LocaleManager provideLocaleManager(
      MessageMediator messageMediator,
      WorkerValidator workerValidator,
      CoordinatesValidator coordinatesValidator,
      PersonValidator personValidator,
      LocationValidator locationValidator,
      RegisterValidator registerValidator,
      IdFormer idFormer,
      LoginFormer loginFormer,
      NewWorkerFormer newWorkerFormer,
      NewWorkerIdFormer newWorkerIdFormer,
      NoArgumentsFormer noArgumentsFormer,
      RegisterFormer registerFormer,
      ScriptFormer scriptFormer) {
    List<LocaleListener> entities =
        new ArrayList<LocaleListener>() {
          {
            add(messageMediator);
            add(workerValidator);
            add(coordinatesValidator);
            add(personValidator);
            add(locationValidator);
            add(registerValidator);
            add(idFormer);
            add(loginFormer);
            add(newWorkerFormer);
            add(newWorkerIdFormer);
            add(noArgumentsFormer);
            add(registerFormer);
            add(scriptFormer);
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
      IdFormer idFormer,
      LoginFormer loginFormer,
      NewWorkerFormer newWorkerFormer,
      NewWorkerIdFormer newWorkerIdFormer,
      NoArgumentsFormer noArgumentsFormer,
      RegisterFormer registerFormer,
      ScriptFormer scriptFormer) {
    Map<String, ArgumentFormer> argumentFormerMap =
        new HashMap<String, ArgumentFormer>() {
          {
            put(commandMediator.login, loginFormer);
            put(commandMediator.logout, noArgumentsFormer);
            put(commandMediator.register, registerFormer);
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
      ArgumentMediator argumentMediator,
      WorkerValidator workerValidator,
      CoordinatesValidator coordinatesValidator,
      PersonValidator personValidator,
      LocationValidator locationValidator,
      RegisterValidator registerValidator) {
    Map<String, ArgumentValidator> argumentValidatorMap =
        new HashMap<String, ArgumentValidator>() {
          {
            put(argumentMediator.workerId, workerValidator::checkId);
            put(argumentMediator.workerSalary, workerValidator::checkSalary);
            put(argumentMediator.workerStatus, workerValidator::checkStatus);
            put(argumentMediator.workerStartDate, workerValidator::checkStartDate);
            put(argumentMediator.workerEndDate, workerValidator::checkEndDate);
            put(argumentMediator.coordinatesX, coordinatesValidator::checkX);
            put(argumentMediator.coordinatesY, coordinatesValidator::checkY);
            put(argumentMediator.coordinatesZ, coordinatesValidator::checkZ);
            put(argumentMediator.personName, personValidator::checkName);
            put(argumentMediator.personPassportId, personValidator::checkPassportId);
            put(argumentMediator.locationAddress, locationValidator::checkAddress);
            put(argumentMediator.locationLatitude, locationValidator::checkLatitude);
            put(argumentMediator.locationLongitude, locationValidator::checkLongitude);
            put(argumentMediator.userName, registerValidator::checkName);
            put(argumentMediator.userLogin, registerValidator::checkLogin);
            put(argumentMediator.userPassword, registerValidator::checkPassword);
          }
        };

    logger.debug(() -> "Provided argument validator map.");
    return argumentValidatorMap;
  }

  @Provides
  @Singleton
  List<ResponseHandler> provideResponseHandlers(
      Formatter stringFormatter, MessageMediator messageMediator) {
    List<ResponseHandler> responseHandlers =
        new ArrayList<ResponseHandler>() {
          {
            add(new OkResponseHandler());
            add(new CreatedResponseHandler(stringFormatter));
            add(new NoContentResponseHandler(stringFormatter));
            add(new NotModifiedResponseHandler(stringFormatter));
            add(new BadRequestResponseHandler(stringFormatter, messageMediator));
            add(new UnauthorizedResponseHandler(stringFormatter, messageMediator));
            add(new NotFoundResponseHandler(stringFormatter));
            add(new ForbiddenResponseHandler(stringFormatter));
            add(new ConflictResponseHandler(stringFormatter, messageMediator));
            add(new InternalServerErrorResponseHandler(stringFormatter, messageMediator));
          }
        };

    logger.debug("Provided response handlers list.");
    return responseHandlers;
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
  ExitManager provideExitManager() {
    List<ExitListener> entities = new ArrayList<>();

    ExitManager exitManager = new ExitManager(entities);
    logger.debug(() -> "Provided ExitManager.");
    return exitManager;
  }
}
