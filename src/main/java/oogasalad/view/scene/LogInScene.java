package oogasalad.view.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.model.config.GameConfig;

public class LogInScene extends ViewScene {

  private static final String SOCIAL_CARD_ID = "socialCard";
  private static final String SOCIAL_WELCOME_MESSAGE_ID = "socialWelcomeMessage";
  private static final String SOCIAL_EMAIL_PROMPT_ID = "socialEmailPrompt";
  private static final String SOCIAL_PASSWORD_PROMPT_ID = "socialPasswordPrompt";
  private static final String SOCIAL_LOGIN_BUTTON_ID = "socialLoginButton";
  private static final String SOCIAL_SIGN_UP_BUTTON_ID = "socialSignUpButton";
  private static final String TOGGLE_PASSWORD_BUTTON_ID = "togglePasswordButton";
  private static final String SHOW_PASS_TEXT_ID = "showPassText";
  private static final String HIDE_PASS_TEXT_ID = "hidePassText";

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   */
  public LogInScene(MainViewManager manager) {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));

    VBox card = new VBox(20);
    card.setId(SOCIAL_CARD_ID);

    setupWelcomeMessage(card);
    setupEmailField(card);
    setupPasswordField(card);
    setupLoginButton(card);
    setupSignUpButton(card);

    ((StackPane) getScene().getRoot()).getChildren().add(card);
  }

  private void setupWelcomeMessage(VBox card) {
    Label welcomeMessage = new Label(GameConfig.getText(SOCIAL_WELCOME_MESSAGE_ID));
    welcomeMessage.setId(SOCIAL_WELCOME_MESSAGE_ID);
    card.getChildren().add(welcomeMessage);
  }

  private void setupEmailField(VBox card) {
    TextField emailField = new TextField();
    emailField.setPromptText(GameConfig.getText(SOCIAL_EMAIL_PROMPT_ID));
    emailField.setId(SOCIAL_EMAIL_PROMPT_ID);
    card.getChildren().add(emailField);
  }

  private void setupPasswordField(VBox card) {
    TextField nakedPasswordField = new TextField();
    nakedPasswordField.setPromptText(GameConfig.getText(SOCIAL_PASSWORD_PROMPT_ID));
    nakedPasswordField.setVisible(false);
    nakedPasswordField.setManaged(false);
    nakedPasswordField.setId(SOCIAL_PASSWORD_PROMPT_ID);

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText(GameConfig.getText(SOCIAL_PASSWORD_PROMPT_ID));
    passwordField.setId(SOCIAL_PASSWORD_PROMPT_ID);

    Button toggleButton = createShowPasswordButton(nakedPasswordField, passwordField);

    StackPane passwordStack = new StackPane(nakedPasswordField, passwordField, toggleButton);
    passwordStack.setPadding(new Insets(10));

    StackPane.setAlignment(toggleButton, Pos.CENTER_RIGHT);

    card.getChildren().add(passwordStack);
  }

  private Button createShowPasswordButton(TextField nakedPasswordField, PasswordField passwordField) {
    String showText = GameConfig.getText(SHOW_PASS_TEXT_ID);
    String hideText = GameConfig.getText(HIDE_PASS_TEXT_ID);

    Button eyeButton = new Button(showText);
    eyeButton.setId(TOGGLE_PASSWORD_BUTTON_ID);

    eyeButton.setOnAction(event -> {
      if (passwordField.isVisible()) {
        passwordField.setVisible(false);
        passwordField.setManaged(false);
        nakedPasswordField.setVisible(true);
        nakedPasswordField.setManaged(true);
        nakedPasswordField.setText(passwordField.getText());
        eyeButton.setText(hideText);
      } else {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        nakedPasswordField.setVisible(false);
        nakedPasswordField.setManaged(false);
        eyeButton.setText(showText);
      }
    });

    return eyeButton;
  }

  private void setupLoginButton(VBox card) {
    Button loginButton = new Button(GameConfig.getText(SOCIAL_LOGIN_BUTTON_ID));
    loginButton.setId(SOCIAL_LOGIN_BUTTON_ID);
    card.getChildren().add(loginButton);
  }

  private void setupSignUpButton(VBox card) {
    Button signUpButton = new Button(GameConfig.getText(SOCIAL_SIGN_UP_BUTTON_ID));
    signUpButton.setId(SOCIAL_SIGN_UP_BUTTON_ID);
    card.getChildren().add(signUpButton);
  }

}
