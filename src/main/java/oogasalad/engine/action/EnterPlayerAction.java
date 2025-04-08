package oogasalad.engine.action;

import oogasalad.Main;
import oogasalad.engine.base.behavior.BehaviorAction;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.player.dinosaur.DinosaurGameScene;

public class EnterPlayerAction extends BehaviorAction<String> {
  @SerializableField
  private String parameter;


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
    //TODO: Make this use reflection
    DinosaurGameScene scene = new DinosaurGameScene(parameter);
    Main.game.addScene(scene);
    Main.game.changeScene("Dinosaur");
  }
}
