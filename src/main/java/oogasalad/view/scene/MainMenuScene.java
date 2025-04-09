package oogasalad.view.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Main menu view with play and builder options
 */
public class MainMenuScene extends ViewScene {

  /**
   * Constructs a new MainMenuScene to display the main menu with game selection and builder options.
   *
   * @param manager The MainViewManager used to switch scenes.
   */
  public MainMenuScene(MainViewManager manager) {
    super(new VBox(), 1280, 720);

    VBox root = (VBox) getScene().getRoot();
    root.setAlignment(Pos.CENTER);
    root.setSpacing(20);

    Label title = new Label("Welcome to OOGASalad");
    title.setFont(new Font("Arial", 30));

    ComboBox<String> gameSelector = new ComboBox<>();
    gameSelector.getItems().addAll("Dino Game", "Geometry Dash");
    gameSelector.setValue("Dino Game");

    Button playButton = new Button("Play Game");
    Button buildButton = new Button("Build Game");

    playButton.setOnAction(e ->
        manager.switchTo(new GamePlayerScene(manager, gameSelector.getValue()))
    );

    root.getChildren().addAll(title, gameSelector, playButton, buildButton);
  }
}