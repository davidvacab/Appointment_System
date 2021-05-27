package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Course: Software II Advanced Java Concepts C195
 *  * This program is for keeping track of customer and appointment records and showing reports about them.
 * @author David Vaca
 * @version 1.0
 */
public class AppointmentSystem extends Application {


    /**
     * The Bundle is created first in order to localized all the parts of the GUI interface.
     * @param primaryStage The main stage used to load the Login controller.
     * @throws Exception Only use if no JavaFX modules are found.
     * @since 1.0
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle loginBundle = ResourceBundle.getBundle("Bundles.Login", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"), loginBundle);
        primaryStage.setTitle(loginBundle.getString("stage.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
