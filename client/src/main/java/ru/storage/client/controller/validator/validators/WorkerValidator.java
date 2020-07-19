package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class WorkerValidator implements LocaleListener {
  private String wrongIdException;
  private String wrongSalaryException;
  private String wrongStatusException;
  private String wrongStartDateException;
  private String wrongEndDateException;

  private List<String> statusMap;

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.WorkerValidator");

    statusMap =
        new ArrayList<String>() {
          {
            add(resourceBundle.getString("constants.fired"));
            add(resourceBundle.getString("constants.hired"));
            add(resourceBundle.getString("constants.promotion"));
          }
        };

    wrongIdException = resourceBundle.getString("exceptions.wrongId");
    wrongSalaryException = resourceBundle.getString("exceptions.wrongSalary");
    wrongStatusException =
        String.format("%s %s", resourceBundle.getString("exceptions.wrongStatus"), statusMap);
    wrongStartDateException = resourceBundle.getString("exceptions.wrongStartDate");
    wrongEndDateException = resourceBundle.getString("exceptions.wrongEndDate");
  }

  public void checkId(String idString) throws ValidationException {
    long id;

    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException | NullPointerException exception) {
      throw new ValidationException(wrongIdException);
    }

    if (id < 0) {
      throw new ValidationException(wrongIdException);
    }
  }

  public void checkSalary(String salaryString) throws ValidationException {
    double salary;

    try {
      salary = Double.parseDouble(salaryString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongSalaryException);
    }

    if (salary <= 0) {
      throw new ValidationException(wrongSalaryException);
    }
  }

  public void checkStatus(String statusString) throws ValidationException {
    if (!statusMap.contains(statusString)) {
      throw new ValidationException(wrongStatusException);
    }
  }

  public void checkStartDate(String startDateString) throws ValidationException {
    try {
      LocalDateTime.parse(startDateString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException e) {
      throw new ValidationException(wrongStartDateException);
    }
  }

  public void checkEndDate(String endDateString) throws ValidationException {
    try {
      LocalDateTime.parse(endDateString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException e) {
      throw new ValidationException(wrongEndDateException);
    }
  }
}
