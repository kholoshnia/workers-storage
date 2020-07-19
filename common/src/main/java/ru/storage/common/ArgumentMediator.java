package ru.storage.common;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;

import java.util.ArrayList;
import java.util.List;

public final class ArgumentMediator {
  public final String WORKER_ID;
  public final String WORKER_SALARY;
  public final String WORKER_STATUS;
  public final String WORKER_START_DATE;
  public final String WORKER_END_DATE;
  public final String COORDINATES_X;
  public final String COORDINATES_Y;
  public final String COORDINATES_Z;
  public final String PERSON_NAME;
  public final String PERSON_PASSPORT_ID;
  public final String LOCATION_ADDRESS;
  public final String LOCATION_LATITUDE;
  public final String LOCATION_LONGITUDE;
  public final String USER_NAME;
  public final String USER_LOGIN;
  public final String USER_PASSWORD;

  private final List<String> arguments;

  @Inject
  public ArgumentMediator(Configuration configuration) {
    WORKER_ID = configuration.getString("arguments.worker.id");
    WORKER_SALARY = configuration.getString("arguments.worker.salary");
    WORKER_STATUS = configuration.getString("arguments.worker.status");
    WORKER_START_DATE = configuration.getString("arguments.worker.startDate");
    WORKER_END_DATE = configuration.getString("arguments.worker.endDate");
    COORDINATES_X = configuration.getString("arguments.coordinates.x");
    COORDINATES_Y = configuration.getString("arguments.coordinates.y");
    COORDINATES_Z = configuration.getString("arguments.coordinates.z");
    PERSON_NAME = configuration.getString("arguments.person.name");
    PERSON_PASSPORT_ID = configuration.getString("arguments.person.passportId");
    LOCATION_ADDRESS = configuration.getString("arguments.person.address");
    LOCATION_LATITUDE = configuration.getString("arguments.person.latitude");
    LOCATION_LONGITUDE = configuration.getString("arguments.person.longitude");
    USER_NAME = configuration.getString("arguments.user.name");
    USER_LOGIN = configuration.getString("arguments.user.login");
    USER_PASSWORD = configuration.getString("arguments.user.password");

    arguments = initArgumentsList();
  }

  private List<String> initArgumentsList() {
    return new ArrayList<String>() {
      {
        add(WORKER_ID);
        add(WORKER_SALARY);
        add(WORKER_STATUS);
        add(WORKER_START_DATE);
        add(WORKER_END_DATE);
        add(COORDINATES_X);
        add(COORDINATES_Y);
        add(COORDINATES_Z);
        add(PERSON_NAME);
        add(PERSON_PASSPORT_ID);
        add(LOCATION_ADDRESS);
        add(LOCATION_LATITUDE);
        add(LOCATION_LONGITUDE);
        add(USER_NAME);
        add(USER_LOGIN);
        add(USER_PASSWORD);
      }
    };
  }

  public List<String> getArguments() {
    return new ArrayList<>(arguments);
  }

  public boolean contains(String argument) {
    return arguments.contains(argument);
  }
}
