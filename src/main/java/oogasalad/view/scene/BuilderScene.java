package oogasalad.view.scene;

import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import oogasalad.model.builder.Builder;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.view.gui.TemporaryImageLoader;
import oogasalad.view.gui.button.BuilderSpriteOptionButton;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BuilderView is the main view for the level editor
 */

public class BuilderScene extends ViewScene {

  private static final Logger logger = LogManager.getLogger(BuilderScene.class);

  private final BorderPane myWindow;
  private Canvas myGameCanvas;

  private GameScene gameScene;

  private Builder builder;
  private MainViewManager viewManager;

  /**
   * Constructor for BuilderView
   * the height of the window
   */
  public BuilderScene(MainViewManager manager) {
    // Create the BorderPane as the root
    super(new BorderPane(), 1280, 720);
    myWindow = (BorderPane) getScene().getRoot();
    createDinoGameTest();
    initializeUI();
    viewManager = manager;
  }

  private void createDinoGameTest() {
    gameScene = new DinosaurGameScene("LevelEditTest");
    builder = new Builder(gameScene);
    gameScene.onActivated();
  }

  private void initializeUI() {
    // Top bar
    /**
    HBox topBar = new HBox();
    Button loadButton = new Button("Load");
    Button saveButton = new Button("Save");
    Button playtestButton = new Button("Playtest");
    topBar.getChildren().addAll(loadButton, saveButton, playtestButton);
    myWindow.setTop(topBar);
    **/
    Button returnButton = new Button("Main Menu");
    returnButton.setOnAction(e -> {
      viewManager.switchToMainMenu();
    });
    myWindow.setTop(returnButton);

    // Add layout for bottom button menus
    myWindow.setBottom(createBottomPanel());

    // Add level-view segment of window
    myWindow.setCenter(createGamePreview());

    // Right properties panel
    /**
    VBox propertiesPanel = new VBox();
    propertiesPanel.getChildren().add(new Label("Properties"));
    myWindow.setRight(propertiesPanel);
     **/

  }

  /**
   * Updates game preview window by rendering all current GameObjects in the GameScene in their
   * current position
   */
  public void updateGamePreview() {
    GraphicsContext gc = myGameCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, myGameCanvas.getWidth(), myGameCanvas.getHeight());
    myObjectRenderer.renderWithoutCamera(gc, gameScene);
  }

  private ScrollPane createGamePreview() {
    // TODO: replace canvas size hardcoding with level map size
    myGameCanvas = new Canvas(3000, 600);
    updateGamePreview(); // render GameObjects to canvas

    Group canvasGroup = new Group(myGameCanvas);

    ScrollPane previewScrollPane = new ScrollPane(canvasGroup);

    // Canvas viewport size within builder window
    previewScrollPane.setPrefViewportWidth(1000); // TODO: replace these hardcoded values
    previewScrollPane.setPrefViewportHeight(800);

    // Disable vertical scroll bar
    previewScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

    previewScrollPane.setFocusTraversable(true);
    previewScrollPane.requestFocus();

    // Pan the view using arrow keys
    previewScrollPane.setOnKeyPressed(event -> {
      double delta = 0.05;
      switch (event.getCode()) {
        case LEFT:
          previewScrollPane.setHvalue(Math.max(previewScrollPane.getHvalue() - delta, 0));
          break;
        case RIGHT:
          previewScrollPane.setHvalue(Math.min(previewScrollPane.getHvalue() + delta, 1));
          break;
        // Optionally, handle UP/DOWN if vertical panning is needed:
        case UP:
          previewScrollPane.setVvalue(Math.max(previewScrollPane.getVvalue() - delta, 0));
          break;
        case DOWN:
          previewScrollPane.setVvalue(Math.min(previewScrollPane.getVvalue() + delta, 1));
          break;
        default:
          break;
      }
      event.consume();
    });

    ObjectDragger dragger = new ObjectDragger(myGameCanvas, builder, this, gameScene,
        myObjectRenderer);

    // Add zoom handling
    previewScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
      // Only process the event for zooming, ignore its vertical scrolling aspect.
      double zoomFactor = 1.1;
      double currentScale = canvasGroup.getScaleX(); // assuming uniform scale
      if (event.getDeltaY() < 0) {
        currentScale /= zoomFactor;
      } else {
        currentScale *= zoomFactor;
      }
      canvasGroup.setScaleX(currentScale);
      canvasGroup.setScaleY(currentScale);

      // Ensure the vertical scroll value remains fixed
      previewScrollPane.setVvalue(0);

      event.consume();  // Prevent the default vertical panning
    });

    return previewScrollPane;
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
    tilePane.setHgap(getScene().getWidth()*0.02);
    tilePane.setVgap(getScene().getHeight()*0.02);

    String imageDirectory = "src/main/resources/oogasalad/dinosaur/";
    List<Image> images = TemporaryImageLoader.loadImages(imageDirectory);
    // Create sprite buttons
    for (Image image : images) {
      tilePane.getChildren().add(new BuilderSpriteOptionButton(image, getScene().getWidth()*0.12, getScene().getHeight()*0.12));
    }

    // Wrap the TilePane in a ScrollPane.
    ScrollPane spriteScrollPane = new ScrollPane(tilePane);
    spriteScrollPane.setPrefHeight(getScene().getHeight()*0.25);
    spriteScrollPane.setPrefWidth(getScene().getWidth()*0.45);
    spriteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrolling
    spriteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Enable vertical scrolling

    return spriteScrollPane;
  }
}
