package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An item that can be added to a day in the calendar.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "itemType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Task.class, name = "task"),
    @JsonSubTypes.Type(value = Event.class, name = "event")
})
public abstract class Item {

  private String name;
  private String description;
  private DayOfWeek day;

  /**
   * Creates a new item.
   *
   * @param name the name of the item
   * @param description the description of the item (OPTIONAL)
   * @param day the day of the week the item is on
   */
  public Item(String name, String description, DayOfWeek day) {
    this.name = name;
    this.description = description;
    this.day = day;
  }

  /**
   *
   * @return the name of the item
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @return the description of the item
   */
  public String getDescription() {
    return description;
  }

  /**
   *
   * @return the day of the week the item is on
   */
  public DayOfWeek getDay() {
    return day;
  }

  /**
   *
   * @return the type of the item
   */
  public abstract ItemType getType();

  /**
   *
   * @return a copy of the item
   */
  public abstract Item copy();

}
