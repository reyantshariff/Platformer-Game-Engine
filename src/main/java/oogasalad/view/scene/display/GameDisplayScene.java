package oogasalad.view.scene.display;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.player.GameRunner;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;

/**
 * Superclass for GamePlayerScene and GamePreview Scene
 * @author Hsuan-Kai Liao, Reyan Shariff
 */
public class GameDisplayScene extends ViewScene {

  private final StackPane root;
  private final Button returnButton;
  private final GameRunner gameRunner;

  private String returnSceneName;

  /**
   * Class constructor
   * */
  public GameDisplayScene() {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    gameRunner = new GameRunner();

    root = (StackPane) getScene().getRoot();

    returnButton = new Button(GameConfig.getText("returnButton"));
    returnButton.setOnAction(e -> {
      deactivate();
      MainViewManager.getInstance().switchTo(returnSceneName);
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    root.getChildren().add(returnButton);
  }

  /**
   * Set the return scene the return button will head to.
   */
  public void setReturnSceneName(String sceneName) {
    this.returnSceneName = sceneName;
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
   * Play the preview to start the game.
   */
  public void play(String filepath) {
    getRoot().getChildren().clear();
    getRoot().getChildren().add(getReturnButton());

    try {
      Parser<?> parser = new JsonParser(filepath);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      getGameRunner().setGame((Game) parser.parse(newNode));
      getGameRunner().play();
    } catch (ParsingException e) {
      throw new IllegalStateException(GameConfig.getText("failureToParse", e.getMessage()), e);
    }

    getRoot().getChildren().addFirst(getGameRunner().getCanvas());
    getGameRunner().getCanvas().requestFocus();
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    gameRunner.pause();
  }
}
