package oogasalad.view.screens;

import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.view.gui.GameObjectRenderer;
import oogasalad.view.gui.TemporaryImageLoader;
import oogasalad.view.gui.button.BuilderSpriteOptionButton;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import oogasalad.view.scene.ViewScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BuilderView is the main view for the level editor
 */

public class BuilderView extends Scene {

  private static final Logger logger = LogManager.getLogger(BuilderView.class);

  private final BorderPane myWindow;

  private GameScene gameScene;

  private final GameObjectRenderer gameObjectRenderer;

  /**
   * Constructor for BuilderView
   *
   * @param width  the width of the window
   * @param height the height of the window
   */
  public BuilderView(double width, double height, GameObjectRenderer gameObjectRenderer) {
    // Create the BorderPane as the root
    super(new BorderPane(), width, height);
    this.gameObjectRenderer = gameObjectRenderer;
    myWindow = (BorderPane) getRoot();
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
    Button loadButton = new Button("Load");
    Button saveButton = new Button("Save");
    Button playtestButton = new Button("Playtest");
    topBar.getChildren().addAll(loadButton, saveButton, playtestButton);
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

  private ScrollPane createSpriteButtonOptions() {
    // Create a TilePane that will automatically arrange children in 2 columns.
    TilePane tilePane = new TilePane();
    tilePane.setPrefColumns(3); // set desired number of columns
    tilePane.setHgap(this.getWidth()*0.02);
    tilePane.setVgap(this.getHeight()*0.02);

    String imageDirectory = "src/main/resources/oogasalad/dinosaur/";
    List<Image> images = TemporaryImageLoader.loadImages(imageDirectory);
    // Create sprite buttons
    for (Image image : images) {
      tilePane.getChildren().add(new BuilderSpriteOptionButton(image, this.getWidth()*0.12, this.getHeight()*0.12));
    }

    // Wrap the TilePane in a ScrollPane.
    ScrollPane spriteScrollPane = new ScrollPane(tilePane);
    spriteScrollPane.setPrefHeight(this.getHeight()*0.25);
    spriteScrollPane.setPrefWidth(this.getWidth()*0.45);
    spriteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrolling
    spriteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Enable vertical scrolling

    return spriteScrollPane;
  }

  private void renderInitialFrame(GraphicsContext gc, GameScene gameScene) {
    gameObjectRenderer.render(gc, gameScene);
  }
}
