package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Day class.
 */
class DayTest {

  Day day;

  /**
   * Sets up the tests.
   */
  @BeforeEach
  void setUp() {
    day = new Day(DayOfWeek.MONDAY);
    day.addItem(new Task("task1", "description", DayOfWeek.FRIDAY),
        1, 1);
    day.addItem(new Event("event1", "description",
        DayOfWeek.FRIDAY, "9:00 AM",
        "1 HR", ItemType.EVENT), 1, 1);
  }

  /**
   * Tests serialization
   */
  @Test
  void testSerialization() {
    Day dayCopy = new Day(day.getDayOfWeek(), day.getItems());
    ObjectMapper mapper = new ObjectMapper();
    JsonNode serializedDay = mapper.convertValue(dayCopy, JsonNode.class);
    String expected = "{\"items\":[{\"itemType\":\"event\","
        + "\"name\":\"event1\",\"description\":\"description\","
        + "\"day\":\"FRIDAY\",\"duration\":\"1 HR\",\"type\":\"EVENT\","
        + "\"startTime\":\"9:00 AM\"},{\"itemType\":\"task\",\"name\":\"task1\","
        + "\"description\":\"description\",\"day\":\"FRIDAY\",\"complete\":false,"
        + "\"type\":\"TASK\"}],\"dayOfWeek\":\"MONDAY\"}";
    String actual = serializedDay.toString();
    assertEquals(expected, actual);
  }

  /**
   * Tests tasks overbooked
   */
  @Test
  void checkTasksOverbooked() {
    assertFalse(day.checkTasksOverbooked(1));
    day.addItem(new Task("task2", "description", DayOfWeek.FRIDAY),
        1, 1);
    assertTrue(day.checkTasksOverbooked(1));
  }

  /**
   * Tests events overbooked
   */
  @Test
  void checkEventsOverbooked() {
    assertFalse(day.checkEventsOverbooked(1));
    day.addItem(new Event("event2", "description",
        DayOfWeek.FRIDAY, "9:00 AM", "1 HR", ItemType.EVENT),
        1, 1);
    assertTrue(day.checkEventsOverbooked(1));
  }

  /**
   * Checks when both are overbooked
   */
  @Test
  void checkBothOverbooked() {
    assertFalse(day.checkTasksOverbooked(1));
    assertFalse(day.checkEventsOverbooked(1));
    day.addItem(new Event("event2", "description",
        DayOfWeek.FRIDAY, "9:00 AM", "1 HR",
        ItemType.EVENT), 1, 1);
    day.addItem(new Task("task2", "description", DayOfWeek.FRIDAY),
        1, 1);
    assertTrue(day.getOverbooked(1, 1));
  }

  /**
   * Tests adding an item
   */
  @Test
  void addItem() {
    day.addItem(new Task("task2", "description", DayOfWeek.FRIDAY),
        1, 1);
    day.addItem(new Event("event2", "description",
        DayOfWeek.FRIDAY, "9:00 AM", "1 HR", ItemType.EVENT), 1, 1);
    assertEquals(4, day.getItems().size());
    assertThrows(IllegalArgumentException.class,
        () -> day.addItem(new Event("event2", "description",
            DayOfWeek.FRIDAY, "9:00 AM", "1 HR", ItemType.EVENT), 1, 1));
  }

  /**
   * Tests getting overbooked
   */
  @Test
  void getOverbooked() {
    assertFalse(day.getOverbooked(1, 1));
    day.addItem(new Task("task2", "description", DayOfWeek.FRIDAY),
        1, 1);
    assertTrue(day.getOverbooked(1, 1));
  }

  /**
   * Tests removing an item
   */
  @Test
  void removeItem() {
    try {
      day.removeItem("task1");
    } catch (IllegalAccessException e) {
      fail();
    }
    assertEquals(1, day.getItems().size());
    assertThrows(IllegalAccessException.class, () -> day.removeItem("task1"));
  }

  /**
   * Tests getting items
   */
  @Test
  void getItems() {
    assertEquals(2, day.getItems().size());
  }
}