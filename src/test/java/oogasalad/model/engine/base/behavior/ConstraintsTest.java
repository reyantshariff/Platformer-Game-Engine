package oogasalad.model.engine.base.behavior;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.BehaviorController;
import oogasalad.model.engine.component.Collider;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.architecture.Game;


public abstract class ConstraintsTest<T extends BehaviorConstraint> {

    private Game game;
    private GameScene scene;
    private GameObject obj1;
    private GameObject obj2;
    private Behavior behavior1;
    private Behavior behavior2;
    private T constraint;

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
        scene = new GameScene("Scene");
        scene.registerObject(obj1);
        scene.registerObject(obj2);
        game = new Game();
        game.addScene(scene);
        customSetUp();
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

    protected T getConstraint() {
        return constraint;
    }

    protected void setConstraint(T constraint) {
        this.constraint = constraint;
    }

    protected Game getGame() {
        return game;
    }

    protected void step() {
        scene.step(1);
    }

    @Test
    public void getComponent_getParentTransform_returnsParentTransform() {
        Transform transform = obj1.getComponent(Transform.class);
        assertEquals(transform, constraint.getComponent(Transform.class));
    }

    @Test
    public abstract void check_checkPosotive_returnsTrue();

    @Test
    public abstract void check_checkNegative_returnsFalse();

}
