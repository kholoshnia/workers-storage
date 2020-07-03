package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class WorkerValidator implements LocaleListener {
  private String wrongSalaryException;
  private String wrongStatusException;
  private String wrongStartDateException;
  private String wrongEndDateException;
  private List<String> statuses;

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.WorkerValidator", Locale.getDefault());

    wrongSalaryException = resourceBundle.getString("exceptions.wrongSalary");
    wrongStatusException = resourceBundle.getString("exceptions.wrongStatus");
    wrongStartDateException = resourceBundle.getString("exceptions.wrongStartDate");
    wrongEndDateException = resourceBundle.getString("exceptions.wrongEndDate");

    statuses =
        new ArrayList<String>() {
          {
            add(resourceBundle.getString("constants.fired"));
            add(resourceBundle.getString("constants.hired"));
            add(resourceBundle.getString("constants.promotion"));
          }
        };
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
    if (!statuses.contains(statusString)) {
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
