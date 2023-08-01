package cs3500.pa05.controller.screenctrls;

import static cs3500.pa05.controller.AppUtils.getUserChosenFile;
import static cs3500.pa05.controller.AppUtils.handleTogglePlayer;
import static cs3500.pa05.controller.AppUtils.raisePopup;
import static cs3500.pa05.controller.AppUtils.setCommitmentIndicator;
import static cs3500.pa05.controller.AppUtils.setupSplashScreenTimeline;
import static cs3500.pa05.controller.StorageManager.readFile;
import static cs3500.pa05.controller.StorageManager.writeFile;

import cs3500.pa05.controller.Lockbox;
import cs3500.pa05.model.Day;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.Item;
import cs3500.pa05.model.ItemType;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Week;
import cs3500.pa05.view.ScreenView;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Controller for operating the Week screen
 */
public class WeekScreenController implements ScreenController {
  // Menu options
  @FXML
  private MenuItem newFileMenuItem;
  @FXML
  private MenuItem openFileMenuItem;
  @FXML
  private MenuItem saveFileMenuItem;
  @FXML
  private MenuItem saveFileAsMenuItem;
  @FXML
  private MenuItem newTaskMenuItem;
  @FXML
  private MenuItem newEventMenuItem;
  @FXML
  private MenuItem editWeekParamsMenuItem;
  @FXML
  private MenuItem shortcutsMenuItem;
  // Buttons and Week Name
  @FXML
  private Label weekName;
  @FXML
  private Button saveFileButton;
  @FXML
  private Button addTaskButton;
  @FXML
  private Button addEventButton;
  // Day Commitment status
  @FXML
  private Label sundayCommitment;
  @FXML
  private Label mondayCommitment;
  @FXML
  private Label tuesdayCommitment;
  @FXML
  private Label wednesdayCommitment;
  @FXML
  private Label thursdayCommitment;
  @FXML
  private Label fridayCommitment;
  @FXML
  private Label saturdayCommitment;
  private Label[] weekDayCommitmentStatuses;
  // Day Bars
  @FXML
  private VBox sundayBar;
  @FXML
  private VBox mondayBar;
  @FXML
  private VBox tuesdayBar;
  @FXML
  private VBox wednesdayBar;
  @FXML
  private VBox thursdayBar;
  @FXML
  private VBox fridayBar;
  @FXML
  private VBox saturdayBar;
  @FXML
  private VBox taskbar;
  @FXML
  private ProgressBar progressBar;
  private VBox[] weekDayBars;
  // Internal fields
  private final MediaPlayer player;
  private Stage primaryStage;
  private Week week;

  /**
   * Constructor for WeekScreenController
   *
   * @param primaryStage stage on which the welcome screen is to be displayed
   * @param week to be displayed in the week view
   * @param player media player
   */
  public WeekScreenController(Stage primaryStage, Week week, MediaPlayer player) {
    this.primaryStage = primaryStage;
    this.week = week;
    this.player = player;
  }

  /**
   * Sets the scene and initializes the controls to respond to events
   */
  @Override
  public void run() {
    try {
      writeFile(week, week.getPath());
    } catch (IOException e) {
      raisePopup("Warning - Failed to save",
          "Failed to write to provided file,"
              + " consider using Save As not to lose progeress");
    }

    primaryStage.setTitle("Week View");
    primaryStage.getIcons().add(new Image("file:src/main/resources/media/icon.png"));

    primaryStage.setScene(new ScreenView(this, "splashScreen.fxml").load());
    setupSplashScreenTimeline(primaryStage).setOnFinished(e -> runWeekScreen());
    primaryStage.show();
  }

  /**
   * Sets the scene and initializes the controls to respond to events
   */
  private void runWeekScreen() {
    primaryStage.setScene(
        new ScreenView(this, "weekScreen.fxml").load());
    primaryStage.getScene().setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    primaryStage.show();

    initConvenienceFields();
    displayWeekItems();
    displayTaskbarItems();
    initButtons();
    weekName.setText(week.getName());
  }

