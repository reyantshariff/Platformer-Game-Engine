package oogasalad.view.scene;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.view.gui.Gui;
import oogasalad.view.player.dinosaur.DinosaurGameScene;

/**
 * Displays the game using a GUI canvas inside a JavaFX scene
 */
public class GamePlayerScene extends ViewScene {

  public GamePlayerScene(MainViewManager manager, String gameName) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();

    Game game = new Game();
    Gui gui = new Gui(game);

    GameScene scene = switch (gameName) {
      case "Dino Game" -> new DinosaurGameScene("Dino");
      default -> new DinosaurGameScene("Default");
    };

    game.addScene(scene);
    game.changeScene(scene.getName());

    Button returnButton = new Button("Main Menu");
    returnButton.setOnAction(e -> {
      gui.stop();
      manager.switchToMainMenu();
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    returnButton.setStyle("-fx-background-color: white; -fx-font-weight: bold;");

    root.getChildren().addAll(gui.getCanvas(), returnButton);
  }
}