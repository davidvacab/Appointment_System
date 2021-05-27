package StaticControl;

import javafx.application.Platform;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Getter for the connection with database and creator of new threads for the queries.
 * @author David Vaca
 * @since 1.0
 */
public class DBControl {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("Bundles.dbError", Locale.getDefault());

    /**
     * Encapsulates the process of connecting to the database.
     * <p>
     *     If the connection fails an Alert will display on the JavaFX Thread later.<br>
     *     A lambda expression is used to make easier use of the Runnable in the Platform.runLater.
     * </p>
     * @return Connection to the database.
     * @since 1.0
     */
    public static Connection getConnection(){
        Connection conn = null;
        String url       = "jdbc:mysql://wgudb.ucertify.com:3306/WJ06k2V";
        String user      = "U06k2V";
        String password  = "53688791220";
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            Platform.runLater(() -> AlertControl.errorMessage(bundle.getString("error.title"), bundle.getString("error.content")));
        }
        return conn;
    }

    /**
     * Creates a new thread and running the runnable.
     * @param runnable Runnable used for most queries so they can run in the background.
     * @since 1.0
     */
    public static void runQuery(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
