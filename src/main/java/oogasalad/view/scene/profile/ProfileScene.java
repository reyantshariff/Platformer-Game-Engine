package oogasalad.view.scene;

import static oogasalad.model.config.GameConfig.LOGGER;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import oogasalad.model.config.GameConfig;
import oogasalad.model.profile.PlayerData;
import oogasalad.model.profile.SessionException;
import oogasalad.model.profile.SessionManagement;

/**
 * Profile view with social hub and profile edit options
 */
public class ProfileScene extends ViewScene {

  /**
   * Constructs a new ProfileScene to display the player's profile
   *
   * @param manager The MainViewManager used to switch scenes.
   */
  public ProfileScene(MainViewManager manager) {
    super(new VBox(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));

    VBox root = (VBox) getScene().getRoot();
    root.setAlignment(Pos.CENTER);

    VBox playerInfo = setUpLabels();

    root.getChildren().add(playerInfo);
  }

  private VBox setUpLabels() {
    VBox playerInfo = new VBox();
    PlayerData currentPlayer = null;
    try {
      currentPlayer = SessionManagement.getCurrentUser();
    } catch (SessionException e) {
      LOGGER.error("There is no current session.");
      return new VBox();
      // TODO: direct player to sign in screen
    }
    Label fullName = new Label(currentPlayer.getFullName());
    fullName.setId("fullName");
    Label userName = new Label("Username: " + currentPlayer.getUsername());
    userName.setId("username");
    Label dateCreated = new Label("Date Joined: " + currentPlayer.getCreatedAt());
    dateCreated.setId("dateCreated");

    Button editProfile = new Button("Edit Profile");
    editProfile.setId("editProfileButton");
    // TODO: create profile edit scene
    // editProfile.setOnAction(e -> {})
    playerInfo.getChildren().addAll(fullName, userName, dateCreated, editProfile);
    return playerInfo;
  }
}
