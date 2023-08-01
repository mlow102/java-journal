package cs3500.pa05.controller;

import cs3500.pa05.model.DayOfWeek;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.ItemType;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Week;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the StorageManager class.
 */
class StorageManagerTest {

  /**
   * Tests that a week can be written to and read from the file system.
   */
  @Test
  public void testFileInteractions() {
    // Set up a week with some sample contents
    Week original = new Week("week", Path.of("src/test/artifacts/"));
    original.setMaxNumEvents(10);
    original.setMaxNumTasks(10);
    original.addItem(new Task("task", "description", DayOfWeek.MONDAY));
    original.addItem(new Event("event", "description", DayOfWeek.THURSDAY,
        "3:00 PM", "30 Min", ItemType.EVENT));

    // Write a week to the file system
    Path filePath = Path.of("src", "test", "resources", "artifacts", "testFileInteractions.bujo");
    try {
      StorageManager.writeFile(original, filePath);
    } catch (Exception e) {
      Assertions.fail("Failed to write file");
    }

    // Read the week from the file system
    Week copy = StorageManager.readFile(filePath);

    // Validate Equality
    Assertions.assertEquals(original.getName(), copy.getName());
    Assertions.assertEquals(original.getMaxNumEvents(), copy.getMaxNumEvents());
    Assertions.assertEquals(original.getMaxNumTasks(), copy.getMaxNumTasks());
    Assertions.assertEquals(original.getDays().get(1).getItems().get(0).getName(),
        copy.getDays().get(1).getItems().get(0).getName());
    Assertions.assertEquals(original.getDays().get(4).getItems().get(0).getName(),
        copy.getDays().get(4).getItems().get(0).getName());

  }

  /**
   * Tests that an exception is thrown when a file with an illegal extension is read.
   */
  @Test
  public void checkIllegalExtension() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      StorageManager.readFile(Path.of("src/test/artifacts/testFileInteractions.txt"));
    });
  }

  /**
   * Tests that an exception is thrown when a nonexistent file is selected
   */
  @Test
  public void checkNonExistentFile() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      StorageManager.readFile(Path.of("src/test/artifacts/nonExistentFile.bujo"));
    });
  }

  /**
   * Tests that the constructor works
   */
  @Test
  public void testConstructorWorks() {
    Assertions.assertDoesNotThrow(() ->
      new StorageManager());
  }
}