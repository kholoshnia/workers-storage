package ru.storage.common;

import org.apache.commons.configuration2.Configuration;

public final class ArgumentMediator {
  public final String NAME;
  public final String LOGIN;
  public final String PASSWORD;
  public final String ID;

  public ArgumentMediator(Configuration configuration) {
    NAME = configuration.getString("arguments.name");
    LOGIN = configuration.getString("arguments.login");
    PASSWORD = configuration.getString("arguments.password");
    ID = configuration.getString("arguments.id");
  }
}
