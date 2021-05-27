package Controller;

import StaticControl.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller of the login interface.
 * <p>
 *     Checks user's attempts for loging in and records them.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class LoginController implements Initializable {
    public Label locationValueLabel;

    public TextField usernameTextField;
    public TextField passwordField;
    public Button loginButton;
    public ProgressIndicator loadingAnimation;
    public AnchorPane anchorPane;

    private ResourceBundle loginBundle;

    public LoginController(){
    }

    /**
     * The {@link #locationValueLabel} is assigned a value. This label is used to display the country of the OS.
     * <p>
     *     The resource bundle is assigned to the {@link #loginBundle} parameter which is used to localized the errors displayed.<br>
     *     The checkFields method is called since it contains a listener and will only be activated when needed.
     * </p>
     * @see #checkFields()
     * @param location Location of the root object.
     * @param resources ResourceBundle provided in the main class, used for localization of the GUI interface
     * @since 1.0
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginBundle = resources;
        locationValueLabel.setText(Locale.getDefault().getCountry());
        checkFields();
    }

    /**
     * Binds the {@link #loginButton} to the {@link #usernameTextField} and {@link #passwordField}.
     * <p>
     *     Prevents the program from throwing an exception if ether TextField is empty and the {@link #loginButton} is pressed.
     * </p>
     * @since 1.0
     */
    private void checkFields(){
        BooleanBinding checkFields = usernameTextField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty());
        loginButton.disableProperty().bind(checkFields);
    }

    /**
     * Checks the username and password provided against the database.
     * <p>
     *     If the username and password match the records in the database, the program will launch the main screen.<br>
     *     If the information is not valid a error will be displayed using the AlertControl.errorMessage static method and the {@link #passwordField} will be cleared.<br>
     *     Any attempt to login will be recorded by using the LoginRecord.WriteRecord static method.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #loadingView(boolean)
     * @since 1.0
     */
    public void onLoginButton() {
        loadingView(true);
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        DBControl.runQuery(() -> {
            String query = "SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?;";
            try (Connection connection = DBControl.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()){
                    CurrentUser.setUserID(rs.getInt("User_ID"));
                    CurrentUser.setUserName(username);
                    LoginRecord.writeRecord(username, true);
                    Platform.runLater(() -> {
                        try {
                            ResourceBundle mainScreenBundle = ResourceBundle.getBundle("Bundles.MainScreen", Locale.getDefault());
                            Parent root = FXMLLoader.load(getClass().getResource("/View/MainScreenView.fxml"), mainScreenBundle);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setTitle(mainScreenBundle.getString("stage.title"));
                            stage.setScene(scene);
                            stage.setResizable(false);
                            loginButton.getScene().getWindow().hide();
                            stage.show();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            loadingView(false);
                        }
                    });
                } else {
                    LoginRecord.writeRecord(username, false);
                    Platform.runLater(() -> {
                        loadingView(false);
                        AlertControl.errorMessage(loginBundle.getString("error.userNotFound.title"), loginBundle.getString("error.userNotFound.content"));
                    });
                }
            } catch (SQLException | NullPointerException ex) {
                System.out.println(ex.getMessage());
                loadingView(false);
            }
        });
    }

    /**
     * A confirmation alert is shown, if the result is OK the program will close; otherwise the window will stay opened.
     * <p>
     *     Called when the cancel Button is pressed.
     * </p>
     * @since 1.0
     */
    public void onCancelButton() {
        Optional<ButtonType> result = AlertControl.askConfirmation(loginBundle.getString("error.close.title"), loginBundle.getString("error.close.content"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * Passes the focus to the {@link #passwordField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the username on the {@link #usernameTextField}.
     * </p>
     * @since 1.0
     */
    public void onUsernameTextField() {
        passwordField.requestFocus();
    }

    /**
     * Calls the {@link #onLoginButton()} method, therefore giving the same result as if the user had clicked the {@link #loginButton}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the password on the {@link #passwordField}.
     * </p>
     * @since 1.0
     */
    public void onPasswordField() {
        onLoginButton();
    }

    /**
     * Prevents the user from trying to summit multiple login attempts by disabling the GUI interface and showing a loading animation
     * until the processes sent to the database are done processing.
     * <p>
     *     If the state value is false, then the {@link #passwordField} will be cleared and given the focus.
     * </p>
     * @param state A boolean that sets the {@link #anchorPane} and the {@link #loadingAnimation}.
     * @since 1.0
     */
    private void loadingView(boolean state){
        if (!state){
            Platform.runLater(() -> {
                passwordField.clear();
                passwordField.requestFocus();
            });
        }
        anchorPane.setDisable(state);
        loadingAnimation.setVisible(state);
    }
}
