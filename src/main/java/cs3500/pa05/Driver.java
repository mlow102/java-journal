package cs3500.pa05;

/**
 * For JAR-file creation.
 */
public class Driver {

  /**
   * The program entry point
   *
   * @param args not applicable for this application
   */
  public static void main(String[] args) {
    try {
      JournalApp.launch(JournalApp.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
