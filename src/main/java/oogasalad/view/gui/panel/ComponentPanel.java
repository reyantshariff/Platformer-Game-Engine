package oogasalad.view.gui.panel;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.view.gui.deserializedFieldInput.DeserializedFieldUIFactory;

/**
 * ComponentPanel is a GUI component that displays a list of game components.
 * It is used in the BuilderScene to let the user select and configure components for the game.
 *
 * @author Hsuan-Kai Liao
 */
public class ComponentPanel extends VBox {

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

    // Add component title
    Label title = new Label(component.getClass().getSimpleName());
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    // Add component details to the panel
    getChildren().add(title);
    getChildren().addAll(getDeserializedComponents(component));
  }

  private List<HBox> getDeserializedComponents(GameComponent component) {
    List<SerializedField<?>> fields = component.getSerializedFields();
    return fields.stream().map(DeserializedFieldUIFactory::createDeserializedFieldUI).toList();
  }

}
