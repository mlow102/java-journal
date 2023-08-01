package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa05.controller.PasswordEncoder;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Represents a week in the bullet journal
 */
public class Week {
  private ArrayList<Day> days;
  private String name;
  private final String hashedPassword;
  private final String saltPassword;
  @JsonIgnore
  private Path path;
  private int maxNumEvents;
  private int maxNumTasks;

  /**
   * Creates a new week.
   *
   * @param name the name of the week
   * @param path path to the corresponding .bujo file
   */
  public Week(String name, Path path) {
    this.path = path;
    this.name = name;
    this.hashedPassword = "";
    this.saltPassword = "";
    this.maxNumEvents = Integer.MAX_VALUE; // Leave this at max until specified
    this.maxNumTasks = Integer.MAX_VALUE; // Better to say a user is never overbooked than always
    this.days = new ArrayList<>(List.of(
        new Day(DayOfWeek.SUNDAY), new Day(DayOfWeek.MONDAY), new Day(DayOfWeek.TUESDAY),
        new Day(DayOfWeek.WEDNESDAY), new Day(DayOfWeek.THURSDAY), new Day(DayOfWeek.FRIDAY),
        new Day(DayOfWeek.SATURDAY)));
  }

  /**
   * Creates a new week with given even and task constraints
   *
   * @param name the name of the week
   * @param path path to the corresponding .bujo file
   * @param passwordString password string to be encrypted with PBKDF2 algorithm
   * @param eventConstraint int number of max events per day for that week
   * @param taskConstraint  int number of max tasks per day for that week
   */
  public Week(String name, Path path, String passwordString,
              int eventConstraint, int taskConstraint) {
    this.path = path;
    this.name = name;

    if (passwordString.isEmpty()) {
      this.hashedPassword = "";
      this.saltPassword = "";
    } else {
      byte[] salt = new byte[16];
      new SecureRandom().nextBytes(salt);
      String strSalt = Base64.getEncoder().encodeToString(salt);

      this.saltPassword = strSalt;
      this.hashedPassword = PasswordEncoder.encode(passwordString, strSalt);
    }

    this.maxNumEvents = eventConstraint;
    this.maxNumTasks = taskConstraint;
    this.days = new ArrayList<>(List.of(
        new Day(DayOfWeek.SUNDAY), new Day(DayOfWeek.MONDAY), new Day(DayOfWeek.TUESDAY),
        new Day(DayOfWeek.WEDNESDAY), new Day(DayOfWeek.THURSDAY), new Day(DayOfWeek.FRIDAY),
        new Day(DayOfWeek.SATURDAY)));
  }

  /**
   * Creates a new week, with JSON creation capabilities.
   *
   * @param name the name of the week
   * @param hashedPassword hashed password for this week
   * @param saltPassword salt for hashing
   * @param maxNumEvents the maximum number of events allowed on this week
   * @param maxNumTasks the maximum number of tasks allowed on this week
   * @param days the days of the week
   */
  @JsonCreator
  public Week(
      @JsonProperty("name") String name,
      @JsonProperty("hashedPassword") String hashedPassword,
      @JsonProperty("salt") String saltPassword,
      @JsonProperty("maxNumEvents") int maxNumEvents,
      @JsonProperty("maxNumTasks") int maxNumTasks,
      @JsonProperty("days") ArrayList<Day> days) {
    this.path = null;
    this.hashedPassword = hashedPassword;
    this.saltPassword = saltPassword;
    this.name = name;
    this.maxNumEvents = maxNumEvents;
    this.maxNumTasks = maxNumTasks;
    this.days = days;
  }

  /**
   * Setter for the week name
   *
   * @param name new name of the week
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   *
   * @param maxNumEvents the maximum number of events allowed on this week
   *                     without being overbooked
   */
  public void setMaxNumEvents(int maxNumEvents) {
    this.maxNumEvents = maxNumEvents;
  }

  /**
   *
   * @param maxNumTasks the maximum number of tasks allowed on this week
   *                    without being overbooked
   */
  public void setMaxNumTasks(int maxNumTasks) {
    this.maxNumTasks = maxNumTasks;
  }

  /**
   *
   * @param path new path to which this week is tied in this session
   */
  public void setPath(Path path) {
    this.path = path;
  }

  /**
   *
   * @return the name of the week
   */
  public String getName() {
    return name;
  }

  /**
   * @return hashed password for this week, no password if hashedPassword is empty
   */
  public String getHashedPassword() {
    return hashedPassword;
  }

  /**
   * @return hash salt for the stored password for this week, no password if saltPassword is empty
   */
  public String getSaltPassword() {
    return saltPassword;
  }

  /**
   *
   * @return the path to the corresponding .bujo file
   */
  public Path getPath() {
    return path;
  }

  /**
   *
   * @return a deep copy of the days in this week
   */
  public ArrayList<Day> getDays() {
    return new ArrayList<>(days);
  }

  /**
   *
   * @return the maximum number of events allowed on this week without being overbooked
   */
  public int getMaxNumEvents() {
    return maxNumEvents;
  }

  /**
   *
   * @return the maximum number of tasks allowed on this week without being overbooked
   */
  public int getMaxNumTasks() {
    return maxNumTasks;
  }

  /**
   *
   * @return task list for the current week
   */
  @JsonIgnore
  public ArrayList<Task> getTaskList() {
    ArrayList<Task> taskList = new ArrayList<>();
    for (Day day : days) {
      for (Item item : day.getItems()) {
        if (item.getType().equals(ItemType.TASK)) {
          taskList.add((Task) item);
        }
      }
    }
    return taskList;
  }

  /**
   *
   * @param item the item to add to the week
   * @throws IllegalArgumentException if the item is already in that day
   */
  public void addItem(Item item) throws IllegalArgumentException {
    days.get(item.getDay().ordinal()).addItem(item, maxNumEvents, maxNumTasks);
  }

  /**
   *
   * @param item the item to remove from the week
   * @throws IllegalAccessException if the item is not in this week
   */
  public void removeItem(Item item) throws IllegalAccessException {
    days.get(item.getDay().ordinal()).removeItem(item.getName());
  }

  /**
   * Checks whether the provided password is valid
   *
   * @param inputPassword input password to be checked against the true password
   * @return true if password is valid
   */
  public boolean isPasswordValid(String inputPassword) {
    return PasswordEncoder.matches(inputPassword, hashedPassword, saltPassword);
  }
}
