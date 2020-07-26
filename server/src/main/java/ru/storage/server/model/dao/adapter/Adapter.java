package ru.storage.server.model.dao.adapter;

import ru.storage.server.model.dao.adapter.exceptions.AdapterException;

public interface Adapter<From, To> {
  To to(From from) throws AdapterException;

  From from(To to) throws AdapterException;
}
