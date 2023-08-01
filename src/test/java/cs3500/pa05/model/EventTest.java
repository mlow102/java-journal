package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Event class.
 */
class EventTest {

  Event event;

  /**
   * Sets up the event for testing.
   */
  @BeforeEach
  void setUp() {
    event = new Event("event", "description",
        DayOfWeek.MONDAY, "12:00 PM", "1 Hr", ItemType.EVENT);
  }

  /**
   * Tests the serialization of the event.
   */
  @Test
  void testSerialization() {
    Event eventCopy = new Event(event.getName(),
        event.getDescription(), event.getDay(), event.getStartTime(),
        event.getDuration(), event.getType());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode serializedEvent = mapper.convertValue(eventCopy, JsonNode.class);
    String expected = "{\"itemType\":\"event\",\"name\":\"event\",\"description"
        + "\":\"description\",\"day\":\"MONDAY\",\"duration\":\"1 Hr\",\"type\":"
        + "\"EVENT\",\"startTime\":\"12:00 PM\"}";
    String actual = serializedEvent.toString();
    assertEquals(expected, actual);
  }

  /**
   * Tests the getStartTime method.
   */
  @Test
  void getStartTime() {
    assertEquals("12:00 PM", event.getStartTime());
  }

  /**
   * Tests the getDuration method.
   */
  @Test
  void getDuration() {
    assertEquals("1 Hr", event.getDuration());
  }

  /**
   * Tests the getType method.
   */
  @Test
  void getType() {
    assertEquals(ItemType.EVENT, event.getType());
  }
}