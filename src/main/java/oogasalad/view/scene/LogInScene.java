package oogasalad.view.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.controller.GameController;
import oogasalad.model.config.GameConfig;
import static oogasalad.model.config.GameConfig.LOGGER;


public class LogInScene extends ViewScene {

  private static final String SOCIAL_CARD_ID = "socialCard";
  private static final String SOCIAL_WELCOME_MESSAGE_ID = "socialWelcomeMessage";
  private static final String SOCIAL_EMAIL_PROMPT_ID = "socialEmailPrompt";
  private static final String SOCIAL_PASSWORD_PROMPT_ID = "socialPasswordPrompt";
  private static final String SOCIAL_LOGIN_BUTTON_ID = "socialLoginButton";
  private static final String SOCIAL_SIGN_UP_BUTTON_ID = "socialSignUpButton";
  private static final String TOGGLE_PASSWORD_BUTTON_ID = "togglePasswordButton";
  private GameController gameController;
  private TextField usernameField;
  private TextField nakedPasswordField;
  private PasswordField passwordField;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   */
  public LogInScene(MainViewManager manager) {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));

    VBox card = new VBox(20);
    card.setId(SOCIAL_CARD_ID);

    setupWelcomeMessage(card);
    setUsernameField(card);
    setupPasswordField(card);
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

  private void setUsernameField(VBox card) {
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

    passwordField = new PasswordField();
    passwordField.setPromptText(GameConfig.getText(SOCIAL_PASSWORD_PROMPT_ID));

    Button toggleButton = createShowPasswordButton(nakedPasswordField, passwordField);

    StackPane fieldStack = new StackPane(nakedPasswordField, passwordField, toggleButton);
    fieldStack.setId(SOCIAL_PASSWORD_PROMPT_ID);
    fieldStack.setAlignment(Pos.CENTER_RIGHT);

    card.getChildren().add(fieldStack);
  }

  private Button createShowPasswordButton(TextField nakedPasswordField, PasswordField passwordField) {
    Button eyeButton = new Button();
    Image image = new Image("file:/src/main/resources/oogasalad/button/eyeimage.jpg");
    ImageView eyeImageView = new ImageView(image);
    eyeImageView.setFitWidth(16);    // Adjust width as needed
    eyeImageView.setFitHeight(16);   // Adjust height as needed
    eyeImageView.setPreserveRatio(true);
    eyeButton.setGraphic(eyeImageView);
    eyeButton.setId(TOGGLE_PASSWORD_BUTTON_ID);

    eyeButton.setOnAction(event -> {
      if (passwordField.isVisible()) {
        passwordField.setVisible(false);
        passwordField.setManaged(false);
        nakedPasswordField.setVisible(true);
        nakedPasswordField.setManaged(true);
        nakedPasswordField.setText(passwordField.getText());
      } else {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        passwordField.setText(nakedPasswordField.getText());
        nakedPasswordField.setVisible(false);
        nakedPasswordField.setManaged(false);
      }
    });

    return eyeButton;
  }

  private void setupLoginButton(VBox card) {
    Button loginButton = new Button(GameConfig.getText(SOCIAL_LOGIN_BUTTON_ID));
    loginButton.setId(SOCIAL_LOGIN_BUTTON_ID);

    loginButton.setOnMouseClicked(event -> {
        String username = usernameField.getText().trim();
        String password = nakedPasswordField.isVisible()
            ? nakedPasswordField.getText().trim()
            : passwordField.getText().trim();

      if (!username.isEmpty() && !password.isEmpty()) {
        gameController.handleLogin(username, password);
      } else {
        LOGGER.info("Missing username or password");
        // Show to user
      }
    });
    card.getChildren().add(loginButton);
  }

  private void setupSignUpButton(VBox card) {
    Button signUpButton = new Button(GameConfig.getText(SOCIAL_SIGN_UP_BUTTON_ID));
    signUpButton.setId(SOCIAL_SIGN_UP_BUTTON_ID);
    card.getChildren().add(signUpButton);
  }

}
