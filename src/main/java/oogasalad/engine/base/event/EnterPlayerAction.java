package oogasalad.engine.base.event;

import oogasalad.Main;
import oogasalad.player.dinosaur.DinosaurGameScene;

public class EnterPlayerAction extends GameAction {

  public EnterPlayerAction() {
    super();
  }

  /**
   * Trigger the action Implementations of this method should call checkConstraints before executing
   * the body
   */
  @Override
  public void dispatch() {
    Main.game.addScene(DinosaurGameScene.class, "Dinosaur");
    Main.game.changeScene("Dinosaur");
  }
}
