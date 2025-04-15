package oogasalad.view.scene.menu;

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
import oogasalad.view.config.StyleConfig;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;
import oogasalad.view.scene.builder.BuilderScene;
import oogasalad.view.scene.display.GameDisplayScene;

/**
 * Main menu view with play and builder options
 */
@SuppressWarnings("unused")
public class MainMenuScene extends ViewScene {

  private static final String GAME_PLAYER_SCENE_NAME = "GamePlayer";
  private static final String BUILDER_SCENE_NAME = "Builder";

  private String selectedFilePath = "";
  private final GameController gameController;
  /**
   * Constructs a new MainMenuScene to display the main menu with game selection and builder options.
   */
  private MainMenuScene() {
    super(new VBox(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    gameController = new GameController(MainViewManager.getInstance());
    VBox root = (VBox) getScene().getRoot();
    root.setAlignment(Pos.CENTER);

    Label title = new Label(GameConfig.getText("mainMenuTitle"));
    title.getStyleClass().add("title");

    Button gameSelector = setupGameSelector();

    HBox buttonBox = setupButtonBox();
    Button playButton = (Button) buttonBox.lookup("#playButton");
    Button buildButton = (Button) buttonBox.lookup("#buildButton");

    GameDisplayScene playScene = MainViewManager.getInstance().addViewScene(GameDisplayScene.class, GAME_PLAYER_SCENE_NAME);
    playScene.setReturnSceneName(GameConfig.getText("defaultScene"));
    playButton.setOnAction(e -> {
      ((GameDisplayScene) MainViewManager.getInstance().getViewScene(GAME_PLAYER_SCENE_NAME)).play(selectedFilePath);
      MainViewManager.getInstance().switchTo(GAME_PLAYER_SCENE_NAME);
    });

    MainViewManager.getInstance().addViewScene(BuilderScene.class, BUILDER_SCENE_NAME);
    buildButton.setOnAction(e -> {
      ((BuilderScene) MainViewManager.getInstance().getViewScene(BUILDER_SCENE_NAME)).setUpBuilder(selectedFilePath);
      MainViewManager.getInstance().switchTo(BUILDER_SCENE_NAME);
    });

    // Language and Theme Selections
    HBox selectorBox = setupSelectorBox();
    HBox logOutBox = setUpLogOutBox();

    // Add to root
    root.getChildren().addAll(title, gameSelector, buttonBox, selectorBox, logOutBox);

    // Set default styles
    StyleConfig.setStylesheet(getScene(), "");
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

  private Button setupGameSelector() {
    Button gameSelector = new Button(GameConfig.getText("gameSelectorInitialValue"));
    gameSelector.setId("gameSelector");
    gameSelector.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select a Game JSON File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
      fileChooser.setInitialDirectory(new File("data/GameJsons"));

      File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
      if (selectedFile != null) {
        selectedFilePath = selectedFile.getAbsolutePath();
      }
    });
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

}

