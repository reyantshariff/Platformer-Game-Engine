package oogasalad.engine.gameObject;
import java.util.UUID;
import oogasalad.engine.base.GameObject;
import oogasalad.engine.base.GameScene;
import oogasalad.engine.gameComponent.InputHandler;

public class Player extends GameObject {

  public Player(String name, UUID id, GameScene parentScene) {
    super(name, id, parentScene);
    addComponent(InputHandler.class);
  }
}
