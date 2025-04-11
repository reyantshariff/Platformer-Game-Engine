package oogasalad.view.scene;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.input.KeyCode;
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

  public static final double ZOOM_FACTOR = 1.05;
  public static final double MAX_ZOOM = 2.0;
  public static final double MIN_ZOOM = 0.5;

  private static final Logger logger = LogManager.getLogger(BuilderScene.class);

  private final BorderPane myWindow;
  private Canvas myGameCanvas;
  private ScrollPane levelViewScrollPane; // ScrollPane containing the game Canvas

  private GameScene gameScene;

  private Builder builder;
  private final MainViewManager viewManager;

  private final Map<KeyCode, Consumer<Double>> scrollPaneEventMap = Map.of(
    KeyCode.LEFT, (delta) -> scrollPaneKeyPress(-delta, 0),
    KeyCode.RIGHT, (delta) -> scrollPaneKeyPress(delta, 1),
    KeyCode.UP, (delta) -> scrollPaneKeyPress(-delta, 0),
    KeyCode.DOWN, (delta) -> scrollPaneKeyPress(delta, 1)
);

  /**
   * Constructor for BuilderView
   *
   * @param manager the view manager which this scene will use to navigate to other screens
   */
  public BuilderScene(MainViewManager manager) {
    // Create the BorderPane as the root
    super(new BorderPane(), 1280, 720);
    viewManager = manager;
    myWindow = (BorderPane) getScene().getRoot();
    createDinoGameTest();
    initializeUI();
  }

  private void createDinoGameTest() {
    gameScene = new DinosaurGameScene("LevelEditTest");
    gameScene.onActivated();
    builder = new Builder(gameScene);
  }

  private void initializeUI() {
    // Top bar
    HBox topBar = new HBox();
    Button mainMenuButton = new Button("Main Menu");
    mainMenuButton.setOnAction(e -> viewManager.switchToMainMenu());
    Button previewLevelButton = new Button("Preview Level");
    previewLevelButton.setOnAction(e -> {
      JsonNode gameNode = builder.saveGameAs("test.json");
      viewManager.switchTo(
          new LevelPreviewScene(viewManager, this, gameNode));

    });
    topBar.getChildren().addAll(mainMenuButton, previewLevelButton);
    myWindow.setTop(topBar);

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
    getObjectRenderer().renderWithoutCamera(gc, gameScene);
  }

  private ScrollPane createGamePreview() {
    // TODO: replace canvas size hardcoding with level map size
    myGameCanvas = new Canvas(3000, 600);
    updateGamePreview(); // render GameObjects to canvas

    Group canvasGroup = new Group(myGameCanvas);

    levelViewScrollPane = new ScrollPane(canvasGroup);

    initializeScrollPane(canvasGroup);

    return levelViewScrollPane;
  }

  private void initializeScrollPane(Group canvasGroup) {
    // Canvas viewport size within builder window
    levelViewScrollPane.setPrefViewportWidth(
        GAME_PREVIEW_WIDTH); // TODO: replace these hardcoded values
    levelViewScrollPane.setPrefViewportHeight(GAME_PREVIEW_HEIGHT);

    // Disable vertical scroll bar
    levelViewScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

    levelViewScrollPane.setFocusTraversable(true);
    levelViewScrollPane.requestFocus();

    // Pan the view using arrow keys
    levelViewScrollPane.setOnKeyPressed(event -> {
      double delta = 0.05;
      try {
        scrollPaneEventMap.get(event.getCode()).accept(delta);
      } catch (NullPointerException e) {
        logger.error("Key event not mapped: " + event.getCode());
      }
      event.consume();
    });

    ObjectDragger dragger = new ObjectDragger(myGameCanvas, builder, this, gameScene,
        getObjectRenderer());

    // Add zoom handling
    levelViewScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
      // Only process the event for zooming, ignore its vertical scrolling aspect.
      double currentScale = getCurrentScale(canvasGroup, event.getDeltaY());
      canvasGroup.setScaleX(currentScale);
      canvasGroup.setScaleY(currentScale);

      // Ensure the vertical scroll value remains fixed
      levelViewScrollPane.setVvalue(0);

      event.consume();  // Prevent the default vertical panning
    });
  }

  private void scrollPaneKeyPress(double delta, int b) {
    levelViewScrollPane.setHvalue(Math.max(levelViewScrollPane.getHvalue() + delta, b));
  }

  private double getCurrentScale(Group canvasGroup, double deltaY) {
    double currentScale = canvasGroup.getScaleX(); // assuming uniform scale
    if (deltaY < 0) {
      if (currentScale / ZOOM_FACTOR >= MIN_ZOOM) {
        currentScale /= ZOOM_FACTOR;
      }
    } else {
      if (currentScale * ZOOM_FACTOR <= MAX_ZOOM) {
        currentScale *= ZOOM_FACTOR;
      }
    }
    return currentScale;
  }

  private HBox createBottomPanel() {
    HBox bottomPanel = new HBox();
    bottomPanel.setSpacing(10);
    bottomPanel.getChildren().add(createAddSpriteButtonOptions());
    return bottomPanel;
  }

  private ScrollPane createAddSpriteButtonOptions() {
    // Define width and height of the sprite button panel
    double spriteButtonPaneWidth = getScene().getWidth() * 0.45;
    double spriteButtonPaneHeight = getScene().getHeight() * 0.25;

    // Organize the button layout via TilePane
    TilePane tilePane = createSpriteButtonTilePane(spriteButtonPaneWidth);

    // Create a ScrollPane to hold the buttons
    ScrollPane spriteScrollPane = createSpriteButtonScrollPane(spriteButtonPaneWidth,
        spriteButtonPaneHeight, tilePane);

    // Load the prefab GameObjects
    List<GameObject> prefabObjects = PrefabLoader.loadAvailablePrefabs(
        "dinosaur"); // TODO: remove hardcoded game type
    for (GameObject prefab : prefabObjects) {
     createObject(prefab, tilePane);
    }

    // TODO: TilePane and its buttons should immediately be correctly sized without requiring mouse movement
    tilePane.setOnMouseMoved(event -> {
      tilePane.setPrefWidth(spriteScrollPane.getPrefWidth());
    });

    return spriteScrollPane;
  }

  private void alignObject(Transform t) {
    // Calculate center based on preview or scene dimensions.
    double previewHorizontalMidpoint =
    levelViewScrollPane.getHvalue() * GAME_PREVIEW_WIDTH + (GAME_PREVIEW_WIDTH / 2);
    double previewVerticalMidpoint =
        levelViewScrollPane.getVvalue() * GAME_PREVIEW_HEIGHT + (GAME_PREVIEW_HEIGHT / 2);
    double objectWidth = t.getScaleX();
    double objectHeight = t.getScaleY();

    // Center the object
    t.setX(previewHorizontalMidpoint - (objectWidth / 2));
    t.setY(previewVerticalMidpoint - (objectHeight / 2));
  }

  private void createObject(GameObject prefab, TilePane tilePane) {
    // Assume the prefab has a SpriteRenderer component.
    String previewImagePath = getPreviewImagePath(prefab);
    // Convert the preview image path to a JavaFX Image
    try {
      Image previewImage = new Image(new File(previewImagePath).toURI().toURL().toString());
      Button newSpriteButton = new BuilderSpriteOptionButton(
          previewImage,
          tilePane.getPrefWidth() * 0.25,
          tilePane.getPrefHeight() * 0.25,
          prefab);
      newSpriteButton.setOnAction(event -> {
        GameObject newObject = prefab.clone();  // or a deep copy via serialization

        // Retrieve the Transform component
        Transform t = newObject.getComponent(Transform.class);
        try {
          alignObject(t);
        } catch (NullPointerException e) {
          logger.error("Error getting Transform component from prefab " + prefab.getName());
        }
        // Register new object to the scene
        gameScene.registerObject(newObject);
        // Update the game preview so the new object appears
        updateGamePreview();
      });
      tilePane.getChildren().add(newSpriteButton);
    } catch (Exception e) {
      logger.error("Error loading preview image from: " + previewImagePath);
    }
  }

  private ScrollPane createSpriteButtonScrollPane(double width, double height, Pane contents) {
    ScrollPane spriteScrollPane = new ScrollPane(contents);
    spriteScrollPane.setPrefWidth(width);
    spriteScrollPane.setPrefHeight(height);
    spriteScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER); // Disable horizontal scrolling
    spriteScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS); // Enable vertical scrolling
    spriteScrollPane.setFitToWidth(true);
    spriteScrollPane.setFitToHeight(true);
    return spriteScrollPane;
  }

  private TilePane createSpriteButtonTilePane(double width) {
    // Create a TilePane that will automatically arrange the sprite buttons in a tile/grid layout
    TilePane tilePane = new TilePane();
    tilePane.setPrefColumns(2); // Desired number of columns
    tilePane.setHgap(getScene().getWidth() * 0.02);
    tilePane.setVgap(getScene().getHeight() * 0.02);
    // Define TilePane size
    tilePane.setPrefWidth(width);
    tilePane.setMinWidth(width);
    tilePane.setMaxWidth(width);
    return tilePane;
  }

  private String getPreviewImagePath(GameObject prefab) {
    try {
      var spriteRenderer = prefab.getComponent(
          oogasalad.model.engine.component.SpriteRenderer.class);
      String imagePath = spriteRenderer.getImagePath();
      if (imagePath != null && !imagePath.isEmpty()) {
        return "src/main/resources/" + imagePath;
      }
    } catch (Exception e) {
      logger.error("Error getting preview image from prefab " + prefab.getName());
    }
    return null;
  }
}
