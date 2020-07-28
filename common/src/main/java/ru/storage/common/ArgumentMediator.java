package ru.storage.common;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** Argument mediator class contains all argument names. */
public final class ArgumentMediator {
  public final String WORKER;
  public final String WORKER_ID;
  public final String WORKER_SALARY;
  public final String WORKER_STATUS;
  public final String WORKER_START_DATE;
  public final String WORKER_END_DATE;

  public final String COORDINATES;
  public final String COORDINATES_X;
  public final String COORDINATES_Y;
  public final String COORDINATES_Z;

  public final String PERSON;
  public final String PERSON_NAME;
  public final String PERSON_PASSPORT_ID;

  public final String LOCATION;
  public final String LOCATION_ADDRESS;
  public final String LOCATION_LATITUDE;
  public final String LOCATION_LONGITUDE;

  public final String USER_NAME;
  public final String USER_LOGIN;
  public final String USER_PASSWORD;

  public final String SCRIPT_LINE;

  public final String INCLUDED;

  private final List<String> arguments;

  @Inject
  public ArgumentMediator() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ArgumentMediator");

    WORKER = resourceBundle.getString("arguments.worker");
    WORKER_ID = resourceBundle.getString("arguments.worker.id");
    WORKER_SALARY = resourceBundle.getString("arguments.worker.salary");
    WORKER_STATUS = resourceBundle.getString("arguments.worker.status");
    WORKER_START_DATE = resourceBundle.getString("arguments.worker.startDate");
    WORKER_END_DATE = resourceBundle.getString("arguments.worker.endDate");

    COORDINATES = resourceBundle.getString("arguments.coordinates");
    COORDINATES_X = resourceBundle.getString("arguments.coordinates.x");
    COORDINATES_Y = resourceBundle.getString("arguments.coordinates.y");
    COORDINATES_Z = resourceBundle.getString("arguments.coordinates.z");

    PERSON = resourceBundle.getString("arguments.person");
    PERSON_NAME = resourceBundle.getString("arguments.person.name");
    PERSON_PASSPORT_ID = resourceBundle.getString("arguments.person.passportId");

    LOCATION = resourceBundle.getString("arguments.location");
    LOCATION_ADDRESS = resourceBundle.getString("arguments.location.address");
    LOCATION_LATITUDE = resourceBundle.getString("arguments.location.latitude");
    LOCATION_LONGITUDE = resourceBundle.getString("arguments.location.longitude");

    USER_NAME = resourceBundle.getString("arguments.user.name");
    USER_LOGIN = resourceBundle.getString("arguments.user.login");
    USER_PASSWORD = resourceBundle.getString("arguments.user.password");

    SCRIPT_LINE = resourceBundle.getString("arguments.scriptLine");

    INCLUDED = resourceBundle.getString("arguments.included");

    arguments = initArgumentsList();
  }

  private List<String> initArgumentsList() {
    return new ArrayList<String>() {
      {
        if (WORKER != null) {
          add(WORKER);
        }
        if (WORKER_ID != null) {
          add(WORKER_ID);
        }
        if (WORKER_SALARY != null) {
          add(WORKER_SALARY);
        }
        if (WORKER_STATUS != null) {
          add(WORKER_STATUS);
        }
        if (WORKER_START_DATE != null) {
          add(WORKER_START_DATE);
        }
        if (WORKER_END_DATE != null) {
          add(WORKER_END_DATE);
        }

        if (COORDINATES != null) {
          add(COORDINATES);
        }
        if (COORDINATES_X != null) {
          add(COORDINATES_X);
        }
        if (COORDINATES_Y != null) {
          add(COORDINATES_Y);
        }
        if (COORDINATES_Z != null) {
          add(COORDINATES_Z);
        }

        if (PERSON != null) {
          add(PERSON);
        }
        if (PERSON_NAME != null) {
          add(PERSON_NAME);
        }
        if (PERSON_PASSPORT_ID != null) {
          add(PERSON_PASSPORT_ID);
        }

        if (LOCATION != null) {
          add(LOCATION);
        }
        if (LOCATION_ADDRESS != null) {
          add(LOCATION_ADDRESS);
        }
        if (LOCATION_LATITUDE != null) {
          add(LOCATION_LATITUDE);
        }
        if (LOCATION_LONGITUDE != null) {
          add(LOCATION_LONGITUDE);
        }

        if (USER_NAME != null) {
          add(USER_NAME);
        }
        if (USER_LOGIN != null) {
          add(USER_LOGIN);
        }
        if (USER_PASSWORD != null) {
          add(USER_PASSWORD);
        }

        if (SCRIPT_LINE != null) {
          add(SCRIPT_LINE);
        }

        if (INCLUDED != null) {
          add(INCLUDED);
        }
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
