package cs3500.pa05;

import cs3500.pa05.controller.JournalAppController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The program entry point
 */
public class JournalApp extends Application  {
  /**
   * Starts the GUI for a .
   *
   * @param stage the JavaFX stage to add elements to
   */
  @Override
  public void start(Stage stage) {
    JournalAppController controller = new JournalAppController(stage);
    controller.run();
  }
}
