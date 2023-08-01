package cs3500.pa05.controller.screenctrls;

import static cs3500.pa05.controller.AppUtils.raisePopup;

import cs3500.pa05.model.ItemType;
import cs3500.pa05.model.Week;
import cs3500.pa05.view.ScreenView;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Represents a controller for the Commitment Level Change Screen
 */
public class EditWeekScreenController implements ScreenController {
  @FXML
  private Button cancelCommitmentLevelButton;
  @FXML
  private Button updateCommitmentLevelButton;
  @FXML
  private TextField updateWeekNameField;
  @FXML
  private TextField updateEventConstraintTextField;
  @FXML
  private TextField updateTaskConstraintTextField;
  private final Stage primaryStage;
  private Week weekToBeEdited;

  /**
   * Constructor for CommChangeScreenController
   *
   * @param primaryStage stage on which the commitment level editor screen is to be displayed
   * @param weekToBeEdited a week to be edited
   */
  public EditWeekScreenController(Stage primaryStage, Week weekToBeEdited) {
    this.primaryStage = primaryStage;
    this.weekToBeEdited = weekToBeEdited;
  }

  /**
   * Sets the scene and initializes the controls to respond to events
   */
  @Override
  public void run() {
    primaryStage.setTitle("Commitment Level Change");
    primaryStage.setScene(
        new ScreenView(this, "editWeekParamScreen.fxml").load());
    primaryStage.getScene().setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    primaryStage.getIcons().add(new Image("file:src/main/resources/media/icon.png"));
    primaryStage.show();

    initButtons();
    initFields();
  }

  /**
   * Set button on-action event handlers
   */
  private void initButtons() {
    this.cancelCommitmentLevelButton.setOnAction(e -> handleCancel());
    this.updateCommitmentLevelButton.setOnAction(e -> handleUpdate());
  }

  /**
   * Handles key press events
   *
   * @param code the key code of the shortcut
   */
  private void handleKeyPress(KeyCode code) {
    int keyCode = code.getCode();
    switch (keyCode) {
      case KeyEvent.VK_S, KeyEvent.VK_U -> handleUpdate();
      case KeyEvent.VK_D, KeyEvent.VK_Q -> handleCancel();
      default -> {
        // Ignore
      }
    }
  }

  /**
   * Populate the fields with data from the passed-in task, if applicable
   */
  private void initFields() {
    this.updateWeekNameField.setText(weekToBeEdited.getName());
    this.updateEventConstraintTextField.setText(
        Integer.toString(weekToBeEdited.getMaxNumEvents()));
    this.updateTaskConstraintTextField.setText(
        Integer.toString(weekToBeEdited.getMaxNumTasks()));
  }

  /**
   * Handler for "delete" option
   */
  private void handleCancel() {
    this.primaryStage.hide();
  }

  /**
   * Handler for "save" option
   */
  private void handleUpdate() {
    Map<ItemType, Integer> constraints;

    if (updateWeekNameField.getCharacters().isEmpty()) {
      raisePopup("Error - Invalid name",
          "Week name cannot be empty, please provide a name.");
    } else if (updateWeekNameField.getCharacters().length() > 50) {
      raisePopup("Error - Invalid name",
          "Please, provide a week name that is shorter than 50 characters.");
    } else if ((constraints = getConstraintsIfValid()) != null) {
      weekToBeEdited.setName(updateWeekNameField.getCharacters().toString());
      weekToBeEdited.setMaxNumEvents(constraints.get(ItemType.EVENT));
      weekToBeEdited.setMaxNumTasks(constraints.get(ItemType.TASK));
      primaryStage.hide();
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
          Integer.parseInt(updateEventConstraintTextField.getCharacters().toString()));
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
          Integer.parseInt(updateTaskConstraintTextField.getCharacters().toString()));
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