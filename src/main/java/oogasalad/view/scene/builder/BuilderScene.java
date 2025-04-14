package oogasalad.view.scene.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import oogasalad.model.builder.Builder;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.model.parser.PrefabLoader;
import oogasalad.view.gui.button.BuilderSpriteOptionButton;
import oogasalad.view.gui.dropDown.ClassSelectionDropDownMenu;
import oogasalad.view.gui.panel.ComponentPanel;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;
import oogasalad.view.scene.builder.builderControl.ObjectDragger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * BuilderView is the main view for the level editor
 */

public class BuilderScene extends ViewScene {

  // Static constants defining the size of the level-preview window within the builder scene
  public static final double GAME_PREVIEW_WIDTH = 1000;
  public static final double GAME_PREVIEW_HEIGHT = 800;

  private static final String COMPONENT_PACKAGE_NAME = "oogasalad.model.engine.component";

  public static final double MAX_ZOOM = 5.0;
  public static final double MIN_ZOOM = 0.1;

  private static final Logger logger = LogManager.getLogger(BuilderScene.class);

  private final BorderPane myWindow;
  private final MainViewManager viewManager;

  private Canvas myGameCanvas;
  private VBox myComponentContainer;
  private Builder builder;

  /**
   * Constructor for BuilderView
   *
   * @param manager the view manager which this scene will use to navigate to other screens
   */
  public BuilderScene(MainViewManager manager, String gameFilepath) {
    // Create the BorderPane as the root
    super(new BorderPane(), 1280, 720);
    viewManager = manager;
    myWindow = (BorderPane) getScene().getRoot();
    builder = new Builder(gameFilepath);
    initializeUI();
  }

  private void initializeUI() {
    // Top bar
    HBox topBar = new HBox();
    Button mainMenuButton = new Button("Main Menu");
    mainMenuButton.setOnAction(e -> viewManager.switchToMainMenu());
    Button previewLevelButton = new Button("Preview Level");
    previewLevelButton.setOnAction(e -> {
      JsonNode gameNode = builder.saveGameAs("temp.json");
      viewManager.switchTo(
          new LevelPreviewScene(viewManager, this, gameNode));

    });
    topBar.getChildren().addAll(mainMenuButton, previewLevelButton);
    myWindow.setTop(topBar);

    // Add SplitPane for sprite button options and object control panel
    SplitPane objectControlSplitPane = new SplitPane(createAddSpriteButtonOptions(), createObjectControlPanel());
    objectControlSplitPane.setOrientation(Orientation.HORIZONTAL);
    objectControlSplitPane.setDividerPositions(0.7);

    // Add SplitPane for splitting the view and object panel
    SplitPane viewObjectSplitPane = new SplitPane(createGamePreview(), objectControlSplitPane);
    viewObjectSplitPane.setOrientation(Orientation.VERTICAL);
    viewObjectSplitPane.setDividerPositions(0.6);

    // Add SplitPane for splitting the view and component panel
    SplitPane viewComponentSplitPane = new SplitPane(viewObjectSplitPane, createComponentPanel());
    viewComponentSplitPane.setOrientation(Orientation.HORIZONTAL);
    viewComponentSplitPane.setDividerPositions(0.7);

    // Add split pane to the center of the window
    myWindow.setCenter(viewComponentSplitPane);
  }

  @SuppressWarnings("unchecked")
  private ScrollPane createComponentPanel() {
    myComponentContainer = new VBox(10);
    ClassSelectionDropDownMenu addComponentMenuButton = new ClassSelectionDropDownMenu("Add A Component", COMPONENT_PACKAGE_NAME, GameComponent.class);
    addComponentMenuButton.setOnClassSelected(className -> {
      try {
        Class<? extends GameComponent> componentClass = (Class<? extends GameComponent>) Class.forName(COMPONENT_PACKAGE_NAME + "." + className);
        GameObject selectedObject = builder.getSelectedObject();

        if (selectedObject != null && !selectedObject.hasComponent(componentClass)) {
          selectedObject.addComponent(componentClass);
          componentSelectionUpdate();
        }

      } catch (ClassNotFoundException e) {
        logger.error("Class not found: {}", e.getMessage());
      }
    });

    VBox container = new VBox(10, myComponentContainer, addComponentMenuButton);
    container.setPadding(new Insets(10, 10, 10, 10));
    container.setAlignment(Pos.TOP_CENTER);

    ScrollPane scrollPane = new ScrollPane(container);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.setFitToWidth(true);

    return scrollPane;
  }

