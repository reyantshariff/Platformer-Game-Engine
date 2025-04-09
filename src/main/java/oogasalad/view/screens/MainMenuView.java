package oogasalad.view.screens;

import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Creates the main JavaFX view for the main menu of the entire game.
 *
 * @author Justin Aronwald
 */
public class MainMenuView {
  private final VBox menuBox = new VBox();
  private final ComboBox<String> gameSelector = new ComboBox<>();


  /**
   * The constructor for the MainMenuView that creates all of the buttons and components
   *
   * @param onPlay - the button that handles playing a game
   * @param onBuilder - the button that handles building a game
   */
  public MainMenuView(Consumer<String> onPlay, Consumer<String> onBuilder) {
    Label title = new Label("Welcome to OOGASalad");
    title.setFont(new Font("Arial", 30));

    Label subtitle = new Label("Select a game to play");
    subtitle.setFont(new Font("Arial", 15));

    gameSelector.getItems().addAll("Dinosaur Game", "Geometry Dash");
    gameSelector.setValue("Dino Game");

    Button playButton = new Button("Play Game");
    Button builderButton = new Button("Build Game");

    playButton.setOnAction(e -> onPlay.accept(gameSelector.getValue()));
    builderButton.setOnAction(e -> onBuilder.accept(gameSelector.getValue()));

    menuBox.getChildren().addAll(title, subtitle, gameSelector, playButton, builderButton);
    menuBox.setAlignment(Pos.CENTER);
    menuBox.setSpacing(20);
  }

  /**
   * Returns the view of the main menu scene
   *
   * @return - the VBox containing the main menu elements
   */
  public Node getMenuView() {
    return menuBox;
  }
}
