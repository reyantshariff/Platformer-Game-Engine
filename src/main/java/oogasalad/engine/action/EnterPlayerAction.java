package oogasalad.engine.action;

import oogasalad.Main;
import oogasalad.engine.base.behavior.BehaviorAction;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.player.dinosaur.DinosaurGameScene;

/**
 * The EnterPlayerAction class is used to enter the player into the game. It is responsible for
 * creating a new game scene and adding it to the game.
 */

public class EnterPlayerAction extends BehaviorAction<String> {
  @SerializableField
  private String parameter;

  /**
   * Default constructor for the EnterPlayerAction class.
   */
  public EnterPlayerAction() {
    super();
  }

  /**
   * The type of the constraint. This is used to classify the constraints being checked. Note: This
   * method MUST be override.
   *
   * @return the type of the constraint
   */
  @Override
  public ComponentTag ActionType() {
    return ComponentTag.INPUT;
  }

  /**
   * Check if the constraint is met.
   *
   * @param parameter the parameter to check against
   * @return true if the constraint is met, false otherwise
   */
  @Override
  protected void perform(String parameter) {
    // TODO: Make this use reflection
    DinosaurGameScene scene = new DinosaurGameScene(parameter);
    Main.game.addScene(scene);
    Main.game.changeScene("Dinosaur");
  }
}
