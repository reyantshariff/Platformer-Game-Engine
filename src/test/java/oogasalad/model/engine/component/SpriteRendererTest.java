package oogasalad.model.engine.component;

import oogasalad.model.engine.base.enumerate.ComponentTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpriteRendererTest {

  private SpriteRenderer spriteRenderer;

  @BeforeEach
  void setup() {
    spriteRenderer = new SpriteRenderer();
    List<String> testPaths = List.of("img1.png", "img2.png", "img3.png");
    spriteRenderer.setImagePaths(testPaths);
    spriteRenderer.start();
  }

  @Test
  void getOffsetX_DefaultState_ReturnsZero() {
    assertEquals(0.0, spriteRenderer.getOffsetX());
  }

  @Test
  void setOffsetX_ValidValue_UpdatesOffset() {
    spriteRenderer.setOffsetX(25.5);
    assertEquals(25.5, spriteRenderer.getOffsetX());
  }

  @Test
  void getOffsetY_DefaultState_ReturnsZero() {
    assertEquals(0.0, spriteRenderer.getOffsetY());
  }

  @Test
  void setOffsetY_ValidValue_UpdatesOffset() {
    spriteRenderer.setOffsetY(-12.75);
    assertEquals(-12.75, spriteRenderer.getOffsetY());
  }

  @Test
  void getImagePath_InitialState_ReturnsFirstImagePath() {
    assertEquals("img1.png", spriteRenderer.getImagePath());
  }

  @Test
  void setSpriteIndex_ValidIndex_SelectsCorrectSprite() {
    spriteRenderer.setSpriteIndex(2);
    assertEquals("img3.png", spriteRenderer.getImagePath());
  }

  @Test
  void setSpriteIndex_NegativeIndex_ClampsToZero() {
    spriteRenderer.setSpriteIndex(-5);
    assertEquals("img1.png", spriteRenderer.getImagePath());
  }

  @Test
  void setSpriteIndex_IndexTooHigh_ClampsToLast() {
    spriteRenderer.setSpriteIndex(100);
    assertEquals("img3.png", spriteRenderer.getImagePath());
  }

  @Test
  void componentTag_Always_ReturnsRenderTag() {
    assertEquals(ComponentTag.RENDER, spriteRenderer.componentTag());
  }
}
