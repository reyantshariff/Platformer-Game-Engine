package oogasalad;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.component.Transform;
import oogasalad.player.dinosaur.DinosaurGameScene;

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
    public void start(Stage stage) throws Exception {
        // Create new Game
        Game game = new Game();
        game.addScene(DinosaurGameScene.class, "Dinosaur");

        // === JavaFX UI Setup ===
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600); // size your game window here
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        stage.setTitle("OOGASalad Platformer");
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(e -> {
            KeyCode key = mapToEngineKeyCode(e.getCode());
            if (key != null && game.getCurrentScene() != null) {
                game.getCurrentScene().subscribeInputKey(key);
            }
        });

        // Start game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.getKeyFrames().add(new KeyFrame(
            Duration.seconds((double) 1 / FRAMES_PER_SECOND),
            event -> {
                game.step((double) 1 / FRAMES_PER_SECOND);
                render(gc, game.getCurrentScene());
            }
        ));
        gameLoop.play();
    }

    private void render(GraphicsContext gc, GameScene scene) {
        gc.clearRect(0, 0, 800, 600);

        // Simple draw loop — assume all GameObjects have Transform and (eventually) Sprite
        for (GameObject obj : scene.getAllObjects()) {
            Transform transform = obj.getComponent(Transform.class);
            if (transform != null) {
                gc.fillRect(transform.getX(), transform.getY(), transform.getScaleX(),
                    transform.getScaleY());
            }
        }
    }

    private KeyCode mapToEngineKeyCode(javafx.scene.input.KeyCode code) {
        try {
            return KeyCode.valueOf(code.name());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
