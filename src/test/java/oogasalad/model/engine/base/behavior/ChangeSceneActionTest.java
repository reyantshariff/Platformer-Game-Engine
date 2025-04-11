package oogasalad.model.engine.base.behavior;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import oogasalad.model.engine.action.ChangeSceneAction;
import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

public class ChangeSceneActionTest extends ActionsTest {

    @Override
    public void customSetUp() {
        ChangeSceneAction action = getBehavior1().addAction(ChangeSceneAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        step();
        assertEquals(getScene1().getName(), getGame().getCurrentScene().getName());

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                getAction().onPerform(getScene2().getName());
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }

        step();
        assertEquals("Scene2", getGame().getCurrentScene().getName());
    }
}
