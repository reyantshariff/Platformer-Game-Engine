package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.EnterPlayerAction;
import oogasalad.engine.component.ImageComponent;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.Transform;
import oogasalad.engine.prefabs.Player;

public class MainMenuGameScene extends GameScene {

  public MainMenuGameScene(String name) {
    super(name);
  }

  @Override
  public void onActivated() {
    Player enterPlayer = instantiateObject(Player.class);

    ImageComponent iPlayer = enterPlayer.addComponent(ImageComponent.class);
    iPlayer.setImagePath("/oogasalad/dinosaur/GameOver.png");
    iPlayer.setX(100);
    iPlayer.setY(100);

    InputHandler input = enterPlayer.addComponent(InputHandler.class);
    EnterPlayerAction enterPlayerAction = new EnterPlayerAction(enterPlayer);
    input.registerAction(KeyCode.valueOf("SPACE"), enterPlayerAction);
  }

  @Override
  public void onDeactivated() {

  }
}
