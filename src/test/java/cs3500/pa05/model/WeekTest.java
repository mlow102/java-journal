package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Week class.
 */
class WeekTest {

  Week week;

  /**
   * Sets up the week for testing.
   */
  @BeforeEach
  void setUp() {
    week = new Week("Week 1", Path.of("src"));
    Item task = new Task("task", "description", DayOfWeek.MONDAY);
    Item event = new Event("event", "description", DayOfWeek.MONDAY,
        "3:00 PM", "30 Min", ItemType.EVENT);
    week.addItem(task);
    week.addItem(event);
  }

  /**
   * Tests the serialization of the week.
   */
  @Test
  void testSerialization() {
    Week weekCopy = new Week(week.getName(), "", "",
        week.getMaxNumEvents(), week.getMaxNumTasks(), week.getDays());
    ObjectMapper mapper = new ObjectMapper();
    JsonNode weekSerialized = mapper.convertValue(weekCopy, JsonNode.class);
    String expected = "{\"name\":\"Week 1\",\"hashedPassword\":\"\",\"maxNumEvents\":2147483647,"
        + "\"maxNumTasks\":2147483647,\"days\":[{\"items\":[],\"dayOfWeek\":\"SUNDAY\"},"
        + "{\"items\":[{\"itemType\":\"event\",\"name\":\"event\",\"description\":\"description\","
        + "\"day\":\"MONDAY\",\"duration\":\"30 Min\",\"type\":\"EVENT\",\"startTime\":\"3:00 PM\""
        + "},{\"itemType\":\"task\",\"name\":\"task\",\"description\":\"description\","
        + "\"day\":\"MONDAY\",\"complete\":false,\"type\":\"TASK\"}],\"dayOfWeek\":\"MONDAY\"},"
        + "{\"items\":[],\"dayOfWeek\":\"TUESDAY\"},{\"items\":[],\"dayOfWeek\":\"WEDNESDAY\"},"
        + "{\"items\":[],\"dayOfWeek\":\"THURSDAY\"},{\"items\":[],\"dayOfWeek\":\"FRIDAY\"},"
        + "{\"items\":[],\"dayOfWeek\":\"SATURDAY\"}],\"saltPassword\":\"\"}";
    String actual = weekSerialized.toString();
    assertEquals(expected, actual);
  }

  /**
   * Tests the add item method.
   */
  @Test
  void maxNumEvents() {
    week.setMaxNumEvents(10);
    assertEquals(10, week.getMaxNumEvents());
  }

  /**
   * Tests the add item method.
   */
  @Test
  void maxNumTasks() {
    week.setMaxNumTasks(10);
    assertEquals(10, week.getMaxNumTasks());
  }

  /**
   * Tests the constraint constructor
   */
  @Test
  void testConstraintConstructor() {
    Path testPath = Path.of("example", "path");
    Week week = new Week("Week 1", testPath,
        "password", 10, 10);
    assertEquals(10, week.getMaxNumEvents());
    assertEquals(10, week.getMaxNumTasks());

    assertTrue(week.isPasswordValid("password"));
    assertFalse(week.isPasswordValid("not a password"));

    assertEquals(testPath, week.getPath());
  }

  /**
   * Tests the getTaskList method.
   */
  @Test
  void testGetTaskList() {
    assertEquals(1, week.getTaskList().size()); // 1 task in this week of 2 Items
  }

  /**
   * Tests the password constructor
   */
  @Test
  void testPasswordConstructor() {
    assertDoesNotThrow(() -> new Week("Week", Path.of("fake/path"),
        "", 5, 5));
  }

  /**
   * Tests setting the name and path, and removing an item
   */
  @Test
  void testSetNameAndPath() {
    week.setName("Week 2");
    assertEquals("Week 2", week.getName());
    week.setPath(Path.of("src", "test"));
    assertEquals(Path.of("src", "test"), week.getPath());
    try {
      week.removeItem(week.getTaskList().get(0));
    } catch (IllegalAccessException e) {
      fail();
    }
    assertEquals(0, week.getTaskList().size());
  }

}