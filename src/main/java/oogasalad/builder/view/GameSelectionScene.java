package oogasalad.builder.view;

import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The GameSelectionScene allows the user to choose what game type they would like to design.
 */
public class GameSelectionScene extends Scene {

  private Group root;

  public GameSelectionScene() {
    super(new Group(), 1280, 720);
    root = (Group) getRoot();
    root.getChildren().addAll(getComponents());
  }

  private List<Node> getComponents() {
    Label sceneTitle = new Label("Select a game to build");
    Button dinoGame = new Button("DinoGame");
    Button geoDashGame = new Button("GeoDashGame");
    Button doodleJumpGame = new Button("DoodleJumpGame");
    return List.of(sceneTitle, dinoGame, geoDashGame, doodleJumpGame);
  }

}
