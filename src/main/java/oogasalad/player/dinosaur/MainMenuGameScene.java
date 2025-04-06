package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.EnterPlayerAction;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.TextComponent;
import oogasalad.engine.component.Transform;
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
    Player player = makePlayerTest();
  }

  private Player makePlayerTest() {
    Player player = instantiateObject(Player.class);

    Transform t = player.addComponent(Transform.class);
    t.setX(100);
    t.setY(100);
    t.setScaleX(10);
    t.setScaleY(10);

    TextComponent textComponent = player.addComponent(TextComponent.class);
    textComponent.setText("Welcome to OOOOOOOGASALAD");
    textComponent.setX(600);
    textComponent.setY(300);

    InputHandler input = player.addComponent(InputHandler.class);
    EnterPlayerAction enterPlayer = new EnterPlayerAction();
    input.registerAction(KeyCode.SPACE, enterPlayer.getClass());

    return player;
  }

  /**
   * Deactivate game components when scene is deactivated.
   */
  @Override
  public void onDeactivated() {
  }
}
