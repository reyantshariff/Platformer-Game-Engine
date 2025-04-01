package oogasalad.menu.view;


import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The MenuScene class is the initial scene displayed to the user. It presents a choice between
 * entering the Social Hub, Game Player, and Game Builder.
 */
public class MenuScene extends Scene {

  private Group root;

  public MenuScene() {
    super(new Group(), 1280, 720);
    root = (Group) getRoot();
    root.getChildren().addAll(getComponents());
  }

  private List<Node> getComponents() {
    Label menuTitle = new Label("Menu");
    Button enterBuilder = new Button("Enter Builder");
    Button enterPlayer = new Button("Enter Player");
    Button enterSocialHub = new Button("Enter Social Hub");
    return List.of(menuTitle, enterBuilder, enterPlayer, enterSocialHub);
  }

}
