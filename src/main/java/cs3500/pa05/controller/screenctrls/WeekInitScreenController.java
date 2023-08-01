package cs3500.pa05.controller.screenctrls;

import static cs3500.pa05.controller.AppUtils.getUserChosenFile;
import static cs3500.pa05.controller.AppUtils.raisePopup;

import cs3500.pa05.controller.Lockbox;
import cs3500.pa05.model.ItemType;
import cs3500.pa05.model.Week;
import cs3500.pa05.view.ScreenView;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Represents a controller for thr WeekInitScreen
 */
public class WeekInitScreenController implements ScreenController {
  @FXML
  private Button fileChooserButton;
  @FXML
  private Label chosenFilepath;
  @FXML
  private TextField weekNameTextField;
  @FXML
  private TextField weekPasswordField;
  @FXML
  private TextField eventConstraintTextField;
  @FXML
  private TextField taskConstraintTextField;
  @FXML
  private Button createWeekButton;
  private Path pathToNewWeek;
  private Stage thisStage;
  private Lockbox<Week> weekLockbox;

  /**
   * Constructor for WeekInitScreenController
   *
   * @param thisStage the Stage
   * @param weekLockbox the lockbox
   */
  public WeekInitScreenController(Stage thisStage, Lockbox<Week> weekLockbox) {
    this.thisStage = thisStage;
    this.weekLockbox = weekLockbox;
    this.pathToNewWeek = null;
  }

  /**
   * Sets the scene and initializes the controls to respond to events
   */
  @Override
  public void run() {
    thisStage.setTitle("New Week");
    thisStage.setScene(
        new ScreenView(this, "weekInitScreen.fxml").load());
    thisStage.getIcons().add(new Image("file:src/main/resources/media/icon.png"));
    thisStage.getScene().setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    thisStage.show();

    initButtons();
    this.chosenFilepath.setText("");
  }

  /**
   * Initializes button actions
   */
  private void initButtons() {
    this.fileChooserButton.setOnAction(e -> handleChooseFile());
    this.createWeekButton.setOnMouseClicked(e -> handleCreateWeek());
  }

  /**
   * Handles key press events
   *
   * @param code the key code of the shortcut
   */
  private void handleKeyPress(KeyCode code) {
    int keyCode = code.getCode();
    switch (keyCode) {
      case KeyEvent.VK_S -> handleCreateWeek();
      case KeyEvent.VK_Q -> thisStage.hide();
      default -> {
          // Ignore
      }
    }
  }

  /**
   * Handler for "choose filepath" option
   * Gets a valid path to the file chooser
   */
  private void handleChooseFile() {
    Path userChosenPath;
    if ((userChosenPath = getUserChosenFile(false, thisStage)) != null) {
      this.pathToNewWeek = userChosenPath;
      this.chosenFilepath.setText(this.pathToNewWeek.toString());
    }
  }

  /**
   * Handler for "create week" option
   * Validates input and closes the window if week is created
   */
  private void handleCreateWeek() {
    Map<ItemType, Integer> constraints;

    if (pathToNewWeek == null) {
      raisePopup("Error - Path to file not selected",
          "Please, choose a path where you want to save the bullet journal week.");
    } else if (weekNameTextField.getCharacters().isEmpty()) {
      raisePopup("Error - Invalid week name",
          "Please, provide a week name.");
    } else if (weekNameTextField.getCharacters().length() > 50)  {
      raisePopup("Error - Invalid week name",
          "Please, provide a week name that is shorter than 50 characters.");
    } else if ((constraints = getConstraintsIfValid()) != null) {
      weekLockbox.putItemInLockbox(new Week(
          weekNameTextField.getCharacters().toString(),
          pathToNewWeek,
          weekPasswordField.getCharacters().toString(),
          constraints.get(ItemType.EVENT),
          constraints.get(ItemType.TASK)));
      thisStage.hide();
    }
  }

  /**
   * Helper to validate constraint input
   *
   * @return map from item type to parsed constraints
   */
  private Map<ItemType, Integer> getConstraintsIfValid() {
    Map<ItemType, Integer> constraints = new HashMap<>();

    try {
      constraints.put(ItemType.EVENT,
          Integer.parseInt(eventConstraintTextField.getCharacters().toString()));
      if (constraints.get(ItemType.EVENT) < 0) {
        raisePopup("Error - Invalid event constraint number.",
            "Please, provide a positive integer value for event constraint.");
        return null;
      }
    } catch (NumberFormatException e) {
      raisePopup("Error - Invalid event constraint number.",
          "Please, provide a integer value for event constraint.");
      return null;
    }

    try {
      constraints.put(ItemType.TASK,
          Integer.parseInt(taskConstraintTextField.getCharacters().toString()));
      if (constraints.get(ItemType.TASK) < 0) {
        raisePopup("Error - Invalid task constraint number.",
            "Please, provide a positive integer value for task constraint.");
        return null;
      }
    } catch (NumberFormatException e) {
      raisePopup("Error - Invalid task constraint number.",
          "Please, provide a integer value for task constraint.");
      return null;
    }

    return constraints;
  }
}