  /**
   * Initializes convenience fields
   */
  private void initConvenienceFields() {
    weekDayBars = new VBox[] {
        sundayBar, mondayBar, tuesdayBar, wednesdayBar, thursdayBar, fridayBar, saturdayBar
    };

    weekDayCommitmentStatuses = new Label[] {
        sundayCommitment, mondayCommitment, tuesdayCommitment,
        wednesdayCommitment, thursdayCommitment, fridayCommitment, saturdayCommitment
    };
  }

  /**
   * Displays current state of the week
   */
  private void displayWeekItems() {
    for (int i = 0; i < weekDayBars.length; i++) {
      Day thisDay = week.getDays().get(i);
      ArrayList<Item> itemsThisDay = thisDay.getItems();

      VBox dayColumn = weekDayBars[i];
      double columnWidth = dayColumn.getWidth();
      dayColumn.getChildren().clear();
      weekDayBars[i].setSpacing(5);

      for (Item item : itemsThisDay) {
        if (item.getType().equals(ItemType.TASK)) {
          VBox itemCard = createTaskCard(columnWidth
              - dayColumn.getPadding().getLeft() - dayColumn.getPadding().getRight(), (Task) item);
          weekDayBars[i].getChildren().add(itemCard);
          itemCard.setOnMouseClicked(e -> handleItemView(item));
        } else {
          VBox itemCard = createEventCard(columnWidth
              - dayColumn.getPadding().getLeft() - dayColumn.getPadding().getRight(), (Event) item);
          weekDayBars[i].getChildren().add(itemCard);
          itemCard.setOnMouseClicked(e -> handleItemView(item));
        }



      }

      setCommitmentIndicator(weekDayCommitmentStatuses[i],
          thisDay.getOverbooked(week.getMaxNumEvents(), week.getMaxNumTasks()));
    }
  }

  /**
   * Displays the task bar
   */
  private void displayTaskbarItems() {
    ArrayList<Task> taskList = week.getTaskList();

    double taskBarWidth = taskbar.getWidth();
    taskbar.getChildren().clear();
    taskbar.setSpacing(5);

    int completedCount = 0;
    for (Task task : taskList) {
      if (task.getComplete()) {
        completedCount++;
      }

      VBox taskCard = createTaskbarCard(taskBarWidth, task);
      taskbar.getChildren().add(taskCard);
      taskCard.getChildren().get(0).setOnMouseClicked(e -> {
        try {
          week.removeItem(task);
          Task completed = new Task(task.getName(),
              task.getDescription(), task.getDay(), !task.getComplete(), ItemType.TASK);
          week.addItem(completed);
        } catch (IllegalAccessException ex) {
          throw new RuntimeException(ex);
        }
        displayTaskbarItems();
        displayWeekItems();
      });
    }

    // Update the completion bar
    progressBar.setProgress((double) completedCount / taskList.size());
  }

  /**
   * Initializes button actions
   */
  private void initButtons() {
    // Menu Options
    this.newFileMenuItem.setOnAction(e -> handleNewWeek());
    this.openFileMenuItem.setOnAction(e -> handleOpenAnotherWeek());
    this.saveFileMenuItem.setOnAction(e -> handleSaveInPlace());
    this.saveFileAsMenuItem.setOnAction(e -> handleSaveAs());
    this.newEventMenuItem.setOnAction(e -> handleAddEvent());
    this.newTaskMenuItem.setOnAction(e -> handleAddTask());
    this.editWeekParamsMenuItem.setOnAction(e -> handleEditWeekParams());
    this.shortcutsMenuItem.setOnAction(e -> raisePopup("Available shortcuts",
        """
            - ctrl + s to save
            - ctrl + n to create a new week
            - ctrl + e to create a new event
            - ctrl + t to create a new task
            - ctrl + o to open a ".bojo" file
            - ctrl + d to delete an event or task
            - ctrl + q to save and quit
            - ctrl + m to mute"""));

    // Buttons
    this.saveFileButton.setOnAction(e -> handleSaveInPlace());
    this.addTaskButton.setOnAction(e -> handleAddTask());
    this.addEventButton.setOnAction(e -> handleAddEvent());
  }

