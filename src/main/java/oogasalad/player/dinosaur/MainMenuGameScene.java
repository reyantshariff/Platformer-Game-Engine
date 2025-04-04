package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.EnterPlayerAction;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.prefabs.Player;

public class MainMenuGameScene extends GameScene {

  public MainMenuGameScene(String name) {
    super(name);
  }

  /**
   * Initialize game components when scene is activated.
   */
  @Override
  public void onActivated() {
  }

  /**
   * Deactivate game components when scene is deactivated.
   */
  @Override
  public void onDeactivated() {
  }
}
