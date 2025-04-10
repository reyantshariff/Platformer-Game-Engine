package oogasalad.view.scene;

import java.io.File;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import oogasalad.model.builder.Builder;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.parser.PrefabLoader;
import oogasalad.view.gui.button.BuilderSpriteOptionButton;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BuilderView is the main view for the level editor
 */

public class BuilderScene extends ViewScene {

  // Static constants defining the size of the level-preview window within the builder scene
  public static final double GAME_PREVIEW_WIDTH = 1000;
  public static final double GAME_PREVIEW_HEIGHT = 800;

  private static final Logger logger = LogManager.getLogger(BuilderScene.class);

  private final BorderPane myWindow;
  private Canvas myGameCanvas;
  private ScrollPane levelViewScrollPane; // ScrollPane containing the game Canvas

  private GameScene gameScene;

  private Builder builder;
  private final MainViewManager viewManager;

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
  }

  private void initializeUI() {
    // Top bar
    /*
    HBox topBar = new HBox();
    Button loadButton = new Button("Load");
    Button saveButton = new Button("Save");
    Button playtestButton = new Button("Playtest");
    topBar.getChildren().addAll(loadButton, saveButton, playtestButton);
    myWindow.setTop(topBar);
    */
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
    /*
    VBox propertiesPanel = new VBox();
    propertiesPanel.getChildren().add(new Label("Properties"));
    myWindow.setRight(propertiesPanel);
    */

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

    levelViewScrollPane = new ScrollPane(canvasGroup);

    // Canvas viewport size within builder window
    levelViewScrollPane.setPrefViewportWidth(GAME_PREVIEW_WIDTH); // TODO: replace these hardcoded values
    levelViewScrollPane.setPrefViewportHeight(GAME_PREVIEW_HEIGHT);

    // Disable vertical scroll bar
    levelViewScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

    levelViewScrollPane.setFocusTraversable(true);
    levelViewScrollPane.requestFocus();

    // Pan the view using arrow keys
    levelViewScrollPane.setOnKeyPressed(event -> {
      double delta = 0.05;
      switch (event.getCode()) {
        case LEFT:
          levelViewScrollPane.setHvalue(Math.max(levelViewScrollPane.getHvalue() - delta, 0));
          break;
        case RIGHT:
          levelViewScrollPane.setHvalue(Math.min(levelViewScrollPane.getHvalue() + delta, 1));
          break;
        // Optionally, handle UP/DOWN if vertical panning is needed:
        case UP:
          levelViewScrollPane.setVvalue(Math.max(levelViewScrollPane.getVvalue() - delta, 0));
          break;
        case DOWN:
          levelViewScrollPane.setVvalue(Math.min(levelViewScrollPane.getVvalue() + delta, 1));
          break;
        default:
          break;
      }
      event.consume();
    });

    ObjectDragger dragger = new ObjectDragger(myGameCanvas, builder, this, gameScene,
        myObjectRenderer);

    // Add zoom handling
    levelViewScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
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
      levelViewScrollPane.setVvalue(0);

      event.consume();  // Prevent the default vertical panning
    });

    return levelViewScrollPane;
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

    // Load the prefab GameObjects
    List<GameObject> prefabObjects = PrefabLoader.loadAvailablePrefabs("dinosaur"); // TODO: remove hardcoded game type
    for (GameObject prefab : prefabObjects) {
      // Assume the prefab has a SpriteRenderer component.
      // You can add a method in GameObject or via a helper to retrieve the preview image path.
      String previewImagePath = getPreviewImagePath(prefab);
      if (previewImagePath != null) {
        // Convert the preview image path to a JavaFX Image
        try {
          Image previewImage = new Image(new File(previewImagePath).toURI().toURL().toString());
          Button newSpriteButton = new BuilderSpriteOptionButton(
              previewImage,
              getScene().getWidth()*0.12,
              getScene().getHeight()*0.12,
              prefab);
          newSpriteButton.setOnAction(event -> {
            GameObject newObject = prefab.clone();  // or a deep copy via serialization

            // Retrieve the Transform component
            Transform t = newObject.getComponent(Transform.class);
            if (t != null) {
              // Calculate center based on preview or scene dimensions.
              double previewHorizontalMidpoint = levelViewScrollPane.getHvalue()*GAME_PREVIEW_WIDTH + (GAME_PREVIEW_WIDTH / 2);
              double previewVerticalMidpoint = levelViewScrollPane.getVvalue()*GAME_PREVIEW_HEIGHT + (GAME_PREVIEW_HEIGHT / 2);
              double objectWidth = t.getScaleX();
              double objectHeight = t.getScaleY();

              // Center the object
              t.setX(previewHorizontalMidpoint - (objectWidth / 2));
              t.setY(previewVerticalMidpoint - (objectHeight / 2));
            }
            // Register new object to the scene
            gameScene.registerObject(newObject);
            // Update the game preview so the new object appears
            updateGamePreview();
          });
          tilePane.getChildren().add(newSpriteButton);
        } catch (Exception e) {
          System.err.println("Error loading preview image from: " + previewImagePath);
        }
      }
    }

    // Wrap the TilePane in a ScrollPane.
    ScrollPane spriteScrollPane = new ScrollPane(tilePane);
    spriteScrollPane.setPrefHeight(getScene().getHeight()*0.25);
    spriteScrollPane.setPrefWidth(getScene().getWidth()*0.45);
    spriteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrolling
    spriteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Enable vertical scrolling

    return spriteScrollPane;
  }

  private String getPreviewImagePath(GameObject prefab) {
    try {
      var spriteRenderer = prefab.getComponent(oogasalad.model.engine.component.SpriteRenderer.class);
      String imagePath = spriteRenderer.getImagePath();
      if (imagePath != null && !imagePath.isEmpty()) {
        return "src/main/resources/" + imagePath;
      }
    } catch (Exception e) {
      System.err.println("Error getting preview image from prefab " + prefab.getName());
    }
    return null;
  }
}
