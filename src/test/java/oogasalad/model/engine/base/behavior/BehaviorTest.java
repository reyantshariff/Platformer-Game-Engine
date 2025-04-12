package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import oogasalad.model.engine.action.VelocityXSetAction;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.model.engine.component.PhysicsHandler;
import oogasalad.model.engine.constraint.KeyPressConstraint;
import org.junit.jupiter.api.Test;


public class BehaviorTest extends BehaviorBaseTest {

  @Override
  public void customSetUp() {
    getObj1().addComponent(PhysicsHandler.class);
    getObj1().addComponent(InputHandler.class);
  }

  @Test
  public void execute_passesConstraints_executeBehavior() {
    getBehavior1().addAction(VelocityXSetAction.class).setParameter(10.0);
    getBehavior1().addConstraint(KeyPressConstraint.class).setParameter(KeyCode.A);
    getGame().keyPressed(KeyCode.A.getValue());
    step();
    assertEquals(getObj1().getComponent(PhysicsHandler.class).getVelocityX(), 10.0);
  }

  @Test
  public void execute_doesNotPassConstraints_doesNotExecuteBehavior() {
    getBehavior1().addAction(VelocityXSetAction.class).setParameter(10.0);
    getBehavior1().addConstraint(KeyPressConstraint.class).setParameter(KeyCode.A);
    getGame().keyPressed(KeyCode.D.getValue());
    step();
    assertNotEquals(getObj1().getComponent(PhysicsHandler.class).getVelocityX(), 10.0);
  }

  @Test
  public void addConstraint_addByClass_addsConstraint() {
    KeyPressConstraint constraint = getBehavior1().addConstraint(KeyPressConstraint.class);
    assertNotNull(constraint);
    assertEquals(getBehavior1().getConstraints().size(), 1);
    assertTrue(getBehavior1().getConstraints().contains(constraint));
  }

  @Test
  public void addConstraint_addByInstance_addsConstraint() {
    KeyPressConstraint constraint = new KeyPressConstraint();
    getBehavior1().addConstraint(constraint);
    assertNotNull(constraint);
    assertEquals(getBehavior1().getConstraints().size(), 1);
    assertTrue(getBehavior1().getConstraints().contains(constraint));
  }

  @Test
  public void removeConstraint_removeByClass_removesConstraint() {
    KeyPressConstraint constraint = getBehavior1().addConstraint(KeyPressConstraint.class);
    assertNotNull(constraint);
    assertEquals(getBehavior1().getConstraints().size(), 1);
    getBehavior1().removeConstraint(KeyPressConstraint.class);
    assertEquals(getBehavior1().getConstraints().size(), 0);
  }

  @Test
  public void addAction_addByClass_addsAction() {
    VelocityXSetAction action = getBehavior1().addAction(VelocityXSetAction.class);
    assertNotNull(action);
    assertEquals(getBehavior1().getActions().size(), 1);
    assertTrue(getBehavior1().getActions().contains(action));
  }

  @Test
  public void addAction_addByInstance_addsAction() {
    VelocityXSetAction action = new VelocityXSetAction();
    getBehavior1().addAction(action);
    assertNotNull(action);
    assertEquals(getBehavior1().getActions().size(), 1);
    assertTrue(getBehavior1().getActions().contains(action));
  }

  @Test
  public void removeAction_removeByClass_removesAction() {
    VelocityXSetAction action = getBehavior1().addAction(VelocityXSetAction.class);
    assertNotNull(action);
    assertEquals(getBehavior1().getActions().size(), 1);
    getBehavior1().removeAction(VelocityXSetAction.class);
    assertEquals(getBehavior1().getActions().size(), 0);
  }
}
