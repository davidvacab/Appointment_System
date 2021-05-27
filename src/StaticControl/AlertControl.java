package StaticControl;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Controller of all the alert notifications shown in the program.
 * @author David Vaca
 * @since 1.0
 */
public class AlertControl {
    /**
     * Displays errors and alerts to the user.
     * <p>
     *     Localized parameters must be provide.
     * </p>
     * @param title Localized title of the alert.
     * @param content Localized content of the alert.
     * @since 1.0
     */
    public static void errorMessage(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Ask the user for confirmation before executed certain parts of the code.
     * <p>
     *     Localized parameters must be provide.
     * </p>
     * @param title Localized title of the alert.
     * @param content Localized content of the alert.
     * @return returns the user selection used to perform different actions.
     * @since 1.0
     */
    public static Optional<ButtonType> askConfirmation(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
