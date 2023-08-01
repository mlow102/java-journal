package cs3500.pa05.view;

import cs3500.pa05.controller.screenctrls.ScreenController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


/**
 * A utility class for screen view functionality
 */
public class ScreenView {
  FXMLLoader loader;

  /**
   * Constructs a screen view.
   *
   * @param controller The controller for the screen
   * @param fxmlFilename The filename of the FXML file to load
   */
  public ScreenView(ScreenController controller, String fxmlFilename) {
    // look up and store the layout
    this.loader = new FXMLLoader();
    this.loader.setLocation(getClass().getClassLoader().getResource(fxmlFilename));

    this.loader.setController(controller);
  }

  /**
   * Loads a GUI window.
   *
   * @return The scene layout
   * @throws IllegalStateException If the GUI cannot be loaded
   */
  public Scene load() throws IllegalStateException {
    try {
      return this.loader.load();
    } catch (IOException exc) {
      throw new IllegalStateException("Unable to load layout.");
    }
  }
}