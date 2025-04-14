package oogasalad.view.scene;

import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import oogasalad.model.config.GameConfig;
import oogasalad.view.config.StyleConfig;

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

    ComboBox<String> gameSelector = setupGameSelector();

    HBox buttonBox = setupButtonBox();
    Button playButton = (Button) buttonBox.lookup("#playButton");
    Button buildButton = (Button) buttonBox.lookup("#buildButton");

    playButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select a Game JSON File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
      fileChooser.setInitialDirectory(new File("data/GameJsons"));

      String gameType = gameSelector.getValue();

      File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
      if (selectedFile != null) {
        String fileName = selectedFile.getName().replace(".json", "");

        manager.registerSceneFactory("GamePlayerScene", vm -> new GamePlayerScene(vm, fileName, gameType));
        manager.switchTo("GamePlayerScene");
      }
    });

    buildButton.setOnAction(e -> manager.switchTo("BuilderScene")
    );
    // Language and Theme Selections
    HBox selectorBox = setupSelectorBox();

    root.getChildren().addAll(title, gameSelector, buttonBox, selectorBox);
  }

  private HBox setupButtonBox() {
    HBox buttonBox = new HBox();
    Button playButton = new Button("PLAY GAME");
    playButton.setId("playButton");
    Button buildButton = new Button("AUTHORING ENVIRONMENT");
    buildButton.setId("buildButton");
    Button socialHubButton = new Button("SOCIAL HUB");
    socialHubButton.setId("socialHubButton");
    buttonBox.getChildren().addAll(playButton, buildButton, socialHubButton);
    buttonBox.setId("buttonBox");
    return buttonBox;
  }

  private ComboBox<String> setupGameSelector() {
    ComboBox<String> gameSelector = new ComboBox<>();
    gameSelector.getItems().addAll("DINO", "GEOMETRY DASH");
    gameSelector.setValue("SELECT A GAME");
    gameSelector.setId("gameSelector");
    return gameSelector;
  }

  private HBox setupSelectorBox() {
    HBox selectorBox = new HBox();
    ComboBox<String> languageSelector = setupLanguageSelector();
    ComboBox<String> themeSelector = setupThemeSelector();
    selectorBox.getChildren().addAll(languageSelector, themeSelector);
    selectorBox.setId("selectorBox");
    return selectorBox;
  }

  private ComboBox<String> setupLanguageSelector() {
    ComboBox<String> languageSelector = new ComboBox<>();
    languageSelector.setValue("SELECT A LANGUAGE");
    languageSelector.setId("menuSelector");
    languageSelector.getItems().addAll("ENGLISH", "ETC.");
    languageSelector.setOnAction(e -> {
      GameConfig.setLanguage(languageSelector.getValue());});
    return languageSelector;
  }

  private ComboBox<String> setupThemeSelector() {
    ComboBox<String> themeSelector = new ComboBox<>();
    themeSelector.setValue("SELECT A THEME");
    themeSelector.setId("menuSelector");
    themeSelector.getItems().addAll("DARK", "LIGHT", "FREAKY", "CLASSY", "FAIL");
    themeSelector.setOnAction(e -> {
      StyleConfig.setStylesheet(getScene(), themeSelector.getValue());
    });
    return themeSelector;
  }
}