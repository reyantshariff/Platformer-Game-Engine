package oogasalad.view.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.config.GameConfig;
import oogasalad.view.player.GameRunner;

/**
 * Superclass for GamePlayerScene and GamePreview Scene
 * @author Hsuan-Kai Liao, Reyan Shariff
 */
public class GameDisplayScene extends ViewScene {
  private final StackPane root;
  private final Button returnButton;
  private final GameRunner gameRunner;

  /**
   * Class constructor
   * */
  public GameDisplayScene() {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    gameRunner = new GameRunner();

    root = (StackPane) getScene().getRoot();

    returnButton = new Button(GameConfig.getText("returnToBuilderButton"));
    returnButton.setOnAction(e -> {
      deactivate();
      MainViewManager.getInstance().switchTo("Builder");
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    root.getChildren().add(returnButton);
  }

  /**
   * Gets Root
   */
  public StackPane getRoot() {
    return root;
  }

  /**
   * Gets Return Button
   */
  public Button getReturnButton() {
    return returnButton;
  }

  /**
   * Gets game runner
   */
  public GameRunner getGameRunner() {
    return gameRunner;
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    gameRunner.pause();
  }
}
