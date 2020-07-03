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
import ru.storage.client.app.connection.Connection;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.client.app.guice.exceptions.ProvidingException;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.localeManager.LocaleManager;
import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.ServerResponseHandler;
import ru.storage.client.view.View;
import ru.storage.client.view.console.Console;
import ru.storage.client.view.console.exceptions.ConsoleException;
import ru.storage.common.CommandMediator;
import ru.storage.common.chunker.Chunker;
import ru.storage.common.guice.CommonModule;
import ru.storage.common.serizliser.Serializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public final class ClientModule extends AbstractModule {
  private static final String CLIENT_CONFIG_PATH = "client.properties";

  private final Logger logger;

  public ClientModule(String[] args) {
    this.logger = LogManager.getLogger(ClientModule.class);
  }

  @Override
  protected void configure() {
    install(new CommonModule());
    logger.debug(() -> "Common module was installed.");

    bind(ServerResponseHandler.class).in(Scopes.SINGLETON);
    bind(ResponseHandler.class).to(ServerResponseHandler.class);

    bind(FormerMediator.class).in(Scopes.SINGLETON);
    bind(Client.class).in(Scopes.SINGLETON);
    logger.debug(() -> "Client was configured.");
  }

  @Provides
  @Singleton
  View provideView(
      Configuration configuration,
      Connection connection,
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
              System.in,
              System.out,
              connection,
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
  Connection provideConnection(Configuration configuration, Chunker chunker, Serializer serializer)
      throws ProvidingException {
    Connection connection;

    try {
      InetAddress address = InetAddress.getByName(configuration.getString("server.address"));
      int port = configuration.getInt("server.port");
      connection = new Connection(chunker, serializer, address, port);
    } catch (UnknownHostException | ClientConnectionException e) {
      logger.fatal(() -> "Cannot provide Server.", e);
      throw new ProvidingException(e);
    }

    logger.debug(() -> "Provided Connection.");
    return connection;
  }
}
