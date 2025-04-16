package oogasalad.view.scene.auth;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.controller.GameController;
import oogasalad.database.DatabaseException;
import oogasalad.model.config.GameConfig;
import oogasalad.model.config.PasswordHashingException;
import oogasalad.model.profile.SignUpRequest;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;

import static oogasalad.model.config.GameConfig.LOGGER;

/**
 * This is the scene that visualizes the sign-up process so that users can put their information in
 * the Firebase database.
 *
 * @author Daniel Rodriguez-Florido, Justin Aronwald
 */
public class SignUpScene extends ViewScene {

  private static final String SIGNUP_CARD_ID = "signUpCard";
  private static final String SIGNUP_LABEL_ID = "signUpLabel";
  private static final String SIGNUP_PROMPT_ID = "signUpPrompt";
  private static final String SIGNUP_FIRST_NAME_FIELD_ID = "signUpFirstNameField";
  private static final String SIGNUP_LAST_NAME_FIELD_ID = "signUpLastNameField";
  private static final String SOCIAL_EMAIL_PROMPT_ID = "socialEmailPrompt";
  private static final String SOCIAL_PASSWORD_PROMPT_ID = "socialPasswordPrompt";

  private final GameController gameController;
  private TextField usernameField;
  private TextField passwordField;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param manager The view manager of type MainViewManager that will be in charge of managing this
   *                scene
   */
  public SignUpScene(MainViewManager manager) {
    super(new StackPane(), GameConfig.getNumber("windowWidth"),
        GameConfig.getNumber("windowHeight"));

    VBox card = new VBox(20);
    card.setId(SIGNUP_CARD_ID);

    setUpSignUpLabel(card);
    setUpSignUpPrompt(card);
    setUpFirstNameField(card);
    setUpLastNameField(card);
    handleUsernameField(card);
    handlePasswordField(card);
    handleSignUpButton(card);
    handleLoginButton(card);

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

  private void handleUsernameField(VBox card) {
    usernameField = new TextField();
    usernameField.setPromptText(GameConfig.getText(SOCIAL_EMAIL_PROMPT_ID));
    usernameField.setId(SOCIAL_EMAIL_PROMPT_ID);
    card.getChildren().add(usernameField);
  }

  private void handlePasswordField(VBox card) {
    passwordField = new TextField();
    passwordField.setPromptText(GameConfig.getText(SOCIAL_PASSWORD_PROMPT_ID));
    passwordField.setId(SOCIAL_PASSWORD_PROMPT_ID);
    card.getChildren().add(passwordField);
  }

  private void handleSignUpButton(VBox card) {
    Button signUpButton = new Button(GameConfig.getText(SIGNUP_LABEL_ID));
    signUpButton.setId("SignUpButton");
    signUpButton.setOnAction(e -> {
      String username = usernameField.getText().trim();
      String password = passwordField.getText().trim();
      String firstName = usernameField.getText().trim();
      String lastName = passwordField.getText().trim();

      if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
        LOGGER.warn("One or more field is missing");
        return;
      }
      handleGameControllerSignUp(new SignUpRequest(username, password, firstName, lastName));
    });
    card.getChildren().add(signUpButton);
  }

  private void handleGameControllerSignUp(SignUpRequest signUpRequest) {
    try {
      gameController.handleSignUp(signUpRequest);
    } catch (PasswordHashingException ex) {
      LOGGER.error("Error hashing password:{}", ex.getMessage());
      //show on front
    } catch (DatabaseException ex) {
      LOGGER.error("User already exists in database:{}", ex.getMessage());
      //show on front - IMPORTANT
    }
  }

  private void handleLoginButton(VBox card) {
    Button logInButton = new Button("Log In");
    logInButton.setId("LoginButton");
    logInButton.setOnAction(e -> MainViewManager.getInstance().switchTo("LogInScene"));
    card.getChildren().add(logInButton);
  }

}
