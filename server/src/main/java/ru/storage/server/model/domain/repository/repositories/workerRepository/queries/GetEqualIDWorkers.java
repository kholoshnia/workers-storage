package ru.storage.server.model.domain.repository.repositories.workerRepository.queries;

import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/** Returns workers collection clone. */
public final class GetEqualIDWorkers implements Query<Worker> {
  private final long id;

  /**
   * Creates query to get workers with specified id.
   *
   * @param id concrete id
   */
  public GetEqualIDWorkers(long id) {
    this.id = id;
  }

  @Override
  public List<Worker> execute(List<Worker> workers) {
    return workers.stream()
        .filter(worker -> worker.getID() == id)
        .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
  }
}
