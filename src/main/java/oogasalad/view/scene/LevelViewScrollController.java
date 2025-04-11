package oogasalad.view.scene;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.ScrollEvent;

/**
 * {@code LevelViewScrollController} manages the viewport and zoom controls
 * for a scrollable level preview within the level editor.
 *
 * <p>This record wraps a {@link ScrollPane} and the associated {@link Group}
 * used for rendering the game canvas. It adds support for arrow key panning
 * and mouse scroll wheel zooming, bounded by minimum and maximum zoom levels.</p>
 *@author Reyan Shariff, Calvin Chen
 * @param scrollPane the scroll container displaying the level preview
 * @param canvasGroup the group containing the rendered canvas content
 * @param previewWidth the fixed width of the viewport
 * @param previewHeight the fixed height of the viewport
 */
public record LevelViewScrollController(
    ScrollPane scrollPane,
    Group canvasGroup,
    double previewWidth,
    double previewHeight
) {

  private static final double ZOOM_FACTOR = 1.05;
  private static final double MAX_ZOOM = 2.0;
  private static final double MIN_ZOOM = 0.5;

  /**
   * Constructs a {@code LevelViewScrollController} with the given canvas group
   * and viewport dimensions, automatically configuring the scroll pane and
   * adding keyboard and zoom handlers.
   *
   * @param canvasGroup the group representing the visual canvas
   * @param previewWidth the desired width of the scrollable viewport
   * @param previewHeight the desired height of the scrollable viewport
   */
  public LevelViewScrollController(Group canvasGroup, double previewWidth, double previewHeight) {
    this(
        new ScrollPane(canvasGroup),
        canvasGroup,
        previewWidth,
        previewHeight
    );
    setupScrollPaneDefaults();
    addKeyScrollHandler();
    addZoomHandler();
  }

  /**
   * Sets default settings for the {@link ScrollPane}, including size,
   * scroll bar policy, and focus behavior.
   */
  private void setupScrollPaneDefaults() {
    scrollPane.setPrefViewportWidth(previewWidth);
    scrollPane.setPrefViewportHeight(previewHeight);
    scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setFocusTraversable(true);
    scrollPane.requestFocus();
  }

  /**
   * Adds key event handlers to the scroll pane that allow
   * scrolling using the arrow keys.
   */
  private void addKeyScrollHandler() {
    scrollPane.setOnKeyPressed(event -> {
      double delta = 0.05;
      switch (event.getCode()) {
        case LEFT -> scrollPane.setHvalue(Math.max(scrollPane.getHvalue() - delta, 0));
        case RIGHT -> scrollPane.setHvalue(Math.min(scrollPane.getHvalue() + delta, 1));
        case UP -> scrollPane.setVvalue(Math.max(scrollPane.getVvalue() - delta, 0));
        case DOWN -> scrollPane.setVvalue(Math.min(scrollPane.getVvalue() + delta, 1));
      }
      event.consume();
    });
  }

  /**
   * Adds a scroll event handler to zoom in or out using the mouse wheel.
   * The zoom level is constrained by {@code MIN_ZOOM} and {@code MAX_ZOOM}.
   */
  private void addZoomHandler() {
    scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
      double currentScale = canvasGroup.getScaleX();
      if (event.getDeltaY() < 0 && currentScale / ZOOM_FACTOR >= MIN_ZOOM) {
        currentScale /= ZOOM_FACTOR;
      } else if (event.getDeltaY() > 0 && currentScale * ZOOM_FACTOR <= MAX_ZOOM) {
        currentScale *= ZOOM_FACTOR;
      }
      canvasGroup.setScaleX(currentScale);
      canvasGroup.setScaleY(currentScale);
      scrollPane.setVvalue(0); // optional: resets vertical scroll on zoom
      event.consume();
    });
  }

  /**
   * Returns the X-coordinate of the center point of the current viewport.
   *
   * @return the horizontal midpoint of the visible area
   */
  public double getViewportMidX() {
    return scrollPane.getHvalue() * previewWidth + (previewWidth / 2);
  }

  /**
   * Returns the Y-coordinate of the center point of the current viewport.
   *
   * @return the vertical midpoint of the visible area
   */
  public double getViewportMidY() {
    return scrollPane.getVvalue() * previewHeight + (previewHeight / 2);
  }
}
