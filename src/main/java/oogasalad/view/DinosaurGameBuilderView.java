package oogasalad.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.builder.BuilderScene;
import oogasalad.gui.button.StylizedButton;

public class DinosaurGameBuilderView extends BorderPane {
  private BuilderScene sceneBackend;

  private StylizedButton loadButton;
  private StylizedButton saveButton;
  private StylizedButton playtestButton;

  private Pane editorCanvas;
  private VBox toolbox;
  private VBox propertiesPanel;

  public DinosaurGameBuilderView() {
    initializeUI();
  }

  private void initializeUI() {
    // Top bar
    HBox topBar = new HBox();
    loadButton = new StylizedButton("Load");
    saveButton = new StylizedButton("Save");
    playtestButton = new StylizedButton("Playtest");
    topBar.getChildren().addAll(loadButton.getButton(), saveButton.getButton(), playtestButton.getButton());
    setTop(topBar);

    // Left toolbox
    toolbox = new VBox();
    toolbox.getChildren().add(new Label("Blocks"));
    // Add more UI controls for placing blocks
    setLeft(toolbox);

    // Center canvas
    editorCanvas = new Pane();
    setCenter(editorCanvas);

    // Right properties panel
    propertiesPanel = new VBox();
    propertiesPanel.getChildren().add(new Label("Properties"));
    setRight(propertiesPanel);

    // Event handlers
//    loadButton.setOnAction(e -> handleLoad());
//    saveButton.setOnAction(e -> handleSave());
//    playtestButton.setOnAction(e -> handlePlaytest());
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

