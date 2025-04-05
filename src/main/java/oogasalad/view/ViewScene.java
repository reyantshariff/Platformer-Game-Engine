package oogasalad.view;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class ViewScene {
  Scene myScene;

  public ViewScene(Parent root, double width, double height) {
    myScene = new Scene(root, width, height);
  }

}
