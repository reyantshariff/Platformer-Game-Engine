package oogasalad;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import oogasalad.engine.base.Game;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Game update frame rate.
     */
    public static final int FRAMES_PER_SECOND = 60;

    /**
     * Start of the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create new Game
        Game game = new Game();

        // Start game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.getKeyFrames().add(new KeyFrame(
            Duration.seconds((double) 1 / FRAMES_PER_SECOND),
            event -> game.step((double) 1 / FRAMES_PER_SECOND)
        ));
        gameLoop.play();
    }
}
