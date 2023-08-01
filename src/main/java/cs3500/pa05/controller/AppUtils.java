package cs3500.pa05.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class containing utility static methods for app's normal operation
 */
public final class AppUtils {
  private static String lastVisitedDirectory = System.getProperty("user.home");
  private static final String linkRegex = "((?>http(?>s)?:\\/\\/.)?(?>www\\.)"
      + "?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b(?>[-a-zA-Z0-9@:%_\\+.~#?&\\/=]*))";

  /**
   * Private constructor to prevent class from instantiation
   */
  private AppUtils() {}

  /**
   * Summons FileChooser window and allows the user to select a file
   *
   * @param showOpenDialog summons open dialog window if true, save dialog window if false
   * @param callerStage stage that called the file chooser
   * @return Path selected by the user
   */
  public static Path getUserChosenFile(boolean showOpenDialog, Stage callerStage) {
    FileChooser opener = new FileChooser();
    opener.setInitialDirectory(new File(lastVisitedDirectory));
    opener.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Bullet Journal Files", "*.bujo"));

    File chosenFile;
    if (showOpenDialog) {
      chosenFile = opener.showOpenDialog(callerStage);
    } else {
      chosenFile = opener.showSaveDialog(callerStage);
    }

    if (chosenFile != null) {
      lastVisitedDirectory = chosenFile.getParent().toString();
      return chosenFile.toPath();
    } else {
      return null;
    }
  }

  /**
   * Sets up the timeline for the splash screen
   *
   * @param stage stage on which the splash screen is to be displayed
   * @return timeline for the splash screen
   */
  public static Timeline setupSplashScreenTimeline(Stage stage) {
    Timeline tl = new Timeline();
    KeyFrame start = new KeyFrame(Duration.seconds(0.0),
        new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
    KeyFrame middle = new KeyFrame(Duration.seconds(0.75),
        new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));
    KeyFrame end = new KeyFrame(Duration.seconds(1),
        new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));

    tl.getKeyFrames().addAll(start, middle, end);
    tl.play();
    return tl;
  }

  /**
   * Toggles the media player on/off
   *
   * @param player Media player playing the background music
   */
  public static void handleTogglePlayer(MediaPlayer player) {
    if (player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
      player.pause();
    } else {
      player.play();
    }
  }

  /**
   * Formats the description in view mode so that the links are clickable
   *
   * @param textFlow TextFlow object, to which formatted text will be released
   * @param itemDescription String description to be processed
   */
  public static void highlightLinksInTextFlow(TextFlow textFlow, String itemDescription) {
    ArrayList<String> splitDesc = new ArrayList<>(List.of(itemDescription.split(linkRegex)));

    Pattern linkPattern = Pattern.compile(linkRegex);
    Matcher matcher = linkPattern.matcher(itemDescription);
    ArrayList<String> links = new ArrayList<>();
    while (matcher.find()) {
      links.add(matcher.group(1));
    }

    if (splitDesc.size() == 0 && links.size() != 0) {
      for (String link : links) {
        Hyperlink newLink = new Hyperlink(link);
        newLink.setFont(Font.font("Niramit Regular", 15));

        newLink.setOnAction(e -> {
          try {
            java.awt.Desktop.getDesktop().browse(URI.create(link));
          } catch (IOException ex) {
            raisePopup("Warning - Can't open link",
                "Couldn't automatically open link. If the link is a website, "
                    + "make sure it is preceeded by \'www.\'");
          }
        });

        textFlow.getChildren().add(newLink);
      }
    }

    for (int i = 0; i < (splitDesc.size() + links.size()); i++) {
      Node thisNode;

      if (i % 2 == 0) { // Text
        Text text = new Text(splitDesc.get(i / 2));
        text.setFont(Font.font("Niramit Regular", 15));
        thisNode = text;
      } else { // Links
        Hyperlink link = new Hyperlink(links.get(i / 2));
        link.setFont(Font.font("Niramit Regular", 15));

        int finalI = i;
        link.setOnAction(e -> {
          try {
            java.awt.Desktop.getDesktop().browse(URI.create(links.get(finalI / 2)));
          } catch (IOException ex) {
            raisePopup("Warning - Can't open link",
                "Couldn't automatically open link. If the link is a website, "
                + "make sure it is preceeded by \'www.\'");
          }
        });
        thisNode = link;
      }

      textFlow.getChildren().add(thisNode);
    }
  }


  /**
   * @param indicator Label indicator of whether the day is overbooked
   * @param isOverbooked boolean, true if overbooked
   */
  public static void setCommitmentIndicator(Label indicator, boolean isOverbooked) {
    if (isOverbooked) {
      indicator.setText("Overbooked!");
      indicator.setStyle("-fx-background-color: #f09ca1; -fx-border-color: #d1e8eb;");
    } else {
      indicator.setText("");
      indicator.setStyle("-fx-background-color:  #A0CA92; -fx-border-color: #d1e8eb;");
    }
  }

  /**
   * Summons a pop-up to inform the user about an error or a successfully completed operation
   *
   * @param popupName window title, short description of the message
   * @param popupText text inside the pop-up window, more verbose description
   */
  public static void raisePopup(String popupName, String popupText) {
    Label popupLabel = new Label(popupText);
    popupLabel.setFont(Font.font("Niramit Regular", 15));
    popupLabel.setTextFill(Color.valueOf("#000000"));
    HBox errorContent = new HBox(popupLabel);
    errorContent.setPadding(new Insets(20, 30, 20, 30));
    errorContent.setMinSize(300, 100);
    Stage errorPopup = new Stage();
    errorPopup.getIcons().add(new Image("file:src/main/resources/media/icon.png"));
    errorPopup.setTitle(popupName);
    errorPopup.setScene(new Scene(errorContent));
    errorPopup.show();
  }
}
