package oogasalad;

import java.util.function.Function;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.view.gui.Gui;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import oogasalad.view.screens.MainMenuView;

/**
 * This is the main class of the OOGASalad Platformer Game Sandbox. Run the start method to open the
 * program.
 */
public class Main extends Application {

  /**
   * Start of the program.
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Create a new game and open the gui.
   *
   * @param stage the primary stage for this application, onto which the application scene can be
   *              set. Applications may create other stages, if needed, but they will not be primary
   *              stages.
   */
  @Override
  public void start(Stage stage) {
    Game game = new Game();
    StackPane root = new StackPane();
    Scene scene = new Scene(root, 1280, 720);

    setKeyPressForGame(scene, game);

    showMainMenu(root, game, stage, scene);

    /**
    // Create an example builder UI
    BuilderView builderUI = new BuilderView(1400, 800);
    Scene builderScene = builderUI.getScene();
    Stage builderStage = new Stage();
    builderStage.setScene(builderScene);
    builderStage.show();
     */
  }

  private void setKeyPressForGame(Scene scene, Game game) {
    scene.setOnKeyPressed(e -> {
      KeyCode key = mapToEngineKeyCode(e.getCode());
      if (key != null) game.keyPressed(key.getValue());
    });

    scene.setOnKeyReleased(e -> {
      KeyCode key = mapToEngineKeyCode(e.getCode());
      if (key != null) game.keyReleased(key.getValue());
    });
  }

  private static void showMainMenu(Pane root, Game curGame, Stage stage, Scene curScene) {
    MainMenuView menu = new MainMenuView(
        selectedGame -> switchToGamePlayer(root, curGame, selectedGame),
        selectedGame -> switchToBuilder(root, curGame, selectedGame)
    );

    root.getChildren().setAll(menu.getMenuView());

    stage.setScene(curScene);
    stage.setTitle("OOGASalad Game");
    stage.show();
  }

  private static void switchToMode(
      Pane root,
      Game game,
      String selectedGame,
      Function<String, GameScene> sceneProvider
  ) {
    Gui gui = new Gui(game);
    GameScene scene = sceneProvider.apply(selectedGame);
    game.addScene(scene);
    game.changeScene(scene.getName());
    root.getChildren().setAll(gui.getCanvas());
  }

  private static void switchToGamePlayer(Pane root, Game game, String selectedGame) {
    switchToMode(root, game, selectedGame, Main::getSceneByName);
  }


  private static void switchToBuilder(Pane root, Game game, String selectedGame) {

    //switchToMode(root, game, selectedGame, Main::getBuilderSceneByName);
  }


  private static GameScene getSceneByName(String name) {
    return switch (name) {
      case "Dino Game" -> new DinosaurGameScene("Dino");
      default -> new DinosaurGameScene("");
    };
  }

  /**
   * Maps a JavaFX KeyCode to an engine KeyCode.
   *
   * @param code The JavaFX KeyCode.
   * @return The engine KeyCode, or null if the mapping fails.
   */
  private KeyCode mapToEngineKeyCode(javafx.scene.input.KeyCode code) {
    try {
      return KeyCode.valueOf(code.name());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

}