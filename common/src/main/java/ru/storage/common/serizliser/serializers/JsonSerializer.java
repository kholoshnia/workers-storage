package ru.storage.common.serizliser.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public final class JsonSerializer implements Serializer {
  private static final Logger logger = LogManager.getLogger(JsonSerializer.class);

  private final Gson gson;

  @Inject
  public JsonSerializer(Gson gson) {
    this.gson = gson;
  }

  @Override
  public byte[] serialize(Serializable serializable) {
    String json = gson.toJson(serializable);
    byte[] result = json.getBytes(StandardCharsets.UTF_8);

    logger.info("Object was serialized.");
    return result;
  }

  @Override
  public <T extends Serializable> T deserialize(byte[] bytes, Class<T> clazz)
      throws DeserializationException {
    String json = new String(bytes, StandardCharsets.UTF_8).trim();
    T result;

    try {
      result = gson.fromJson(json, clazz);
    } catch (JsonSyntaxException | IllegalStateException e) {
      logger.error(() -> "Cannot deserialize object.", e);
      throw new DeserializationException(e);
    }

    logger.info(() -> "Object was deserialized.");
    return result;
  }
}
