package ru.storage.server.controller.services.format.status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class StatusFormatter extends StatusFormat {
  private final Logger logger;
  private final Map<Status, String> statusMap;

  public StatusFormatter(Locale locale) {
    logger = LogManager.getLogger(StatusFormatter.class);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.StatusFormat", locale);
    statusMap = initStatusMap(resourceBundle);
    logger.debug(() -> "Status localized map was created.");
  }

  private Map<Status, String> initStatusMap(ResourceBundle resourceBundle) {
    return new HashMap<Status, String>() {
      {
        put(Status.FIRED, resourceBundle.getString("values.fired"));
        put(Status.HIRED, resourceBundle.getString("values.hired"));
        put(Status.PROMOTION, resourceBundle.getString("values.promotion"));
      }
    };
  }

  @Override
  public String format(Status status) {
    if (status == null) {
      return null;
    }

    String result = statusMap.get(status);

    logger.info("Got localized status: {}.", () -> result);
    return result;
  }
}
