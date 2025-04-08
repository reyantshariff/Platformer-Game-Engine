package oogasalad.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.gui.button.StylizedButton;
import oogasalad.player.dinosaur.DinosaurGameScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuilderView extends ViewScene {

  private static final Logger logger = LogManager.getLogger(BuilderView.class);

  private final BorderPane myWindow;

  private StylizedButton loadButton;
  private StylizedButton saveButton;
  private StylizedButton playtestButton;

  private Canvas canvas;

  private Pane editorCanvas;
  private VBox toolbox;
  private VBox propertiesPanel;

  private GameScene gameScene;

  public BuilderView(double width, double height) {
    // Create the BorderPane as the root
    super(new BorderPane(), width, height);
    myWindow = (BorderPane) super.myScene.getRoot();
    createDinoGameTest();
    initializeUI();
  }

  private void createDinoGameTest() {
    gameScene = new DinosaurGameScene("LevelEditTest");
    gameScene.onActivated();
  }

  private void initializeUI() {
    // Top bar
    HBox topBar = new HBox();
    loadButton = new StylizedButton("Load");
    saveButton = new StylizedButton("Save");
    playtestButton = new StylizedButton("Playtest");
    topBar.getChildren().addAll(loadButton.getButton(), saveButton.getButton(), playtestButton.getButton());
    myWindow.setTop(topBar);

    // Left toolbox
    toolbox = new VBox();
    toolbox.getChildren().add(new Label("Blocks"));
    // Add more UI controls for placing blocks
    myWindow.setLeft(toolbox);

    // Center canvas
    canvas = new Canvas(1200, 800);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    renderInitialFrame(gc, gameScene);
    editorCanvas = new Pane();
    editorCanvas.getChildren().add(canvas);
    myWindow.setCenter(editorCanvas);

    // Right properties panel
    propertiesPanel = new VBox();
    propertiesPanel.getChildren().add(new Label("Properties"));
    myWindow.setRight(propertiesPanel);

    // Event handlers
//    loadButton.setOnAction(e -> handleLoad());
//    saveButton.setOnAction(e -> handleSave());
//    playtestButton.setOnAction(e -> handlePlaytest());
  }

  private void renderInitialFrame(GraphicsContext gc, GameScene gameScene) {
    myObjectRenderer.render(gc, gameScene);
  }

  private void handleLoad() {
    // Load level data, create ECS objects in editorScene
    // ...
  }

  private void handleSave() {
    // Save ECS objects to a file or database
    // ...
  }

  private void handlePlaytest() {
    // Switch to the game scene or start the game using the ECS data
    // ...
  }
}
