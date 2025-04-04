package oogasalad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.base.architecture.Game;
import oogasalad.gui.Gui;
import oogasalad.view.DinosaurGameBuilderView;

/**
 * This is the main class of the OOGASalad Platformer Game Sandbox. Run the start method to open the
 * program.
 */
public class Main extends Application {

    public static Game game;
    /**
     * Start of the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

    /**
     * Create a new game and open the gui.
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        game = new Game();

        // Create the editor UI, passing in the ECS scene
        //DinosaurGameBuilderView editorUI = new DinosaurGameBuilderView();
        // Wrap it in a JavaFX Scene and display
        //Scene fxScene = new Scene(editorUI, 1400, 800);
        //stage.setScene(fxScene);
        //stage.show();

        // Init GUI
        Gui gui = new Gui(stage, game);


    }
}
