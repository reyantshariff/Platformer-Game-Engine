package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.player.dinosaur.DinosaurGameScene;
import oogasalad.Main;

public class EnterPlayerAction extends GameAction{

  public EnterPlayerAction(GameObject parent) {super(parent);}
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