  /**
   * Edits commitment level and week name for the current week
   */
  private void handleEditWeekParams() {
    Stage editCommStage = new Stage();
    ScreenController taskScreenController =
        new EditWeekScreenController(editCommStage, this.week);
    taskScreenController.run();

    editCommStage.setOnHiding(e -> {
      displayWeekItems();
      weekName.setText(week.getName());
    });
  }

  /**
   * Handles adding a new task
   */
  private void handleAddTask() {
    Stage addTaskStage = new Stage();
    Lockbox<Task> newTaskLockbox = new Lockbox<>();
    ScreenController taskScreenController =
        new TaskScreenController(addTaskStage, newTaskLockbox);
    taskScreenController.run();

    addTaskStage.setOnHiding(e -> {
      try {
        week.addItem(newTaskLockbox.getItemInLockbox());
      } catch (IllegalStateException ise) { //Ignore, just means user pressed delete
      } catch (Exception exe) {
        raisePopup("Error",
            "Task could not be added, please try a different task name");
        handleAddTask();
      }
      displayWeekItems();
      displayTaskbarItems();
    });
  }

  /**
   * Handles adding a new event
   */
  private void handleAddEvent() {
    Stage addEventStage = new Stage();
    Lockbox<Event> newEventLockbox = new Lockbox<>();
    ScreenController eventScreenController =
        new EventScreenController(addEventStage, newEventLockbox);
    eventScreenController.run();

    addEventStage.setOnHiding(e -> {
      try {
        week.addItem(newEventLockbox.getItemInLockbox());
      } catch (IllegalStateException ise) { //Ignore, just means user pressed delete
      } catch (Exception exe) {
        raisePopup("Error",
            "Event could not be added, please try a different event name");
        handleAddEvent();
      }
      displayWeekItems();
      displayTaskbarItems();
    });
  }

  /**
   * Handles creating a new week
   */
  private void handleNewWeek() {
    Stage pupUpStage = new Stage();
    Lockbox<Week> newWeekLockbox = new Lockbox<>();
    ScreenController weekInitScreenController =
        new WeekInitScreenController(pupUpStage, newWeekLockbox);
    weekInitScreenController.run();

    pupUpStage.setOnHiding(e -> {
      try {
        week = newWeekLockbox.getItemInLockbox();
        primaryStage.hide();

        primaryStage = new Stage();
        ScreenController weekScreenController
            = new WeekScreenController(primaryStage, week, player);
        weekScreenController.run();
      } catch (IllegalStateException ignored) {
        // Ignore
      }
    });
  }

  /**
   * Handles opening a new week
   */
  private void handleOpenAnotherWeek() {
    Path weekPath;
    if ((weekPath = getUserChosenFile(true, primaryStage)) != null) {
      week = readFile(weekPath);
      week.setPath(weekPath);

      if (!week.getHashedPassword().isEmpty()) {
        Stage popUpStage = new Stage();
        popUpStage.getIcons().add(new Image("file:src/main/resources/media/icon.png"));

        Lockbox<Week> passwordAuthLockbox = new Lockbox<>();
        passwordAuthLockbox.putItemInLockbox(week);
        ScreenController checkPassController
            = new CheckPasswordScreenController(popUpStage, passwordAuthLockbox);
        checkPassController.run();

        popUpStage.setOnHiding(e -> {
          try {
            passwordAuthLockbox.getItemInLockbox();
            primaryStage.hide();

            primaryStage = new Stage();
            ScreenController weekScreenController
                = new WeekScreenController(primaryStage, week, player);
            weekScreenController.run();
          } catch (IllegalStateException ignored) {
            // Ignore
          }
        });
      } else {
        primaryStage.hide();
        primaryStage = new Stage();
        ScreenController weekScreenController
            = new WeekScreenController(primaryStage, week, player);
        weekScreenController.run();
      }
    }
  }

