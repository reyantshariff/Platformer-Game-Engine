package oogasalad.view.scene;


import javafx.stage.Stage;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {

  private final Stage stage;
  private static MainViewManager instance;
  private ViewScene currentScene;

  /**
   * Constructs the MainViewManager with the given primary stage
   *
   * @param stage the JavaFX primary stage
   */
  public MainViewManager(Stage stage) {
    this.stage = stage;
    instance = this;
  }


  /**
   * Get instance - necessary for view scene switching
   *
   * @return - the main view manager object
   */
  public static MainViewManager getInstance() {
    return instance;
  }

  /**
   * Switches to the given ViewScene
   *
   * @param viewScene the new scene to display
   */
  public void switchTo(ViewScene viewScene) {
    // Stop current scene, if applicable
    if (currentScene != null) {
      currentScene.deactivate();
    }

    // Set new scene
    stage.setScene(viewScene.getScene());
    currentScene = viewScene;
    stage.show();
  }

  /**
   * Shortcut to go to the main menu
   */
  public void switchToMainMenu() {
    switchTo(new MainMenuScene(this));
  }
}
