package oogasalad.view.scene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import oogasalad.model.builder.Builder;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.parser.PrefabLoader;
import oogasalad.view.gui.button.BuilderSpriteOptionButton;
import oogasalad.view.gui.dropDown.ClassSelectionDropDown;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import oogasalad.view.scene.BuilderUserControl.LevelViewScrollController;
import oogasalad.view.scene.BuilderUserControl.ObjectDragger;
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
  private final MainViewManager viewManager;

  private Canvas myGameCanvas;
  private ScrollPane levelViewScrollPane; // ScrollPane containing the game Canvas
  private LevelViewScrollController levelViewController;
  private GameScene gameScene;
  private Builder builder;

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
    gameScene.onActivated();
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

    // Add layout for Component panel
    myWindow.setRight(createComponentPanel());

    // Add level-view segment of window
    levelViewScrollPane = createGamePreview();
    myWindow.setCenter(levelViewScrollPane);
  }

  private VBox createComponentPanel() {
    String componentPackageName = "oogasalad.model.engine.component";
    return new VBox(new ClassSelectionDropDown(componentPackageName, GameComponent.class));
  }

  /**
   * Updates game preview window by rendering all current GameObjects in the GameScene in their
   * current position
   */
  public void updateGamePreview() {
    GraphicsContext gc = myGameCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, myGameCanvas.getWidth(), myGameCanvas.getHeight());
    myObjectRenderer.renderWithoutCamera(gc, builder.getCurrentScene());
  }

  private ScrollPane createGamePreview() {
    // TODO: replace canvas size hardcoding with level map size
    myGameCanvas = new Canvas(3000, 600);
    updateGamePreview(); // render GameObjects to canvas

    ObjectDragger dragger = new ObjectDragger(myGameCanvas, builder, this,
        myObjectRenderer);
    dragger.setupListeners();
    Group canvasGroup = new Group(myGameCanvas);
    levelViewController = new LevelViewScrollController(canvasGroup, GAME_PREVIEW_WIDTH, GAME_PREVIEW_HEIGHT);
    return levelViewController.scrollPane();
  }

  private HBox createBottomPanel() {
    HBox bottomPanel = new HBox();
    bottomPanel.setSpacing(10);

    Button undoButton = new Button("Undo");
    undoButton.setOnAction(event -> {
      builder.undoLastAction();
      updateGamePreview();
    });

    Button redoButton = new Button("Redo");
    redoButton.setOnAction(event -> {
      builder.redoLastAction();
      updateGamePreview();
    });

    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event -> {
      builder.deleteSelectedObject();
      updateGamePreview();
    });

    bottomPanel.getChildren().addAll(createAddSpriteButtonOptions(), undoButton, redoButton, deleteButton);

    return bottomPanel;
  }

  private ScrollPane createAddSpriteButtonOptions() {
    // Define width and height of the sprite button panel
    double spriteButtonPaneWidth = getScene().getWidth()*0.45;
    double spriteButtonPaneHeight = getScene().getHeight()*0.25;

    // Organize the button layout via TilePane
    TilePane tilePane = createSpriteButtonTilePane(spriteButtonPaneWidth);

    // Create a ScrollPane to hold the buttons
    ScrollPane spriteScrollPane = createSpriteButtonScrollPane(spriteButtonPaneWidth, spriteButtonPaneHeight, tilePane);

    // Load the prefab GameObjects
    List<GameObject> prefabObjects = PrefabLoader.loadAvailablePrefabs("dinosaur"); // TODO: remove hardcoded game type
    for (GameObject prefab : prefabObjects) {
      // Assume the prefab has a SpriteRenderer component.
      String previewImagePath = getPreviewImagePath(prefab);
      if (previewImagePath != null) {
        // Convert the preview image path to a JavaFX Image
        try {
          Image previewImage = new Image(new File(previewImagePath).toURI().toURL().toString());
          Button newSpriteButton = new BuilderSpriteOptionButton(
              previewImage,
              tilePane.getPrefWidth()*0.25,
              tilePane.getPrefHeight()*0.25,
              prefab);

          newSpriteButton.setOnAction(event -> {
            double previewHorizontalMidpoint = levelViewScrollPane.getHvalue() * GAME_PREVIEW_WIDTH + (GAME_PREVIEW_WIDTH / 2);
            double previewVerticalMidpoint = levelViewScrollPane.getVvalue() * GAME_PREVIEW_HEIGHT + (GAME_PREVIEW_HEIGHT / 2);
            builder.addObject(prefab.clone(), previewHorizontalMidpoint, previewVerticalMidpoint);
            updateGamePreview();
          });

          tilePane.getChildren().add(newSpriteButton);
        } catch (Exception e) {
          System.err.println("Error loading preview image from: " + previewImagePath);
        }
      }
    }

    // TODO: TilePane and its buttons should immediately be correctly sized without requiring mouse movement
    tilePane.setOnMouseMoved(event -> {
      tilePane.setPrefWidth(spriteScrollPane.getPrefWidth());
    });

    return spriteScrollPane;
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
    tilePane.setHgap(getScene().getWidth()*0.02);
    tilePane.setVgap(getScene().getHeight()*0.02);
    // Define TilePane size
    tilePane.setPrefWidth(width);
    tilePane.setMinWidth(width);
    tilePane.setMaxWidth(width);
    return tilePane;
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
