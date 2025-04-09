package oogasalad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.view.gui.Gui;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import oogasalad.view.screens.MainMenuView;

/**
 * This is the main class of the OOGASalad Platformer Game Sandbox. Run the start method to open the
 * program.
 */
public class Main extends Application {

  private Game game;

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
    game = new Game();
    showMainMenu(game, stage);

    /**
    // Create an example builder UI
    BuilderView builderUI = new BuilderView(1400, 800);
    Scene builderScene = builderUI.getScene();
    Stage builderStage = new Stage();
    builderStage.setScene(builderScene);
    builderStage.show();
     */
  }

  private static void showMainMenu(Game curGame, Stage stage) {
    MainMenuView menu = new MainMenuView();
    Scene menuScene = menu.createMainMenu(stage, () -> launchGameScene(curGame, stage));

    stage.setScene(menuScene);
    stage.setTitle("OOGASalad Game");
    stage.show();
  }

  private static void launchGameScene(Game game, Stage stage) {
    DinosaurGameScene dinoScene = new DinosaurGameScene("Dinosaur");
    game.addScene(dinoScene);
    game.changeScene(dinoScene.getName());
    Gui gui = new Gui(stage, game);
  }
}