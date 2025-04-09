package oogasalad.view.screens;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuView {
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

    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> Platform.exit());

    root.getChildren().addAll(title, playButton, exitButton);
    return new Scene(root, 1280, 720);
  }
}
