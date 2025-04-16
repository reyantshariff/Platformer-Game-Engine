package oogasalad.view.scene;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.controller.GameController;
import oogasalad.model.config.GameConfig;

public class SignUpScene extends ViewScene {

  private static final String SIGNUP_CARD_ID = "signUpCard";
  private static final String SIGNUP_LABEL_ID = "signUpLabel";
  private static final String SIGNUP_PROMPT_ID = "signUpPrompt";
  private static final String SIGNUP_FIRST_NAME_FIELD_ID = "signUpFirstNameField";
  private static final String SIGNUP_LAST_NAME_FIELD_ID = "signUpLastNameField";
  private static final String SOCIAL_EMAIL_PROMPT_ID = "socialEmailPrompt";
  private static final String SOCIAL_PASSWORD_PROMPT_ID = "socialPasswordPrompt";

  private GameController gameController;
  private TextField usernameField;
  private TextField passwordField;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param manager The view manager of type MainViewManager that will be in charge of managing this
   *                scene
   */
  public SignUpScene(MainViewManager manager) {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));

    VBox card = new VBox(20);
    card.setId(SIGNUP_CARD_ID);

    setUpSignUpLabel(card);
    setUpSignUpPrompt(card);
    setUpFirstNameField(card);
    setUpLastNameField(card);
    setUsernameField(card);
    setPasswordField(card);

    gameController = new GameController(manager);

    ((StackPane) getScene().getRoot()).getChildren().add(card);
  }

  private void setUpSignUpLabel(VBox card) {
    Label signUpLabel = new Label(GameConfig.getText(SIGNUP_LABEL_ID));
    signUpLabel.setId(SIGNUP_LABEL_ID);
    card.getChildren().add(signUpLabel);
  }

  private void setUpSignUpPrompt(VBox card) {
    Label signUpPrompt = new Label(GameConfig.getText(SIGNUP_PROMPT_ID));
    signUpPrompt.setId(SIGNUP_PROMPT_ID);
    card.getChildren().add(signUpPrompt);
  }

  private void setUpFirstNameField(VBox card) {
    TextField firstNameField = new TextField();
    firstNameField.setPromptText(GameConfig.getText(SIGNUP_FIRST_NAME_FIELD_ID));
    firstNameField.setId(SIGNUP_FIRST_NAME_FIELD_ID);
    card.getChildren().add(firstNameField);
  }

  private void setUpLastNameField(VBox card) {
    TextField lastNameField = new TextField();
    lastNameField.setPromptText(GameConfig.getText(SIGNUP_LAST_NAME_FIELD_ID));
    lastNameField.setId(SIGNUP_LAST_NAME_FIELD_ID);
    card.getChildren().add(lastNameField);
  }

  private void setUsernameField(VBox card) {
    usernameField = new TextField();
    usernameField.setPromptText(GameConfig.getText(SOCIAL_EMAIL_PROMPT_ID));
    usernameField.setId(SOCIAL_EMAIL_PROMPT_ID);
    card.getChildren().add(usernameField);
  }

  private void setPasswordField(VBox card) {
    passwordField = new TextField();
    passwordField.setPromptText(GameConfig.getText(SOCIAL_PASSWORD_PROMPT_ID));
    passwordField.setId(SOCIAL_EMAIL_PROMPT_ID);
    card.getChildren().add(passwordField);
  }

}
