package cs3500.pa05.controller;

import cs3500.pa05.controller.screenctrls.ScreenController;
import cs3500.pa05.controller.screenctrls.WeekScreenController;
import cs3500.pa05.controller.screenctrls.WelcomeScreenController;
import cs3500.pa05.model.Week;
import java.net.URI;
import java.nio.file.Path;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * The top-level cs3500.pa05.controller for the cs3500.pa05.JournalApp application.
 */
public class JournalAppController {
  private static MediaPlayer musicPlayer;
  private Stage primaryStage;
  Lockbox<Week> initWeekLockbox;

  /**
   * Creates a new JournalAppController
   *
   * @param primaryStage the Stage
   */
  public JournalAppController(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Runs the application.
   */
  public void run() {
    startBackgroundMusic();

    initWeekLockbox = new Lockbox<>();
    ScreenController welcomeScreenController
        = new WelcomeScreenController(primaryStage, initWeekLockbox, musicPlayer);
    welcomeScreenController.run();
    primaryStage.getIcons().add(new Image("file:src/main/resources/media/icon.png"));
    primaryStage.setOnHiding(e -> handleTransitionToWeek());
  }

  /**
   * Handles the transition from the welcome screen to the week screen
   */
  private void handleTransitionToWeek() {
    try {
      Week week = initWeekLockbox.getItemInLockbox();

      primaryStage = new Stage();
      ScreenController weekScreenController
          = new WeekScreenController(primaryStage, week, musicPlayer);
      weekScreenController.run();
    } catch (IllegalStateException ignored) {
      // Ignnored
    }
  }

  /**
   * Starts background music player
   */
  private void startBackgroundMusic() {
    URI musicFile = Path.of(
        "src", "main", "resources", "media", "flaing-piano-main-8783.mp3").toUri();
    musicPlayer = new MediaPlayer(new Media(musicFile.toString()));
    musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    musicPlayer.play();
  }
}
