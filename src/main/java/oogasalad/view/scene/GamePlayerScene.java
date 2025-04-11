package oogasalad.view.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.view.gui.Gui;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import oogasalad.view.player.main.MainGameScene;

/**
 * Displays the game using a GUI canvas inside a JavaFX scene
 */
public class GamePlayerScene extends ViewScene {

  private final Gui gui;

  /**
   * Constructs a new GamePlayerScene to display a game within a JavaFX scene.
   *
   * @param manager  The MainViewManager used to switch scenes.
   * @param gameName The name of the game to be displayed.
   */
  public GamePlayerScene(MainViewManager manager, String gameName) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();

    Game game = new Game();
    gui = new Gui(game);

    GameScene scene = new DinosaurGameScene("Dino");
    GameScene main = new MainGameScene("main");

    game.addScene(main);
    game.addScene(scene);
    game.changeScene(scene.getName());

    Button returnButton = new Button("Main Menu");
    returnButton.setOnAction(e -> {
      deactivate();
      manager.switchToMainMenu();
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    returnButton.setStyle("-fx-background-color: white; -fx-font-weight: bold;");

    root.getChildren().addAll(gui.getCanvas(), returnButton);
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    try {
      gui.stop();
    } catch (Exception e) {
      throw new RuntimeException("Error deactivating scene: " + e.getMessage());
    }
  }
}