package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A day in the week
 */
public class Day {
  private ArrayList<Item> items;
  private DayOfWeek dayOfWeek;
  private boolean eventsOverbooked;
  private boolean tasksOverbooked;

  /**
   * Creates a new day.
   *
   * @param day the day of the week
   */
  public Day(DayOfWeek day) {

    this.dayOfWeek = day;
    this.items = new ArrayList<>();
  }

  /**
   * Creates a new day.
   *
   * @param day the day of the week
   * @param items the items on the day
   */
  @JsonCreator
  public Day(@JsonProperty ("day") DayOfWeek day,
             @JsonProperty("items") ArrayList<Item> items) {
    this.dayOfWeek = day;
    this.items = items;
  }

  /**
   *
   * @param maxTasks the maximum number of tasks allowed on this day
   * @return true if the number of tasks on this day is greater than maxTasks
   */
  public boolean checkTasksOverbooked(int maxTasks) {
    int count = 0;
    for (Item i : items) {
      if (i.getType() == ItemType.TASK) {
        count++;
      }
    }
    tasksOverbooked = (count > maxTasks);
    return tasksOverbooked;
  }

  /**
   *
   * @param maxEvents the maximum number of events allowed on this day
   * @return true if the number of events on this day is greater than maxEvents
   */
  public boolean checkEventsOverbooked(int maxEvents) {
    int count = 0;
    for (Item i : items) {
      if (i.getType() == ItemType.EVENT) {
        count++;
      }
    }
    eventsOverbooked = (count > maxEvents);
    return eventsOverbooked;
  }

  /**
   * Adds an item to the day.
   *
   * @param item the item to add to the day
   * @param maxEvents the maximum number of events allowed on this day
   * @param maxTasks the maximum number of tasks allowed on this day
   * @throws IllegalArgumentException if the item already exists in the day
   */
  public void addItem(Item item, int maxEvents, int maxTasks) throws IllegalArgumentException {
    for (Item i : items) {
      if (i.getName().equals(item.getName())) {
        throw new IllegalArgumentException("Item by that name already exists in this day");
      }
    }
    items.add(item);
    checkEventsOverbooked(maxEvents);
    checkTasksOverbooked(maxTasks);
    sortItems();
  }

  /**
   *
   * @param maxEvents the maximum number of events allowed on this day
   * @param maxTasks the maximum number of tasks allowed on this day
   * @return true if the day is overbooked, false otherwise
   */
  public boolean getOverbooked(int maxEvents, int maxTasks) {
    return checkEventsOverbooked(maxEvents) || checkTasksOverbooked(maxTasks);
  }

  /**
   *
   * @param itemName the name of the item to remove
   * @throws IllegalAccessException if the item does not exist in the day
   */
  public void removeItem(String itemName) throws IllegalAccessException {
    for (Item i : items) {
      if (i.getName().equals(itemName)) {
        items.remove(i);
        sortItems();
        return;
      }
    }
    throw new IllegalAccessException("Item does not exist in this day, cannot remove it");
  }

  /**
   * Returns a deep copy of the item list
   *
   * @return deep copy of item list
   */
  public ArrayList<Item> getItems() {
    ArrayList<Item> copy = new ArrayList<>();
    for (Item item : items) {
      copy.add(item.copy());
    }
    return copy;
  }

  /**
   *
   * @return the day of the week
   */
  public DayOfWeek getDayOfWeek() {
    return this.dayOfWeek;
  }

  /**
   * Sorts the items in the day by date and type
   */
  private void sortItems() {
    // Put the Events first, by date order
    List<Event> events = new ArrayList<>();
    List<Item> tasks = new ArrayList<>();
    for (Item i : items) {
      if (i.getType() == ItemType.EVENT) {
        events.add((Event) i);
      } else {
        tasks.add(i);
      }
    }

    // Sort the events by date
    Collections.sort(events, new Comparator<Event>() {
      @Override
      public int compare(Event o1, Event o2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime o1Time = LocalTime.parse(o1.getStartTime(), formatter);
        LocalTime o2Time = LocalTime.parse(o2.getStartTime(), formatter);
        return o1Time.compareTo(o2Time);
      }
    });

    items = new ArrayList<>(); //purge the old list
    items.addAll(events);
    items.addAll(tasks);
  }
}
