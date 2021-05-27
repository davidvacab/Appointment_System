package StaticControl;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Encapsulates the creation and the writing to the login_activity.txt file.
 * @author David Vaca
 * @since 1.0
 * */
public class LoginRecord {

    /**
     * Creates a file name "login_record.txt" if it does not exist, by using the try with statement.
     * <p>
     *     The file is the written with the login record, depending on the provided parameters.
     * </p>
     * @param userName User name used for the attempt to login.
     * @param success A boolean that represents whether or not the login  was successful.
     * @since 1.0
     */
    public static void writeRecord(String userName, boolean success){
        try (FileWriter fileWriter = new FileWriter("login_activity.txt", true)) {
            fileWriter.append("Username: ").append(userName).append("\n");
            fileWriter.append("Date: ").append(LocalDate.now().toString()).append("\n");
            fileWriter.append("Time: ").append(LocalTime.now().toString()).append("\n");
            fileWriter.append("Result: ").append(success ? "Login successful" : "Failed Login").append("\n\n");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
