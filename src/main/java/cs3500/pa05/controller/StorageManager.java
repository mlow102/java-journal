package cs3500.pa05.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.Week;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * A utility class for accessing the file system.
 */
public class StorageManager {

  /**
   * Reads a .bujo file from the file system.
   *
   * @param filePath the path to the file, including filename and extension
   * @return the week at the specified path
   */
  public static Week readFile(Path filePath) {

    if (!filePath.toString().endsWith(".bujo")) {
      throw new IllegalArgumentException("File must be a .bujo file");
    }

    ObjectMapper mapper = new ObjectMapper();
    File file = new File(filePath.toString());
    try {
      JsonNode weekSerialized = mapper.readTree(file);
      return mapper.convertValue(weekSerialized, Week.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("File not found");
    }

  }

  /**
   * Writes a week to the file system, as a .bojo file.
   *
   * @param week the week to write
   * @param filePath the path to the file, including filename and extension
   * @throws IOException if the file cannot be written to the path
   */
  public static void writeFile(Week week, Path filePath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File(filePath.toString());
    FileWriter writer = new FileWriter(file);
    JsonNode weekSerialized = mapper.convertValue(week, JsonNode.class);
    writer.write(weekSerialized.toString());
    writer.close();
  }

}
