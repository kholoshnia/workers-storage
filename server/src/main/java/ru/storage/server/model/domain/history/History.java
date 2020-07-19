package ru.storage.server.model.domain.history;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class History {
  private final Logger logger;
  private final List<Record> records;

  public History() {
    logger = LogManager.getLogger(History.class);
    records = new ArrayList<>();
  }

  public List<Record> getRecords(int number) {
    return records.stream()
        .skip(Math.max(0, records.size() - number))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public List<Record> getRecords() {
    return new ArrayList<>(records);
  }

  public void addRecord(@Nonnull Record record) {
    records.add(record);
    logger.debug(() -> "New record was added to the history.");
  }

  public void clear() {
    records.clear();
    logger.debug(() -> "History was cleared.");
  }

  public long getSize() {
    return records.size();
  }
}
