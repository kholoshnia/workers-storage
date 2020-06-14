package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class WorkerValidator implements LocaleListener {
  private String wrongSalaryExceptionMessage;
  private String wrongStatusExceptionMessage;
  private String wrongStartDateExceptionMessage;
  private String wrongEndDateExceptionMessage;
  private List<String> statuses;

  public WorkerValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.WorkerValidator", Locale.getDefault());

    wrongSalaryExceptionMessage = resourceBundle.getString("exceptionMessages.wrongSalary");
    wrongStatusExceptionMessage = resourceBundle.getString("exceptionMessages.wrongStatus");
    wrongStartDateExceptionMessage = resourceBundle.getString("exceptionMessages.wrongStartDate");
    wrongEndDateExceptionMessage = resourceBundle.getString("exceptionMessages.wrongEndDate");

    statuses =
        new ArrayList<String>() {
          {
            add(resourceBundle.getString("constantsNames.fired"));
            add(resourceBundle.getString("constantsNames.hired"));
            add(resourceBundle.getString("constantsNames.promotion"));
          }
        };
  }

  public void checkSalary(String salaryString) throws ValidationException {
    double salary;

    try {
      salary = Double.parseDouble(salaryString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongSalaryExceptionMessage);
    }

    if (salary <= 0) {
      throw new ValidationException(wrongSalaryExceptionMessage);
    }
  }

  public void checkStatus(String statusString) throws ValidationException {
    if (!statuses.contains(statusString)) {
      throw new ValidationException(wrongStatusExceptionMessage);
    }
  }

  public void checkStartDate(String startDateString) throws ValidationException {
    try {
      LocalDateTime.parse(startDateString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException e) {
      throw new ValidationException(wrongStartDateExceptionMessage);
    }
  }

  public void checkEndDate(String endDateString) throws ValidationException {
    try {
      LocalDateTime.parse(endDateString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException e) {
      throw new ValidationException(wrongEndDateExceptionMessage);
    }
  }
}
