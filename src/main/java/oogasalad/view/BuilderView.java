package oogasalad.view;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.gui.button.StylizedButton;
import oogasalad.player.dinosaur.DinosaurGameScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuilderView extends ViewScene {

  private static final Logger logger = LogManager.getLogger(BuilderView.class);

  private final BorderPane myWindow;

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
    StylizedButton loadButton = new StylizedButton("Load");
    StylizedButton saveButton = new StylizedButton("Save");
    StylizedButton playtestButton = new StylizedButton("Playtest");
    topBar.getChildren().addAll(loadButton.getButton(), saveButton.getButton(), playtestButton.getButton());
    myWindow.setTop(topBar);

    // Add layout for bottom button menus
    myWindow.setBottom(createBottomPanel());

    // Add level-view segment of window
    myWindow.setCenter(createGamePreview());

    // Right properties panel
    VBox propertiesPanel = new VBox();
    propertiesPanel.getChildren().add(new Label("Properties"));
    myWindow.setRight(propertiesPanel);
  }

  private ScrollPane createGamePreview() {
    // Create and set up the canvas and zoomable group:
    Canvas canvas = new Canvas(1000, 600);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    renderInitialFrame(gc, gameScene);

    // Wrap canvas in Group for scaling
    Group zoomableGroup = new Group(canvas);
    // Use a ScrollPane for handling zoom transformation
    ScrollPane scrollPane = new ScrollPane(zoomableGroup);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    // Disable scroll bars
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    // Add zoom handling
    scrollPane.setOnScroll(event -> {
      double zoomFactor = 1.1;
      double currentScale = zoomableGroup.getScaleX(); // uniform scale assumed
      if (event.getDeltaY() < 0) {
        currentScale /= zoomFactor;
      } else {
        currentScale *= zoomFactor;
      }
      zoomableGroup.setScaleX(currentScale);
      zoomableGroup.setScaleY(currentScale);
      event.consume();
    });

    return scrollPane;
  }

  private HBox createBottomPanel() {
    HBox bottomPanel = new HBox();
    bottomPanel.setSpacing(10);
    bottomPanel.getChildren().add(createSpriteButtonOptions());
    return bottomPanel;
  }

  // TODO: make the buttons non-hardcoded
  private ScrollPane createSpriteButtonOptions() {
    // Create a TilePane that will automatically arrange children in 2 columns.
    TilePane tilePane = new TilePane();
    tilePane.setPrefColumns(2); // set desired number of columns
    tilePane.setHgap(10);
    tilePane.setVgap(10);

    // Create sprite buttons.
    StylizedButton buttonSpriteA = new StylizedButton("Sprite A");
    StylizedButton buttonSpriteB = new StylizedButton("Sprite B");
    StylizedButton buttonSpriteC = new StylizedButton("Sprite C");
    StylizedButton buttonSpriteD = new StylizedButton("Sprite D");

    // Add buttons to the TilePane.
    tilePane.getChildren().addAll(
        buttonSpriteA.getButton(),
        buttonSpriteB.getButton(),
        buttonSpriteC.getButton(),
        buttonSpriteD.getButton()
    );

    // Wrap the TilePane in a ScrollPane.
    ScrollPane spriteScrollPane = new ScrollPane(tilePane);
    spriteScrollPane.setPrefHeight(150);
    spriteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    spriteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    return spriteScrollPane;
  }

  private void renderInitialFrame(GraphicsContext gc, GameScene gameScene) {
    myObjectRenderer.render(gc, gameScene);
  }
}
