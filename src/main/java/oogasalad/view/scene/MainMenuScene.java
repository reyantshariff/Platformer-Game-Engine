package oogasalad.view.scene;

<<<<<<< HEAD
import java.io.File;
=======
import static oogasalad.model.config.GameConfig.LOGGER;
>>>>>>> languageUiIntegration
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
    super(new VBox(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));

    VBox root = (VBox) getScene().getRoot();
    root.setAlignment(Pos.CENTER);

    Label title = new Label(GameConfig.getText("mainMenuTitle"));
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
    Button playButton = new Button(GameConfig.getText("playButton"));
    playButton.setId("playButton");
    Button buildButton = new Button(GameConfig.getText("buildButton"));
    buildButton.setId("buildButton");
    Button socialHubButton = new Button(GameConfig.getText("socialHubButton"));
    socialHubButton.setId("socialHubButton");
    buttonBox.getChildren().addAll(playButton, buildButton, socialHubButton);
    buttonBox.setId("buttonBox");
    return buttonBox;
  }

  private ComboBox<String> setupGameSelector() {
    ComboBox<String> gameSelector = new ComboBox<>();
    gameSelector.getItems().addAll(GameConfig.getTextList("gameSelector"));
    gameSelector.setValue(GameConfig.getText("gameSelectorInitialValue"));
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
    languageSelector.setValue(GameConfig.getText("languageSelectorInitialValue"));
    languageSelector.setId("menuSelector");
    languageSelector.getItems().addAll(GameConfig.getTextList("languageSelector"));
    languageSelector.setOnAction(e -> {
      GameConfig.setLanguage(languageSelector.getValue());});
    return languageSelector;
  }

  private ComboBox<String> setupThemeSelector() {
    ComboBox<String> themeSelector = new ComboBox<>();
    themeSelector.setValue(GameConfig.getText("themeSelectorInitialValue"));
    themeSelector.setId("menuSelector");
    themeSelector.getItems().addAll(GameConfig.getTextList("themeSelector"));
    themeSelector.setOnAction(e -> {
      StyleConfig.setStylesheet(getScene(), themeSelector.getValue());
    });
    return themeSelector;
  }
}