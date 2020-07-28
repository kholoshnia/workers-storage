package ru.storage.common;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** Argument mediator class contains all argument names. */
public final class ArgumentMediator {
  public final String worker;
  public final String workerId;
  public final String workerSalary;
  public final String workerStatus;
  public final String workerStartDate;
  public final String workerEndDate;

  public final String coordinates;
  public final String coordinatesX;
  public final String coordinatesY;
  public final String coordinatesZ;

  public final String person;
  public final String personName;
  public final String personPassportId;

  public final String location;
  public final String locationAddress;
  public final String locationLatitude;
  public final String locationLongitude;

  public final String userName;
  public final String userLogin;
  public final String userPassword;

  public final String scriptLine;

  public final String included;

  private final List<String> arguments;

  @Inject
  public ArgumentMediator() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ArgumentMediator");

    worker = resourceBundle.getString("arguments.worker");
    workerId = resourceBundle.getString("arguments.worker.id");
    workerSalary = resourceBundle.getString("arguments.worker.salary");
    workerStatus = resourceBundle.getString("arguments.worker.status");
    workerStartDate = resourceBundle.getString("arguments.worker.startDate");
    workerEndDate = resourceBundle.getString("arguments.worker.endDate");

    coordinates = resourceBundle.getString("arguments.coordinates");
    coordinatesX = resourceBundle.getString("arguments.coordinates.x");
    coordinatesY = resourceBundle.getString("arguments.coordinates.y");
    coordinatesZ = resourceBundle.getString("arguments.coordinates.z");

    person = resourceBundle.getString("arguments.person");
    personName = resourceBundle.getString("arguments.person.name");
    personPassportId = resourceBundle.getString("arguments.person.passportId");

    location = resourceBundle.getString("arguments.location");
    locationAddress = resourceBundle.getString("arguments.location.address");
    locationLatitude = resourceBundle.getString("arguments.location.latitude");
    locationLongitude = resourceBundle.getString("arguments.location.longitude");

    userName = resourceBundle.getString("arguments.user.name");
    userLogin = resourceBundle.getString("arguments.user.login");
    userPassword = resourceBundle.getString("arguments.user.password");

    scriptLine = resourceBundle.getString("arguments.scriptLine");

    included = resourceBundle.getString("arguments.included");

    arguments = initArgumentsList();
  }

  private List<String> initArgumentsList() {
    return new ArrayList<String>() {
      {
        if (worker != null) {
          add(worker);
        }
        if (workerId != null) {
          add(workerId);
        }
        if (workerSalary != null) {
          add(workerSalary);
        }
        if (workerStatus != null) {
          add(workerStatus);
        }
        if (workerStartDate != null) {
          add(workerStartDate);
        }
        if (workerEndDate != null) {
          add(workerEndDate);
        }

        if (coordinates != null) {
          add(coordinates);
        }
        if (coordinatesX != null) {
          add(coordinatesX);
        }
        if (coordinatesY != null) {
          add(coordinatesY);
        }
        if (coordinatesZ != null) {
          add(coordinatesZ);
        }

        if (person != null) {
          add(person);
        }
        if (personName != null) {
          add(personName);
        }
        if (personPassportId != null) {
          add(personPassportId);
        }

        if (location != null) {
          add(location);
        }
        if (locationAddress != null) {
          add(locationAddress);
        }
        if (locationLatitude != null) {
          add(locationLatitude);
        }
        if (locationLongitude != null) {
          add(locationLongitude);
        }

        if (userName != null) {
          add(userName);
        }
        if (userLogin != null) {
          add(userLogin);
        }
        if (userPassword != null) {
          add(userPassword);
        }

        if (scriptLine != null) {
          add(scriptLine);
        }

        if (included != null) {
          add(included);
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