  /**
   * Handles saving the .bujo week file in place
   */
  private void handleSaveInPlace() {
    try {
      writeFile(week, week.getPath());
      raisePopup("Success",
          "This week's .bujo file is saved successfully");
    } catch (IOException e) {
      raisePopup("Error - Failed to save",
          "Failed to save this week's .bujo file."
              + " Try saving at another location through Save As...");
    }
  }

  /**
   * Handles Save As option
   */
  private void handleSaveAs() {
    Path userChosenPath;
    if ((userChosenPath = getUserChosenFile(false, primaryStage)) != null) {
      week.setPath(userChosenPath);
      handleSaveInPlace();
    }
  }

  /**
   * Handles clicking on item to view the item card
   */
  private void handleItemView(Item item) {

    // Handle opening tasks
    ItemType type = item.getType();
    if (type.equals(ItemType.TASK)) {
      Stage addTaskStage = new Stage();
      Lockbox<Task> newTaskLockbox = new Lockbox<>();
      newTaskLockbox.putItemInLockbox((Task) item);
      try {
        week.removeItem(item);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Error removing item from week");
      }
      ScreenController taskScreenController =
          new TaskScreenController(addTaskStage, newTaskLockbox);
      taskScreenController.run();

      addTaskStage.setOnHiding(e -> {
        try {
          Item newItem = newTaskLockbox.getItemInLockbox();
          if (newTaskLockbox.getItemInLockbox() != null) {
            week.addItem(newItem);
          }

        } catch (IllegalStateException ignored) {
          // Ignore
        }
        displayTaskbarItems();
        displayWeekItems();
      });

      // Handle opening events
    } else if (type.equals(ItemType.EVENT)) {
      Stage addEventStage = new Stage();
      Lockbox<Event> newEventLockbox = new Lockbox<>();
      newEventLockbox.putItemInLockbox((Event) item);
      try {
        week.removeItem(item);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Error removing item from week");
      }
      ScreenController eventScreenController =
          new EventScreenController(addEventStage, newEventLockbox);
      eventScreenController.run();

      addEventStage.setOnHiding(e -> {
        try {
          Item newItem = newEventLockbox.getItemInLockbox();
          if (newEventLockbox.getItemInLockbox() != null) {
            week.addItem(newItem);
          }
        } catch (IllegalStateException ignored) {
          // Ignore
        }
        displayWeekItems();
        displayTaskbarItems();
      });
    } else {
      throw new RuntimeException("Error: Invalid item type");
    }

  }

  /**
   * Handles key press events
   *
   * @param code the key code of the shortcut
   */
  private void handleKeyPress(KeyCode code) {
    int keyCode = code.getCode();
    switch (keyCode) {
      case KeyEvent.VK_M -> handleTogglePlayer(player);
      case KeyEvent.VK_E -> handleAddEvent();
      case KeyEvent.VK_T -> handleAddTask();
      case KeyEvent.VK_N -> handleNewWeek();
      case KeyEvent.VK_O -> handleOpenAnotherWeek();
      case KeyEvent.VK_S -> handleSaveInPlace();
      case KeyEvent.VK_Q -> {
        handleSaveInPlace();
        primaryStage.hide();
      }
      default -> {
        // Do nothing
      }
    }
  }

