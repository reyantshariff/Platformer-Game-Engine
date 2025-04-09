package oogasalad.view.scene;


import javafx.stage.Stage;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {

  private final Stage stage;

  /**
   * Constructs the MainViewManager with the given primary stage
   *
   * @param stage the JavaFX primary stage
   */
  public MainViewManager(Stage stage) {
    this.stage = stage;
  }

  /**
   * Switches to the given ViewScene
   *
   * @param viewScene the new scene to display
   */
  public void switchTo(ViewScene viewScene) {
    stage.setScene(viewScene.getScene());
    stage.show();
  }

  /**
   * Shortcut to go to the main menu
   */
  public void switchToMainMenu() {
    switchTo(new MainMenuScene(this));
  }
}
