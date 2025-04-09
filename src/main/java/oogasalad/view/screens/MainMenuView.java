package oogasalad.view.screens;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates the main JavaFX view for the main menu of the entire game.
 *
 * @author Justin Aronwald
 */
public class MainMenuView {

  /**
   *
   * @param stage - the primary JavaFX stage that is being used
   * @param onPlay - the function for the button that starts gameplay
   * @return - a JavaFX scene
   */
  public Scene createMainMenu(Stage stage, Runnable onPlay) {
    VBox root = new VBox(20);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(50));
    root.setStyle("-fx-background-color: white;");

    Label title = new Label("Welcome to OOGASalad");
    title.setStyle("-fx-text-fill: blue; -fx-font-size: 32px;");

    Button playButton = new Button("Play Game");
    playButton.setStyle("-fx-font-size: 20px;");
    playButton.setOnAction(e -> onPlay.run());

    Button builderButton = new Button("Build Game");
    playButton.setStyle("-fx-font-size: 20px;");

    root.getChildren().addAll(title, playButton, builderButton);
    return new Scene(root, 1280, 720);
  }
}