  /**
   * Updates game preview window by rendering all current GameObjects in the GameScene in their
   * current position
   */
  public void updateGamePreview() {
    GraphicsContext gc = myGameCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, myGameCanvas.getWidth(), myGameCanvas.getHeight());
    getObjectRenderer().renderWithoutCamera(gc, builder.getCurrentScene(), builder.getSelectedObject());
  }

  /**
   * Handles the object selection change
   */
  public void handleObjectSelectionChange() {
    componentSelectionUpdate();
  }

  private void componentSelectionUpdate() {
    myComponentContainer.getChildren().clear();

    GameObject selectedObject = builder.getSelectedObject();
    if (selectedObject == null) {
      return;
    }

    List<GameComponent> components = new ArrayList<>(selectedObject.getAllComponents().values());
    components.sort(Comparator.comparing(c -> c.componentTag().ordinal()));
    for (GameComponent component : components) {
      myComponentContainer.getChildren().add(new ComponentPanel(component));
    }
  }

  private Pane createGamePreview() {
    // TODO: replace hardcoded canvas size with level map size
    myGameCanvas = new Canvas(3000, 600);
    updateGamePreview();

    Group canvasGroup = new Group(myGameCanvas);
    Pane container = new Pane(canvasGroup);

    // Scene dragging and zooming
    setupZoomAndPan(container, canvasGroup);

    // Object dragging and resizing
    ObjectDragger objectDragger = new ObjectDragger(myGameCanvas, builder, this, getObjectRenderer());
    objectDragger.setupListeners();

    return container;
  }

  private void setupZoomAndPan(Pane container, Group canvasGroup) {
    final ObjectProperty<Point2D> lastMousePosition = new SimpleObjectProperty<>();

    // handle mouse pressed and dragged events
    container.setOnMousePressed(event -> {
      if (builder.objectIsSelected()) return;
      lastMousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
    });
    container.setOnMouseDragged(event -> {
      if (builder.objectIsSelected()) return;

      Point2D currentMousePosition = new Point2D(event.getSceneX(), event.getSceneY());
      Point2D delta = currentMousePosition.subtract(lastMousePosition.get());

      canvasGroup.setTranslateX(canvasGroup.getTranslateX() + delta.getX());
      canvasGroup.setTranslateY(canvasGroup.getTranslateY() + delta.getY());

      lastMousePosition.set(currentMousePosition);
    });

    // handle zooming
    container.setOnScroll(event -> {
      double zoomFactor = 1.05;
      double deltaY = event.getDeltaY();
      double oldScale = canvasGroup.getScaleX();
      double scale = (deltaY > 0) ? oldScale * zoomFactor : oldScale / zoomFactor;
      scale = Math.min(Math.max(scale, MIN_ZOOM), MAX_ZOOM);

      Point2D mousePoint = new Point2D(event.getX(), event.getY());
      Point2D mouseInGroup = canvasGroup.parentToLocal(mousePoint);

      canvasGroup.setScaleX(scale);
      canvasGroup.setScaleY(scale);

      Point2D newMouseInGroup = canvasGroup.parentToLocal(mousePoint);

      Point2D delta = newMouseInGroup.subtract(mouseInGroup);
      canvasGroup.setTranslateX(canvasGroup.getTranslateX() + delta.getX() * scale);
      canvasGroup.setTranslateY(canvasGroup.getTranslateY() + delta.getY() * scale);

      event.consume();
    });

    container.setOnZoom(event -> {
      double oldScale = canvasGroup.getScaleX();
      double scale = oldScale * event.getZoomFactor();
      scale = Math.min(Math.max(scale, MIN_ZOOM), MAX_ZOOM);

      Point2D zoomCenter = new Point2D(event.getX(), event.getY());
      Point2D zoomCenterInGroup = canvasGroup.parentToLocal(zoomCenter);

      canvasGroup.setScaleX(scale);
      canvasGroup.setScaleY(scale);

      Point2D newZoomCenterInGroup = canvasGroup.parentToLocal(zoomCenter);
      Point2D delta = newZoomCenterInGroup.subtract(zoomCenterInGroup);
      canvasGroup.setTranslateX(canvasGroup.getTranslateX() + delta.getX() * scale);
      canvasGroup.setTranslateY(canvasGroup.getTranslateY() + delta.getY() * scale);

      event.consume();
    });
  }


  private VBox createObjectControlPanel() {
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

    HBox buttonBox = new HBox(undoButton, redoButton, deleteButton);
    buttonBox.setSpacing(10);
    buttonBox.setAlignment(Pos.CENTER);
    VBox bottomPanel = new VBox(buttonBox);
    bottomPanel.setAlignment(Pos.CENTER);
    bottomPanel.setSpacing(10);

    return bottomPanel;
  }

  private ScrollPane createAddSpriteButtonOptions() {
    // Define width and height of the sprite button panel
    double spriteButtonPaneWidth = getScene().getWidth() * 0.75;
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
    // TODO: FIX THIS
    double previewHorizontalMidpoint = GAME_PREVIEW_WIDTH / 2;
    double previewVerticalMidpoint = GAME_PREVIEW_HEIGHT / 2;
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
        if (t == null) {
          logger.error("Error getting Transform component from prefab " + prefab.getName());
        } else {
          alignObject(t);
        }

        // Register new object to the scene
        builder.getCurrentScene().registerObject(newObject);
        // Update the game preview so the new object appears
        updateGamePreview();
      });
      tilePane.getChildren().add(newSpriteButton);
    } catch (IllegalArgumentException | MalformedURLException e) {
      logger.error("Error loading preview image from: " + previewImagePath);
    }
  }

  private ScrollPane createSpriteButtonScrollPane(double width, double height, Pane contents) {
    ScrollPane spriteScrollPane = new ScrollPane(contents);
    spriteScrollPane.setPrefWidth(width);
    spriteScrollPane.setPrefHeight(height);
    spriteScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER); // Disable horizontal scrolling
    spriteScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER); // Disable vertical scrolling
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
    var spriteRenderer = prefab.getComponent(
        oogasalad.model.engine.component.SpriteRenderer.class);
    String imagePath = spriteRenderer.getImagePath();
    if (imagePath != null && !imagePath.isEmpty()) {
      return "src/main/resources/" + imagePath;
    }
    logger.error("Error getting preview image from prefab " + prefab.getName());
    return null;
  }

}
