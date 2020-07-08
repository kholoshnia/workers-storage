package ru.storage.server.controller.services.format.status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class StatusLocaleFormat extends StatusFormat {
  private final Logger logger;
  private final Map<Status, String> statusMap;

  public StatusLocaleFormat(Locale locale) {
    this.logger = LogManager.getLogger(StatusLocaleFormat.class);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.StatusFormat", locale);

    this.statusMap =
        new HashMap<Status, String>() {
          {
            put(Status.FIRED, resourceBundle.getString("values.fired"));
            put(Status.HIRED, resourceBundle.getString("values.hired"));
            put(Status.PROMOTION, resourceBundle.getString("values.promotion"));
          }
        };

    logger.debug(() -> "Status localized map has been created.");
  }

  @Override
  public String format(Status status) {
    String result = statusMap.get(status);

    logger.info("Got localized status: {}.", () -> result);
    return statusMap.get(status);
  }
}
