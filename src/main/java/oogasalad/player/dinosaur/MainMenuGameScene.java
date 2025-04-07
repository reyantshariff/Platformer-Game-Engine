package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.EnterPlayerAction;
import oogasalad.engine.component.ImageComponent;
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
    Player player = makeTitleTest();
    Player player2 = makeButtonTest();
  }

  private Player makeTitleTest() {
    Player title = instantiateObject(Player.class);

    TextComponent textComponent = title.addComponent(TextComponent.class);
    textComponent.setText("Welcome to OOOOOOOGASALAD");
    textComponent.setX(400);
    textComponent.setY(200);
    textComponent.setStyleClass("text-component");

    return title;
  }

  private Player makeButtonTest() {
    Player button = instantiateObject(Player.class);

    TextComponent textComponent = button.addComponent(TextComponent.class);
    textComponent.setText("Enter Dinosaur");
    textComponent.setX(500);
    textComponent.setY(300);
    textComponent.setStyleClass("text-component");

    InputHandler input = button.addComponent(InputHandler.class);
    EnterPlayerAction enterPlayer = new EnterPlayerAction();
    input.registerAction(KeyCode.SPACE, enterPlayer.getClass());

    return button;
  }

  /**
   * Deactivate game components when scene is deactivated.
   */
  @Override
  public void onDeactivated() {

  }
}
