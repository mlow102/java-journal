package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An event that can be added to a day in the calendar.
 */
public class Event extends Item {

  private String startTime;
  private String duration;

  /**
   * Creates a new event.
   *
   * @param name the name of the event
   * @param description the description of the event (OPTIONAL)
   * @param day the day of the week the event is on
   * @param startTime the start time of the event, formatted as: "HH:MM AM/PM"
   * @param duration the duration of the event, formatted as: "XX Min" or "XX Hr YY Min" or "XX Hr"
   * @param type type of item
   */
  @JsonCreator
  public Event(
      @JsonProperty("name") String name,
      @JsonProperty ("description") String description,
      @JsonProperty ("day") DayOfWeek day,
      @JsonProperty ("start-time") String startTime,
      @JsonProperty ("duration") String duration,
      @JsonProperty ("type") ItemType type
  ) {
    super(name, description, day);
    this.startTime = startTime;
    this.duration = duration;
  }

  /**
   *
   * @return the event's start time, formatted as: "HH:MM AM/PM"
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   *
   * @return the event's duration, formatted as: "XX Min" or "XX Hr YY Min" or "XX Hr"
   */
  public String getDuration() {
    return duration;
  }

  /**
   *
   * @return the type of the item
   */
  @Override
  public ItemType getType() {
    return ItemType.EVENT;
  }

  /**
   *
   * @return a copy of this event
   */
  @Override
  public Item copy() {
    return new Event(this.getName(), this.getDescription(), this.getDay(),
        this.getStartTime(), this.getDuration(), this.getType());
  }

}
