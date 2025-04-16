package oogasalad.model.engine.base.behavior;

import com.google.cloud.Timestamp;
import java.io.IOException;
import oogasalad.model.config.PasswordConfig;
import oogasalad.model.config.PasswordHashingException;
import oogasalad.model.profile.Password;
import oogasalad.model.profile.PlayerData;
import oogasalad.model.profile.SessionManagement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import java.awt.Dimension;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.BehaviorController;
import oogasalad.model.engine.component.Camera;
import oogasalad.model.engine.component.Collider;
import oogasalad.model.engine.component.Transform;
import oogasalad.view.scene.MainViewManager;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameInfo;
import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

public abstract class BehaviorBaseTest extends ApplicationTest {

    private Game game;
    private GameScene scene1;
    private GameScene scene2;
    private GameObject obj1;
    private GameObject obj2;
    private Behavior behavior1;
    private Behavior behavior2;

    @Override
    public void start(Stage stage) throws PasswordHashingException, IOException {
        MainViewManager viewManager = new MainViewManager(stage);

        String username = "justin1";
        Password password = Password.fromPlaintext("justin1");
        String fullName = "justin";

        SessionManagement.login((new PlayerData(username, fullName, password, Timestamp.now())), false);
        SessionManagement.tryAutoLogin();
        viewManager.switchToMainMenu();
    }

    @BeforeEach
    public void generalSetUp() {
        obj1 = new GameObject("Object1");
        obj2 = new GameObject("Object2");
        Transform transform1 = obj1.addComponent(Transform.class);
        Transform transform2 = obj2.addComponent(Transform.class);
        transform1.setScaleX(100);
        transform1.setScaleY(100);
        transform2.setScaleX(100);
        transform2.setScaleY(100);
        obj1.addComponent(BehaviorController.class);
        obj2.addComponent(BehaviorController.class);
        obj1.addComponent(Collider.class);
        obj2.addComponent(Collider.class);
        behavior1 = obj1.getComponent(BehaviorController.class).addBehavior();
        behavior2 = obj2.getComponent(BehaviorController.class).addBehavior();
        scene1 = new GameScene("Scene1");
        scene2 = new GameScene("Scene2");
        scene1.registerObject(obj1);
        scene1.registerObject(obj2);
        GameObject camera = new GameObject("Camera");
        Transform cameraTransform = camera.addComponent(Transform.class);
        camera.addComponent(Camera.class);
        cameraTransform.setScaleX(10000);
        cameraTransform.setScaleY(10000);
        scene1.registerObject(camera);
        game = new Game();
        game.setGameInfo(new GameInfo("", "", "", new Dimension(10000, 10000)));
        game.addScene(scene1);
        game.addScene(scene2);

        customSetUp();

        behavior1.setBehaviorController(obj1.getComponent(BehaviorController.class));
        behavior2.setBehaviorController(obj2.getComponent(BehaviorController.class));
        behavior1.awake();
        behavior2.awake();
    }

    @AfterEach
    public void cleanUp() {
        SessionManagement.logout();
    }

    public abstract void customSetUp();

    protected GameObject getObj1() {
        return obj1;
    }

    protected GameObject getObj2() {
        return obj2;
    }

    protected Behavior getBehavior1() {
        return behavior1;
    }

    protected Behavior getBehavior2() {
        return behavior2;
    }

    protected Game getGame() {
        return game;
    }

    protected GameScene getScene1() {
        return scene1;
    }

    protected GameScene getScene2() {
        return scene2;
    }

    protected void step() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                game.getCurrentScene().step(1);
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
