package ru.storage.common.transfer.serizliser.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.serizliser.Serializer;
import ru.storage.common.transfer.serizliser.exceptions.DeserializationException;

import java.io.Serializable;

public final class JsonSerializer implements Serializer {
  private final Logger logger;
  private final Gson gson;

  public JsonSerializer(Gson gson) {
    this.logger = LogManager.getLogger(JsonSerializer.class);
    this.gson = gson;
  }

  @Override
  public String serialize(Serializable serializable) {
    String result = gson.toJson(serializable);

    logger.info("Object was serialized.");
    return result;
  }

  @Override
  public <T extends Serializable> T deserialize(String object, Class<T> clazz)
      throws DeserializationException {
    T result;

    try {
      result = gson.fromJson(object, clazz);
    } catch (JsonSyntaxException e) {
      logger.error(() -> "Cannot deserialize object.", e);
      throw new DeserializationException(e);
    }

    logger.info(() -> "Object was deserialized.");
    return result;
  }
}
