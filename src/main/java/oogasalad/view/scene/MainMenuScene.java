package oogasalad.view.scene;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import oogasalad.controller.GameController;
import oogasalad.model.config.GameConfig;
import oogasalad.model.profile.SessionException;
import oogasalad.model.profile.SessionManagement;
import oogasalad.view.config.StyleConfig;

/**
 * Main menu view with play and builder options
 */
public class MainMenuScene extends ViewScene {
  private GameController gameController;
  /**
   * Constructs a new MainMenuScene to display the main menu with game selection and builder options.
   *
   * @param manager The MainViewManager used to switch scenes.
   */
  public MainMenuScene(MainViewManager manager) throws SessionException {
    super(new VBox(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    gameController = new GameController(manager);
    VBox root = (VBox) getScene().getRoot();
    root.setAlignment(Pos.CENTER);

    Label title = new Label(GameConfig.getText("mainMenuTitle"));
    title.setId("mainMenuTitle");

    Label username = new Label(GameConfig.getText("mainMenuUsername", SessionManagement.getCurrentUser().getUsername()));

    ComboBox<String> gameSelector = setupGameSelector();

    HBox buttonBox = setupButtonBox();
    Button playButton = (Button) buttonBox.lookup("#playButton");
    Button buildButton = (Button) buttonBox.lookup("#buildButton");

    handlePlayButton(manager, playButton, gameSelector);

    buildButton.setOnAction(e -> manager.switchTo("BuilderScene")
    );
    // Language and Theme Selections
    HBox selectorBox = setupSelectorBox();
    HBox logOutBox = setUpLogOutBox();

    root.getChildren().addAll(title, username, gameSelector, buttonBox, selectorBox, logOutBox);
  }

  private void handlePlayButton(MainViewManager manager, Button playButton,
      ComboBox<String> gameSelector) {
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

  private HBox setUpLogOutBox() {
    HBox logOutBox = new HBox();
    logOutBox.setAlignment(Pos.CENTER);
    logOutBox.setSpacing(10);
    logOutBox.setPadding(new Insets(20, 0, 0, 0));


    Button logOutButton = new Button(GameConfig.getText("logOutButton"));
    logOutButton.setOnMouseClicked((event) -> {
      gameController.handleLogout();
    });
    logOutButton.setId("logOutButton");
    logOutBox.getChildren().add(logOutButton);
    logOutBox.setId("logOutBox");

    return logOutBox;
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