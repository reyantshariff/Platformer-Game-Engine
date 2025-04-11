package oogasalad.view.scene;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.ScrollEvent;

public record LevelViewScrollController(
    ScrollPane scrollPane,
    Group canvasGroup,
    double previewWidth,
    double previewHeight
) {

  private static final double ZOOM_FACTOR = 1.05;
  private static final double MAX_ZOOM = 2.0;
  private static final double MIN_ZOOM = 0.5;

  public LevelViewScrollController(Group canvasGroup, double previewWidth, double previewHeight) {
    this(
        new ScrollPane(canvasGroup), // <--- pass the same group into the ScrollPane
        canvasGroup,
        previewWidth,
        previewHeight
    );
    setupScrollPaneDefaults();
    addKeyScrollHandler();
    addZoomHandler();
  }

  private void setupScrollPaneDefaults() {
    scrollPane.setPrefViewportWidth(previewWidth);
    scrollPane.setPrefViewportHeight(previewHeight);
    scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setFocusTraversable(true);
    scrollPane.requestFocus();
  }

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
      scrollPane.setVvalue(0); // optional
      event.consume();
    });
  }

  public double getViewportMidX() {
    return scrollPane.getHvalue() * previewWidth + (previewWidth / 2);
  }

  public double getViewportMidY() {
    return scrollPane.getVvalue() * previewHeight + (previewHeight / 2);
  }
}
