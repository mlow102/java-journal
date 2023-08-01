package cs3500.pa05.controller.screenctrls;

import static cs3500.pa05.controller.AppUtils.raisePopup;

import cs3500.pa05.controller.Lockbox;
import cs3500.pa05.model.Week;
import cs3500.pa05.view.ScreenView;
import java.awt.event.KeyEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Controller for the check password screen
 */
public class CheckPasswordScreenController implements ScreenController {
  @FXML
  private Label weekName;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button cancelButton;
  @FXML
  private Button loginButton;
  private final Stage popupStage;
  private Week week;
  private Lockbox<Week> weekLockbox;

  /**
   * Constructor for TaskScreenController
   *
   * @param popupStage stage on which the task screen is to be displayed
   * @param weekLockbox lockbox for the week to be authenticated
   */
  public CheckPasswordScreenController(Stage popupStage, Lockbox<Week> weekLockbox) {
    this.popupStage = popupStage;
    this.week = weekLockbox.getItemInLockbox();

    weekLockbox.putItemInLockbox(null);
    this.weekLockbox = weekLockbox;
  }

  /**
   * Sets the scene and initializes the controls to respond to events
   */
  @Override
  public void run() {
    popupStage.setTitle("Authentication");
    popupStage.setScene(
        new ScreenView(this, "checkPasswordScreen.fxml").load());
    popupStage.getScene().setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    popupStage.getIcons().add(new Image("file:src/main/resources/media/icon.png"));
    popupStage.show();

    initFields();
    initButtons();
  }

  /**
   * Handles key press events
   *
   * @param code the key code of the shortcut
   */
  private void handleKeyPress(KeyCode code) {
    if (code.getCode() == KeyEvent.VK_Q) {
      handleCancel();
    }
  }

  /**
   * Populate the fields with data from the passed-in task, if applicable
   */
  private void initFields() {
    weekName.setText(week.getName());
  }

  /**
   * Set button on-action event handlers
   */
  private void initButtons() {
    this.loginButton.setOnAction(e -> handleLogin());
    this.cancelButton.setOnAction(e -> handleCancel());
  }

  /**
   * Handles login attempt
   */
  public void handleLogin() {
    System.out.println("Read password " + passwordField.getText());
    System.out.println("Hashed password " + week.getHashedPassword());

    if (week.isPasswordValid(passwordField.getText())) {
      weekLockbox.putItemInLockbox(week);
      this.popupStage.hide();
    } else {
      raisePopup("Error - Invalid password",
          "Password is incorrect. Try again!");
    }
  }

  /**
   * Handles canceling and returning to welcome screen
   */
  public void handleCancel() {
    this.popupStage.hide();
  }
}
