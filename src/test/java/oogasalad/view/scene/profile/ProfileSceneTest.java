package oogasalad.view.scene.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.google.cloud.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import oogasalad.model.config.GameConfig;
import oogasalad.model.profile.PlayerData;
import oogasalad.model.profile.SessionException;
import oogasalad.model.profile.SessionManagement;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ProfileScene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import util.DukeApplicationTest;

@ExtendWith(MockitoExtension.class)
public class ProfileSceneTest extends DukeApplicationTest {

  @Mock
  private MainViewManager mockMainViewManager;

  @Mock
  private GameConfig mockGameConfig;

  @Mock
  private PlayerData mockPlayerData;

  private ProfileScene profileScene;

  @BeforeEach
  void setUp() {
    when(mockPlayerData.getFullName()).thenReturn("John Doe");
    when(mockPlayerData.getUsername()).thenReturn("johndoe123");

    try (MockedStatic<SessionManagement> sessionManagementMockedStatic = mockStatic(
        SessionManagement.class);
        MockedStatic<GameConfig> gameConfigMockedStatic = mockStatic(GameConfig.class)) {
      sessionManagementMockedStatic.when(SessionManagement::getCurrentUser)
          .thenReturn(mockPlayerData);
      gameConfigMockedStatic.when(() -> GameConfig.getNumber("windowWidth")).thenReturn(800.0);
      gameConfigMockedStatic.when(() -> GameConfig.getNumber("windowHeight")).thenReturn(600.0);

      profileScene = new ProfileScene(mockMainViewManager);
    }
  }

  @Test
  void testConstructorSetsUpScene() {
    assertNotNull(profileScene.getScene());
    assertEquals(800, profileScene.getScene().getWidth());
    assertEquals(600, profileScene.getScene().getHeight());
    assertTrue(profileScene.getScene().getRoot() instanceof VBox);
    assertEquals(javafx.geometry.Pos.CENTER,
        ((VBox) profileScene.getScene().getRoot()).getAlignment());
  }

  @Test
  void testSetUpLabelsDisplaysPlayerData() {
    // Act
    VBox root = (VBox) profileScene.getScene().getRoot();
    VBox playerInfo = (VBox) root.getChildren().get(0);

    // Assert
    assertEquals(4, playerInfo.getChildren().size());

    Label fullNameLabel = (Label) playerInfo.getChildren().get(0);
    assertEquals("John Doe", fullNameLabel.getText());
    assertEquals("fullName", fullNameLabel.getId());

    Label userNameLabel = (Label) playerInfo.getChildren().get(1);
    assertEquals("Username: johndoe123", userNameLabel.getText());
    assertEquals("username", userNameLabel.getId());

    Label dateCreatedLabel = (Label) playerInfo.getChildren().get(2);
    assertEquals("dateCreated", dateCreatedLabel.getId());

    Button editProfileButton = (Button) playerInfo.getChildren().get(3);
    assertEquals("Edit Profile", editProfileButton.getText());
    assertEquals("editProfileButton", editProfileButton.getId());
  }

  @Test
  void testSetUpLabelsHandlesSessionException() throws SessionException {
    try (MockedStatic<SessionManagement> sessionManagementMockedStatic = mockStatic(
        SessionManagement.class)) {
      sessionManagementMockedStatic.when(SessionManagement::getCurrentUser)
          .thenThrow(new SessionException("No current session"));

      // Re-initialize profileScene within the mocked static context
      profileScene = new ProfileScene(mockMainViewManager);

      VBox root = (VBox) profileScene.getScene().getRoot();
      assertEquals(1,
          root.getChildren().size()); // Should only have the empty VBox returned by setUpLabels
      assertTrue(root.getChildren().get(0) instanceof VBox);
      assertEquals(0, ((VBox) root.getChildren().get(0)).getChildren().size());
      // Optionally, you could assert that the logger was called with an error message
      // using Mockito.verify if you had a mock logger.
    }
  }
}