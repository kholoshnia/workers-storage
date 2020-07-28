package ru.storage.server.model.domain.repository.repositories.workerRepository.queries;

import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;

import java.util.ArrayList;
import java.util.List;

/** Returns workers collection copy. */
public final class GetAllWorkers implements Query<Worker> {
  @Override
  public List<Worker> execute(List<Worker> workers) {
    return new ArrayList<>(workers);
  }
}
