package oogasalad.view.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.model.config.GameConfig;

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

    Label title = new Label("PLATFORMERS");
    title.setId("mainMenuTitle");

    ComboBox<String> gameSelector = new ComboBox<>();
    gameSelector.getItems().addAll("DINO", "GEOMETRY DASH");
    gameSelector.setValue("SELECT A GAME");
    gameSelector.setId("gameSelector");

    HBox buttonBox = new HBox();
    Button playButton = new Button("PLAY GAME");
    playButton.setId("playButton");
    Button buildButton = new Button("AUTHORING ENVIRONMENT");
    buildButton.setId("buildButton");
    Button socialHubButton = new Button("SOCIAL HUB");
    socialHubButton.setId("socialHubButton");
    buttonBox.getChildren().addAll(playButton, buildButton, socialHubButton);
    buttonBox.setId("buttonBox");

    playButton.setOnAction(e ->
        manager.switchTo(new GamePlayerScene(manager, gameSelector.getValue()))
    );

    buildButton.setOnAction(e -> manager.switchTo(new BuilderScene(manager))
    );
    // Language and Theme Selections
    HBox selectorBox = new HBox();
    ComboBox<String> languageSelector = new ComboBox<>();
    languageSelector.setValue("SELECT A LANGUAGE");
    languageSelector.setId("menuSelector");
    languageSelector.getItems().addAll("ENGLISH", "ETC.");
    languageSelector.setOnAction(e -> {
      GameConfig.setLanguage(languageSelector.getValue());});
    ComboBox<String> themeSelector = new ComboBox<>();
    themeSelector.setValue("SELECT A THEME");
    themeSelector.setId("menuSelector");
    themeSelector.getItems().addAll("DARK", "ETC.");
    // themeSelector.setOnAction(e -> {;}
    // GameConfig.setTheme(themeSelector.getValue());});
    // TODO: add setTheme to GameConfig
    selectorBox.getChildren().addAll(languageSelector, themeSelector);
    selectorBox.setId("selectorBox");

    root.getChildren().addAll(title, gameSelector, buttonBox, selectorBox);
  }
}