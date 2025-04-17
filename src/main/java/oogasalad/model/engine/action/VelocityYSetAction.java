package oogasalad.model.engine.action;

import oogasalad.model.engine.component.PhysicsHandler;

import java.util.function.BiConsumer;

/**
 * VelocityYSetAction is a class that extends SetComponentValueAction and is used to set the Y
 * velocity of the object.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */

public class VelocityYSetAction extends SetComponentValueAction<Double, PhysicsHandler> {

  @Override
  protected PhysicsHandler supplyComponent() {
    return getComponent(PhysicsHandler.class);
  }

  @Override
  protected BiConsumer<PhysicsHandler, Double> provideSetter() {
    return PhysicsHandler::setVelocityY;
  }

  @Override
  protected Double defaultParameter() {
    return 0.0;
  }
}
