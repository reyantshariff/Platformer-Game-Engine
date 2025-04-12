package oogasalad.view.gui.panel;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.view.gui.deserializedFieldInput.DeserializedFieldUIFactory;

/**
 * ComponentPanel is a GUI component that displays a list of game components.
 * It is used in the BuilderScene to let the user select and configure components for the game.
 *
 * Now includes expand/collapse feature with a toggle arrow.
 *
 * @author Hsuan-Kai Liao
 */
public class ComponentPanel extends VBox {

  private static final String COLLAPSED_ARROW = "▶";
  private static final String EXPANDED_ARROW = "▼";
  private static final String DELETE_BUTTON = "-";

  private final VBox contentBox = new VBox();

  /**
   * Constructor for ComponentPanel.
   *
   * @param component the game component to be displayed
   */
  public ComponentPanel(GameComponent component) {
    super();
    setStyle("-fx-background-color: #e4e4e4; -fx-padding: 10px;");
    setAlignment(Pos.TOP_CENTER);
    setSpacing(10);

    // Add expandable header
    HBox header = new HBox();
    header.setAlignment(Pos.CENTER_LEFT);
    header.setSpacing(5);
    header.setCursor(Cursor.HAND);

    // Add arrow and title
    Label arrow = new Label(EXPANDED_ARROW);
    Label title = new Label(component.getClass().getSimpleName());
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    // Add delete button
    Button deleteButton = new Button(DELETE_BUTTON);
    deleteButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: transparent;");
    deleteButton.setOnAction(e -> {
      ((Pane) getParent()).getChildren().remove(this);
      component.getParent().removeComponent(component.getClass());
    });

    // Push deleteButton to the right
    HBox spacer = new HBox();
    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
    header.getChildren().addAll(arrow, title, spacer, deleteButton);

    contentBox.getChildren().addAll(getDeserializedComponents(component));
    contentBox.setVisible(false);
    contentBox.setManaged(false);

    header.setOnMouseClicked(e -> {
      boolean expanded = contentBox.isVisible();
      contentBox.setVisible(!expanded);
      contentBox.setManaged(!expanded);
      arrow.setText(expanded ? COLLAPSED_ARROW : EXPANDED_ARROW);
    });

    getChildren().addAll(header, contentBox);
  }

  private List<HBox> getDeserializedComponents(GameComponent component) {
    List<SerializedField<?>> fields = component.getSerializedFields();
    return fields.stream().map(DeserializedFieldUIFactory::createDeserializedFieldUI).toList();
  }
}
