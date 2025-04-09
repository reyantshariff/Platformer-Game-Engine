package oogasalad;

import javafx.scene.canvas.Canvas;
import java.util.function.Function;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    Scene scene = new Scene(root,
        1280, 720);

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

    createReturnButton(root, game);
  }

  private static void createReturnButton(Pane root, Game game) {
    Button returnButton = new Button("Main Menu");
    returnButton.setOnAction(e -> showMainMenu(root,
        game, (Stage) root.getScene().getWindow(), root.getScene()));
    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    returnButton.setStyle("-fx-background-color: white; -fx-font-weight: bold;");
    root.getChildren().add(returnButton);
  }

  private static void switchToGamePlayer(Pane root, Game game, String selectedGame) {
    Game newGame = new Game();
    switchToMode(root, newGame, selectedGame, Main::getSceneByName);
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

}