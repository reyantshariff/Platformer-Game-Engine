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
    Player dino = instantiateObject(Player.class);

    ImageComponent iPlayer = dino.addComponent(ImageComponent.class);
    iPlayer.setImagePath("/oogasalad/dinosaur/DinoStart.png");
    iPlayer.setX(50);
    iPlayer.setY(620);

    InputHandler input = dino.addComponent(InputHandler.class);
    EnterPlayerAction enterPlayerAction = new EnterPlayerAction();
    input.registerAction(KeyCode.valueOf("SPACE"), enterPlayerAction.getClass());

    Player ground = instantiateObject(Player.class);

    ImageComponent iGround = ground.addComponent(ImageComponent.class);
    iGround.setImagePath("/oogasalad/dinosaur/Track.png");
    iGround.setX(0);
    iGround.setY(680);
  }

  @Override
  public void onDeactivated() {

  }
}
