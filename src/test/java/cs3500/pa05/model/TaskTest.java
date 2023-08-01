package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



/**
 * Tests for the Task class.
 */
class TaskTest {

  Task task;

  /**
   * Sets up the task for testing.
   */
  @BeforeEach
  void setUp() {
    task = new Task("task", "description", DayOfWeek.MONDAY);
  }

  /**
   * Tests the serialization of the task.
   */
  @Test
  void testSerialization() {
    Task taskCopy = new Task(task.getName(),
        task.getDescription(), task.getDay(), task.getComplete(), task.getType());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode serializedTask = mapper.convertValue(taskCopy, JsonNode.class);
    String expected = "{\"itemType\":\"task\",\"name\":\"task\",\"description\":\"description"
        + "\",\"day\":\"MONDAY\",\"complete\":false,\"type\":\"TASK\"}";
    String actual = serializedTask.toString();
    assertEquals(expected, actual);
  }

  /**
   * Tests the getType method.
   */
  @Test
  void getType() {
    assertEquals(ItemType.TASK, task.getType());
  }

  /**
   * Tests the markComplete method.
   */
  @Test
  void markComplete() {
    assertFalse(task.getComplete());
    task.markComplete();
    assertTrue(task.getComplete());
  }
}