  /**
   *
   * @param cardWidth width of the card to be created
   * @param task task to be represented
   *
   * @return VBox card, representing the item
   */
  public VBox createTaskCard(double cardWidth, Task task) {
    VBox box = new VBox();
    box.setPadding(new Insets(5));
    if (task.getComplete()) {
      box.setStyle("-fx-border-radius: 10; -fx-border-color: #929191; "
          + "-fx-background-color: #FFFAF1; -fx-border-width: 3; "
          + "-fx-background-radius: 10;");
    } else {
      box.setStyle("-fx-border-radius: 10; -fx-border-color: #C2958A; "
          + "-fx-background-color: #FFFAF1; -fx-border-width: 3; "
          + "-fx-background-radius: 10;");
    }

    Text taskName = new Text(task.getName());
    taskName.setWrappingWidth(cardWidth - 20);
    taskName.setTextAlignment(TextAlignment.CENTER);
    if (task.getComplete()) {
      taskName.setFont(Font.font("Niramit Regular", FontWeight.BOLD, 15));
      taskName.setFill(Color.valueOf("#929191"));
      taskName.setStrikethrough(true);
    } else {
      taskName.setFont(Font.font("Niramit Regular", 15));
      taskName.setFill(Color.valueOf("#8a6961"));
    }

    if (!task.getDescription().equals("")) {
      Label taskDescription = new Label(task.getDescription());
      taskDescription.setFont(Font.font("Niramit Regular", 10));
      taskDescription.setMaxWidth(cardWidth);
      taskDescription.setAlignment(Pos.CENTER);
      box.getChildren().addAll(taskName, taskDescription);
    } else {
      box.getChildren().addAll(taskName);
    }

    return box;
  }

  /**
   *
   * @param cardWidth width of the card to be created
   * @param event the event to be displayed
   *
   * @return VBox card, representing the item
   */
  public VBox createEventCard(double cardWidth, Event event) {
    VBox box = new VBox();
    box.setPadding(new Insets(5));
    box.setStyle("-fx-border-radius: 10; -fx-border-color: #9DBEBB; "
        + "-fx-background-color: #FFFAF1; -fx-border-width: 3; "
        + "-fx-background-radius: 10;");

    Text eventName = new Text(event.getName());
    eventName.setFont(Font.font("Niramit Regular", 15));
    eventName.setFill(Color.valueOf("#468189"));
    eventName.setWrappingWidth(cardWidth - 20);
    eventName.setTextAlignment(TextAlignment.CENTER);

    Label eventTime = new Label(event.getStartTime());
    eventTime.setFont(Font.font("Niramit Regular", 10));
    eventTime.setMaxWidth(cardWidth);
    eventTime.setAlignment(Pos.CENTER);

    Label eventDuration = new Label(event.getDuration());
    eventDuration.setFont(Font.font("Niramit Regular", 10));
    eventDuration.setMaxWidth(cardWidth);
    eventDuration.setAlignment(Pos.CENTER);

    if (!event.getDescription().equals("")) {
      Label eventDescription = new Label(event.getDescription());
      eventDescription.setFont(Font.font("Niramit Regular", 10));
      eventDescription.setMaxWidth(cardWidth);
      eventDescription.setAlignment(Pos.CENTER);
      box.getChildren().addAll(eventName, eventTime, eventDuration, eventDescription);
    } else {
      box.getChildren().addAll(eventName, eventTime, eventDuration);
    }

    return box;
  }

  /**
   * Creates a card for the task, to be displayed in the taskbar.
   *
   * @param cardWidth width of the card to be created
   * @param task task to be represented
   *
   * @return VBox card, representing the item
   */
  public VBox createTaskbarCard(double cardWidth, Task task) {
    VBox box = new VBox();
    box.setPadding(new Insets(5));
    if (task.getComplete()) {
      box.setStyle("-fx-border-radius: 10; -fx-border-color: #929191; "
          + "-fx-background-color: #FFFAF1; -fx-border-width: 3; "
          + "-fx-background-radius: 10;");
    } else {
      box.setStyle("-fx-border-radius: 10; -fx-border-color: #C2958A; "
          + "-fx-background-color: #FFFAF1; -fx-border-width: 3; "
          + "-fx-background-radius: 10;");
    }

    CheckBox taskName = new CheckBox(task.getName());
    taskName.setSelected(task.getComplete());
    taskName.setMaxWidth(cardWidth);
    if (task.getComplete()) {
      taskName.setFont(Font.font("Niramit Regular", FontWeight.BOLD, 15));
      taskName.setTextFill(Color.valueOf("#929191"));
    } else {
      taskName.setFont(Font.font("Niramit Regular", 15));
      taskName.setTextFill(Color.valueOf("#8a6961"));
    }
    box.getChildren().addAll(taskName);

    return box;
  }
}
