package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a task in the bullet journal
 */
public class Task extends Item {
  private boolean complete;

  /**
   * Creates a new task.
   *
   * @param name the name of the task
   * @param description the description of the task
   * @param day the day of the task
   */
  public Task(String name, String description, DayOfWeek day) {
    super(name, description, day);
    this.complete = false;
  }

  /**
   * Creates a new task, with JSON creation capabilities.
   *
   * @param name the name of the task
   * @param description the description of the task
   * @param day the day of the task
   * @param complete true if the task is complete, false otherwise
   * @param type type of item
   */
  @JsonCreator
  public Task(
      @JsonProperty("name") String name,
      @JsonProperty ("description") String description,
      @JsonProperty ("day") DayOfWeek day,
      @JsonProperty ("complete") boolean complete,
      @JsonProperty ("type") ItemType type
  ) {
    super(name, description, day);
    this.complete = complete;
  }

  /**
   *
   * @return the type of the item
   */
  @Override
  public ItemType getType() {
    return ItemType.TASK;
  }

  /**
   * Marks the task as complete.
   */
  public void markComplete() {
    this.complete = true;
  }

  /**
   *
   * @return true if the task is complete, false otherwise
   */
  public boolean getComplete() {
    return complete;
  }

  /**
   *
   * @return a deep copy of this task
   */
  @Override
  public Item copy() {
    return new Task(this.getName(), this.getDescription(), this.getDay(),
        this.getComplete(), this.getType());
  }

}
