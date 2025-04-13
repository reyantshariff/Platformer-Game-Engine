package oogasalad.model.engine.base.behavior;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

import oogasalad.model.engine.action.ChangeSceneAction;


public class ChangeSceneActionTest extends ActionsTest<ChangeSceneAction> {

    @Override
    public void customSetUp() {
        ChangeSceneAction action = getBehavior1().addAction(ChangeSceneAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        List<String> sceneNames = new ArrayList<>();
        sceneNames.add(getScene1().getName());
        sceneNames.add(getScene2().getName());
        getGame().setLevelOrder(sceneNames);
        assertEquals(getScene1().getName(), getGame().getCurrentScene().getName());

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                getAction().onPerform("nextLevel");
            } finally {
                assertTrue(getScene2().getName().equals(getGame().getCurrentScene().getName()));
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }
}
