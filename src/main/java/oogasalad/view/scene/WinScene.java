package oogasalad.view.scene;

import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import oogasalad.model.config.GameConfig;

/**
 * View shown when the player completes a game.
 */
public class WinScene extends ViewScene {

  public WinScene(MainViewManager manager) {
    super(new VBox(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));


    VBox root = (VBox) getScene().getRoot();
    root.setAlignment(Pos.CENTER);
    root.setSpacing(20);

    Label congratsLabel = new Label("🎉 " + GameConfig.getText("winMessage"));
    congratsLabel.getStyleClass().add("title");

    Button mainMenuButton = new Button(GameConfig.getText("mainMenuButton"));
    mainMenuButton.setOnAction(e -> manager.switchTo("MainMenuScene"));


    root.getChildren().addAll(congratsLabel, mainMenuButton);
  }
}
