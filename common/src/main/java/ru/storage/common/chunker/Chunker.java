package ru.storage.common.chunker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Chunker implements ByteChunker {
  private final int chunkSize;
  private final byte[] stopWordChunk;

  public Chunker(int chunkSize, String stopWord) {
    this.chunkSize = chunkSize;
    this.stopWordChunk = initStopWordChunk(stopWord);
  }

  private byte[] initStopWordChunk(String stopWord) {
    byte[] bytes = new byte[chunkSize];
    byte[] stopWordBytes = stopWord.getBytes();
    System.arraycopy(stopWordBytes, 0, bytes, 0, stopWordBytes.length);
    return bytes;
  }

  @Override
  public byte[] getStopWordChunk() {
    return stopWordChunk;
  }

  @Override
  public List<byte[]> split(byte[] bytes) {
    List<byte[]> chunks = new ArrayList<>();

    for (int index = 0; index < bytes.length; index += chunkSize) {
      if (bytes.length - index < chunkSize) {
        byte[] buffer = new byte[chunkSize];

        if (bytes.length - index >= 0) {
          System.arraycopy(bytes, index, buffer, 0, bytes.length - index);
        }

        chunks.add(buffer);
        break;
      }

      chunks.add(Arrays.copyOfRange(bytes, index, index + chunkSize));
    }

    chunks.add(stopWordChunk);
    return chunks;
  }

  @Override
  public byte[] join(List<byte[]> chunks) {
    byte[] bytes = new byte[chunkSize * chunks.size()];

    for (int index = 0; index < chunks.size(); index++) {
      if (chunkSize >= 0) {
        System.arraycopy(chunks.get(index), 0, bytes, index * chunkSize, chunkSize);
      }
    }

    return bytes;
  }
}
