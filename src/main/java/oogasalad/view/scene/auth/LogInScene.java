package oogasalad.view.scene.auth;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.controller.GameController;
import oogasalad.model.config.GameConfig;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;

import static oogasalad.model.config.GameConfig.LOGGER;

/**
 * This is the scene that visualizes the login process which also initializes the logged in user within the Session Management
 *
 * @author Daniel Rodriguez-Florido, Justin Aronwald
 */
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
  private static final String REMEMBER_ME_ID = "rememberMeCheckbox";

  private final GameController gameController;
  private TextField usernameField;
  private TextField nakedPasswordField;
  private PasswordField passwordField;
  private CheckBox rememberMe;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param manager - the view switcher that handles the various scenes
   */
  public LogInScene(MainViewManager manager) {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));

    VBox card = new VBox(20);
    card.setId(SOCIAL_CARD_ID);

    setupWelcomeMessage(card);
    setupUsernameField(card);
    setupPasswordField(card);
    setUpCheckbox(card);
    setupLoginButton(card);
    setupSignUpButton(card);
    gameController = new GameController(manager);

    ((StackPane) getScene().getRoot()).getChildren().add(card);
  }

  private void setupWelcomeMessage(VBox card) {
    Label welcomeMessage = new Label(GameConfig.getText(SOCIAL_WELCOME_MESSAGE_ID));
    welcomeMessage.setId(SOCIAL_WELCOME_MESSAGE_ID);
    card.getChildren().add(welcomeMessage);
  }

  private void setupUsernameField(VBox card) {
    usernameField = new TextField();
    usernameField.setPromptText(GameConfig.getText(SOCIAL_EMAIL_PROMPT_ID));
    usernameField.setId(SOCIAL_EMAIL_PROMPT_ID);
    card.getChildren().add(usernameField);
  }

  private void setupPasswordField(VBox card) {
    nakedPasswordField = new TextField();
    nakedPasswordField.setPromptText(GameConfig.getText(SOCIAL_PASSWORD_PROMPT_ID));
    nakedPasswordField.setVisible(false);
    nakedPasswordField.setManaged(false);
    nakedPasswordField.setId(SOCIAL_PASSWORD_PROMPT_ID);

    passwordField = new PasswordField();
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

    setButtonOnClick(loginButton);
    card.getChildren().add(loginButton);
  }

  private void setButtonOnClick(Button loginButton) {
    loginButton.setOnMouseClicked(event -> {
        String username = usernameField.getText().trim();
        String password = getTrimmedPassword();

        if (username.isEmpty() || password.isEmpty()) {
          LOGGER.info("Missing username or password");
          return;
        }

        try {
          if (gameController.handleLogin(username, password, rememberMe.isSelected())) {
            LOGGER.info("Successfully logged in");
          } else {
            LOGGER.info("Failed to log in");
          }
        } catch (IOException e) {
          LOGGER.warn("Error setting up autologin:", e);
        }
    });
  }

  private String getTrimmedPassword() {
    return nakedPasswordField.isVisible()
        ? nakedPasswordField.getText().trim()
        : passwordField.getText().trim();
  }

  private void setupSignUpButton(VBox card) {
    Button signUpButton = new Button(GameConfig.getText(SOCIAL_SIGN_UP_BUTTON_ID));
    signUpButton.setId(SOCIAL_SIGN_UP_BUTTON_ID);
    card.getChildren().add(signUpButton);
  }

  private void setUpCheckbox(VBox card) {
    rememberMe = new CheckBox("Remember Me");
    rememberMe.setId(REMEMBER_ME_ID);
    card.getChildren().add(rememberMe);
  }

}
