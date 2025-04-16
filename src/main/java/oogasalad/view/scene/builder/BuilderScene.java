package oogasalad.view.scene.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.util.Duration;
import oogasalad.model.builder.Builder;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.Transform;
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

  private static final String COMPONENT_PACKAGE_NAME = "oogasalad.model.engine.component";
  private static final String GAME_PREVIEW = "GamePreview";
  public static final double MAX_ZOOM = 5.0;
  public static final double MIN_ZOOM = 0.1;

  // Define the initial zoom and pan for the level preview, when opening a BuilderScene
  public static final double DEFAULT_ZOOM = 0.5;
  public static final double DEFAULT_ZOOM_START_X = -1400;
  public static final double DEFAULT_ZOOM_START_Y = -200;

  private static final Logger logger = LogManager.getLogger(BuilderScene.class);

  private final BorderPane myWindow;
  private final ObjectDragger objectDragger;

  private Builder builder;
  private Pane myGameViewPane;
  private Pane canvasHolder;
  private Canvas myGameCanvas;
  private VBox myComponentContainer;

  private BuilderScene() {
    // Create the BorderPane as the root
    super(new BorderPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    myWindow = (BorderPane) getScene().getRoot();
    initializeUI();
    objectDragger = new ObjectDragger(myGameCanvas, this, getSceneRenderer());
  }

  private void initializeUI() {
    // Top bar
    HBox topBar = new HBox();
    Button mainMenuButton = new Button("Main Menu");
    MainViewManager viewManager = MainViewManager.getInstance();
    mainMenuButton.setOnAction(e -> viewManager.switchToMainMenu());

    Button previewLevelButton = new Button("Preview Level");
    viewManager.addViewScene(GamePreviewScene.class, GAME_PREVIEW);
    previewLevelButton.setOnAction(e -> {
      GamePreviewScene previewScene = (GamePreviewScene) viewManager.getViewScene(GAME_PREVIEW);
      builder.saveGameAs("temp.json");
      previewScene.preview();
      viewManager.switchTo(GAME_PREVIEW);
    });

    topBar.getChildren().addAll(mainMenuButton, previewLevelButton);
    myWindow.setTop(topBar);

    // Add SplitPane for sprite button options and object control panel
    SplitPane objectControlSplitPane = new SplitPane(createAddSpriteButtonOptions(), createObjectControlPanel());
    objectControlSplitPane.setOrientation(Orientation.HORIZONTAL);
    objectControlSplitPane.setDividerPositions(0.7);

    // Add SplitPane for splitting the view and object panel
    myGameViewPane = createGamePreview();
    SplitPane viewObjectSplitPane = new SplitPane(myGameViewPane, objectControlSplitPane);
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
   * Set up the builder for the given game filepath.
   * This should be called when the new game file is to be edited.
   * @param gameFilepath the given game filepath
   */
  public void setUpBuilder(String gameFilepath) {
    builder = new Builder(gameFilepath);
    objectDragger.setUpBuilder(builder);
    objectDragger.setupListeners();
    updateGamePreview();
    Platform.runLater(this::resetCanvas);
  }

  /**
   * Updates game preview window by rendering all current GameObjects in the GameScene in their
   * current position
   */
  public void updateGamePreview() {
    GraphicsContext gc = myGameCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, myGameCanvas.getWidth(), myGameCanvas.getHeight());
    getSceneRenderer().renderWithoutCamera(gc, builder.getCurrentScene(), builder.getSelectedObject());
  }

  /**
   * Handles the object selection change
   */
  public void handleObjectSelectionChange() {
    componentSelectionUpdate();
  }

  /**
   * Handles the object attribute change
   */
  public void handleObjectAttributeChange() {
    // NOTE: This can be done in a more elegant way, in which you have a updateAttribute method in
    //       the component panel that updates the attribute of the selected object, right now we
    //       are just generating new panels.
    //   BY: Hsuan-Kai Liao

    GameObject selectedObject = builder.getSelectedObject();
    if (selectedObject == null) {
      return;
    }

    List<Boolean> expandedList = new ArrayList<>();
    for (Node node : myComponentContainer.getChildren()) {
      if (node instanceof ComponentPanel) {
        expandedList.add(((ComponentPanel) node).isExpanded());
      }
    }

    myComponentContainer.getChildren().clear();
    List<GameComponent> components = new ArrayList<>(selectedObject.getAllComponents().values());
    components.sort(Comparator.comparing(c -> c.componentTag().ordinal()));
    for (GameComponent component : components) {
      ComponentPanel componentPanel = new ComponentPanel(component);
      componentPanel.setExpanded(expandedList.get(components.indexOf(component)));
      myComponentContainer.getChildren().add(componentPanel);
    }
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
    myGameCanvas = new Canvas(4000, 600);

    canvasHolder = new Pane(myGameCanvas);

    // TODO: remove hardcoded canvas boundary
    canvasHolder.setStyle("-fx-border-color: black; -fx-border-width: 2;");
    Pane container = new Pane(canvasHolder);

    // Scene dragging and zooming
    setupZoomAndPan(container, canvasHolder);

    return container;
  }

  private void setupZoomAndPan(Pane container, Pane canvasHolder) {
    final ObjectProperty<Point2D> lastMousePosition = new SimpleObjectProperty<>();

    // Set zoom to positional and scale defaults when view is set up
    Point2D startZoomCenter = new Point2D(DEFAULT_ZOOM_START_X, DEFAULT_ZOOM_START_Y);
    Point2D startZoomCenterInGroup = canvasHolder.parentToLocal(startZoomCenter);
    zoomCanvas(canvasHolder, DEFAULT_ZOOM, startZoomCenter, startZoomCenterInGroup);

    // handle mouse pressed and dragged events
    container.setOnMousePressed(event -> {
      if (builder.objectIsSelected()) return;
      lastMousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
      container.requestFocus();
    });
    container.setOnMouseDragged(event -> {
      if (builder.objectIsSelected()) return;

      Point2D currentMousePosition = new Point2D(event.getSceneX(), event.getSceneY());
      Point2D delta = currentMousePosition.subtract(lastMousePosition.get());

      canvasHolder.setTranslateX(canvasHolder.getTranslateX() + delta.getX());
      canvasHolder.setTranslateY(canvasHolder.getTranslateY() + delta.getY());

      lastMousePosition.set(currentMousePosition);
    });

    // handle zooming
    container.setOnScroll(event -> {
      double zoomFactor = 1.05;
      double deltaY = event.getDeltaY();
      double oldScale = canvasHolder.getScaleX();
      double scale = (deltaY > 0) ? oldScale * zoomFactor : oldScale / zoomFactor;

      Point2D mousePoint = new Point2D(event.getX(), event.getY());
      Point2D mouseInGroup = canvasHolder.parentToLocal(mousePoint);

      zoomCanvas(canvasHolder, scale, mousePoint, mouseInGroup);

      event.consume();
    });

    container.setOnZoom(event -> {
      double oldScale = canvasHolder.getScaleX();
      double scale = oldScale * event.getZoomFactor();

      Point2D zoomCenter = new Point2D(event.getX(), event.getY());
      Point2D zoomCenterInGroup = canvasHolder.parentToLocal(zoomCenter);

      zoomCanvas(canvasHolder, scale, zoomCenter, zoomCenterInGroup);

      event.consume();
    });
  }

  private void resetCanvas() {
    Point2D zoomOrigin = new Point2D(0, 0);
    Point2D zoomOriginInCanvas = canvasHolder.parentToLocal(zoomOrigin);
    zoomCanvas(canvasHolder, DEFAULT_ZOOM, zoomOrigin, zoomOriginInCanvas);
    Point2D canvasTopLeftInContainer = canvasHolder.localToParent(0, 0);

    double deltaX = -canvasTopLeftInContainer.getX();
    double deltaY = -canvasTopLeftInContainer.getY();

    canvasHolder.setTranslateX(canvasHolder.getTranslateX() + deltaX);
    canvasHolder.setTranslateY(canvasHolder.getTranslateY() + deltaY);
  }

  private static void zoomCanvas(Pane canvasHolder, double scale, Point2D zoomPoint, Point2D pointInGroup) {
    scale = Math.min(Math.max(scale, MIN_ZOOM), MAX_ZOOM);
    canvasHolder.setScaleX(scale);
    canvasHolder.setScaleY(scale);

    Point2D newMouseInGroup = canvasHolder.parentToLocal(zoomPoint);

    Point2D delta = newMouseInGroup.subtract(pointInGroup);
    canvasHolder.setTranslateX(canvasHolder.getTranslateX() + delta.getX() * scale);
    canvasHolder.setTranslateY(canvasHolder.getTranslateY() + delta.getY() * scale);
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
    double spriteButtonPaneWidth = getScene().getWidth() * 0.5;

    // Organize the button layout via TilePane
    TilePane tilePane = createSpriteButtonTilePane(spriteButtonPaneWidth);

    // Create a ScrollPane to hold the buttons
    ScrollPane spriteScrollPane = createSpriteButtonScrollPane(spriteButtonPaneWidth, tilePane);

    // Load the prefab GameObjects
    // TODO: remove hardcoded game type
    List<GameObject> prefabObjects = PrefabLoader.loadAvailablePrefabs("dinosaur");
    for (GameObject prefab : prefabObjects) {
     createObject(prefab, tilePane);
    }

    // Briefly animate the sprite tile options into their correct positions
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
      tilePane.setPrefWidth(spriteScrollPane.getWidth());
    }));
    timeline.setCycleCount(Animation.INDEFINITE); // Loop continuously
    timeline.play();
    new Timeline(new KeyFrame(Duration.seconds(3), event -> {
      timeline.stop(); // Stop after 3 seconds
    })).play();

    return spriteScrollPane;
  }

  /**
   * Align a newly-placed object into the center of the visible level preview
   */
  private void alignObject(Transform t, Pane container) {
    double containerCenterX = container.getWidth() / 2;
    double containerCenterY = container.getHeight() / 2;
    Point2D viewportCenter = new Point2D(containerCenterX, containerCenterY);
    Point2D centerInCanvas = canvasHolder.parentToLocal(viewportCenter);

    t.setX(centerInCanvas.getX());
    t.setY(centerInCanvas.getY());
  }


  private void createObject(GameObject prefab, TilePane tilePane) {
    // Assume the prefab has a SpriteRenderer component.
    String previewImagePath = getPreviewImagePath(prefab);
    // Convert the preview image path to a JavaFX Image
    try {
      assert previewImagePath != null;
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
          alignObject(t, myGameViewPane);
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

  private ScrollPane createSpriteButtonScrollPane(double width, Pane contents) {
    ScrollPane spriteScrollPane = new ScrollPane(contents);
    spriteScrollPane.setPrefWidth(width);
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
    logger.error("Error getting preview image from prefab {}", prefab.getName());
    return null;
  }

}
