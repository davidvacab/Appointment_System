package Controller;

import Model.Appointment;
import Model.BusinessDay;
import Model.TimeSlot;
import StaticControl.CurrentUser;
import StaticControl.AlertControl;
import StaticControl.DBControl;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the addUpdateAppointmentView.
 * <p>
 *     Adds or updates the appointment records on the database.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class AddUpdateAppointmentController implements Initializable {
    public Label mainLabel;
    public TextField appointmentIDTextField;
    public TextField titleTextField;
    public TextField descriptionTextField;
    public TextField locationTextField;
    public TextField typeTextField;
    public DatePicker datePicker;
    public ComboBox<String> contactComboBox;
    public ComboBox<LocalTime> startTimeComboBox;
    public ComboBox<LocalTime> endTimeComboBox;
    public ComboBox<Integer> customerIDComboBox;
    public ComboBox<Integer> userIDComboBox;
    public Button saveButton;
    public Button cancelButton;
    public AnchorPane anchorPane;
    public ProgressIndicator loadingAnimation;

    private Appointment appointment;
    private boolean update = false;
    private BusinessDay businessDay;
    private ResourceBundle appBundle;

    /**
     * Different methods which create listeners are called.
     * <p>
     *     The ResourceBundle is assigned to the {@link #appBundle} parameter which is used to localized the errors displayed.
     * </p>
     * @param location Location for the root object.
     * @param resources ResourceBundle provided on the MainScreenController class, used for localization of the GUI interface.
     * @see #checkFields()
     * @see #restrictDatePicker()
     * @see #populateStartTimeComboBox()
     * @see #populateEndTimeComboBox()
     * @see #populateContactComboBox()
     * @since 1.0
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appBundle = resources;
        checkFields();
        restrictDatePicker();
        populateStartTimeComboBox();
        populateEndTimeComboBox();
        populateContactComboBox();
    }

    /**
     * Sets the selected appointment and the update value to true.
     * <p>
     *     If a appointment record needs to be updated, this method is called in order to set the {@link #update} value to true,
     *     and all the TextFields, ComboBoxes and DatePicker are set to the customer parameters values.
     * </p>
     * @param appointment Selected appointment from the appointments table on the main screen view.
     * @since 1.0
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        update = true;
    }

    /**
     * Prevents the {@link #datePicker} from allowing the selection of past dates or the current date if it is past business hours.
     * @since 1.0
     */
    private void restrictDatePicker(){
        datePicker.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem (LocalDate item, boolean empty){
                super.updateItem(item, empty);
                Instant instant = Instant.parse("2021-01-01T03:00:00.0Z");
                LocalTime closingTime = instant.atZone(ZoneId.systemDefault()).toLocalTime();
                setDisable(item.isBefore(LocalDate.now()) || (LocalTime.now().isAfter(closingTime) && item.isEqual(LocalDate.now())));
            }
        });

    }

    /**
     * Adds a listener to the {@link #datePicker} in order to query only the appointments with the selected date from the database
     * and determine which spots should be available for selection on the {@link #startTimeComboBox}.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #loadingView(boolean)
     * @since 1.0
     */
    private void populateStartTimeComboBox(){
        datePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            startTimeComboBox.getSelectionModel().clearSelection();
            loadingView(true);
            ObservableList<LocalTime> startAvailableTimeSlotList = FXCollections.observableArrayList();
            businessDay = new BusinessDay();
            ArrayList<TimeSlot> timeSlotsList = businessDay.getList();
            DBControl.runQuery(() -> {
                String query;
                if (update){
                    query = "SELECT Start, End FROM appointments WHERE DATE(Start) = ? AND Appointment_ID != " + appointment.getAppointmentID() + ";";
                } else {
                    query = "SELECT Start, End FROM appointments WHERE DATE(Start) = ?;";
                }
                try (Connection conn = DBControl.getConnection();
                     PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setDate(1, Date.valueOf(newValue));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    for (int i = 0; i < timeSlotsList.size()-1; i++){
                        LocalTime time = timeSlotsList.get(i).getTime();
                        if (newValue.isAfter(LocalDate.now()) || time.isAfter(LocalTime.now())){
                            startAvailableTimeSlotList.add(timeSlotsList.get(i).getTime());
                        }
                    }
                    while(resultSet.next()) {
                        LocalTime startTime = resultSet.getTimestamp(1).toLocalDateTime().toLocalTime();
                        LocalTime endTime = resultSet.getTimestamp(2).toLocalDateTime().toLocalTime();
                        for (TimeSlot timeSlot : timeSlotsList){
                            if (timeSlot.getTime().isAfter(startTime.minusMinutes(1)) && timeSlot.getTime().isBefore(endTime) && timeSlot.isAvailable()){
                                startAvailableTimeSlotList.remove(timeSlot.getTime());
                                timeSlot.setAvailable(false);
                            }
                        }
                    }
                    Platform.runLater(() ->  {
                        startTimeComboBox.setItems(startAvailableTimeSlotList);
                        if (startAvailableTimeSlotList.isEmpty()){
                            startTimeComboBox.setPromptText(appBundle.getString("error.noSlots"));
                        }
                        if (update){
                            if (appointment.getStartDateTime().isAfter(LocalDateTime.now())){
                                startTimeComboBox.setValue(appointment.getStartDateTime().toLocalTime());
                                endTimeComboBox.setValue(appointment.getEndDateTime().toLocalTime());
                            }
                        }
                        loadingView(false);
                    });
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    loadingView(false);
                }
            });
        }));
    }

    /**
     * Adds a listener to the {@link #startTimeComboBox} in order to populate the {@link #endTimeComboBox} based on the selection.
     * <p>
     *     TimeSpots are added only if the TimeSpot is available and it does not overlap with other appointments.
     * </p>
     * @since 1.0
     */
    private void populateEndTimeComboBox(){
        startTimeComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            endTimeComboBox.getSelectionModel().clearSelection();
            ObservableList<LocalTime> endAvailableTimeSlotList = FXCollections.observableArrayList();
            ArrayList<TimeSlot> timeSlotsList = businessDay.getList();
            if (newValue != null){
                for (TimeSlot timeSlot: timeSlotsList){
                    LocalTime time = timeSlot.getTime();
                    if (time.isAfter(newValue)){
                        endAvailableTimeSlotList.add(timeSlot.getTime());
                        if (!timeSlot.isAvailable()){
                            break;
                        }
                    }
                }
            }
            endTimeComboBox.setItems(endAvailableTimeSlotList);
        }));
    }

    /**
     * Populates the {@link #contactComboBox} by querying all the available contacts from the database.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #populateCustomerIDComboBox()
     * @since 1.0
     */
    private void populateContactComboBox(){
        loadingView(true);
        ObservableList<String> contactsList = FXCollections.observableArrayList();
        DBControl.runQuery(() -> {
            String query = "SELECT Contact_Name FROM contacts GROUP BY Contact_Name;";
            try (Connection conn = DBControl.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while(rs.next()){
                    contactsList.add(rs.getString(1));
                }
                Platform.runLater(() -> {
                    contactComboBox.setItems(contactsList);
                    populateCustomerIDComboBox();
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                loadingView(false);
            }
        });
    }

    /**
     * Populates the {@link #customerIDComboBox} by querying all the available customerID's from the database.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #populateUserIDComboBox()
     * @since 1.0
     */
    private void populateCustomerIDComboBox(){
        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
        DBControl.runQuery(() -> {
            String query = "SELECT Customer_ID FROM customers ORDER BY Customer_ID;";
            try (Connection conn = DBControl.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while(rs.next()){
                    customerIDList.add(rs.getInt(1));
                }
                Platform.runLater(() -> {
                    customerIDComboBox.setItems(customerIDList);
                    populateUserIDComboBox();
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                loadingView(false);
            }
        });
    }

    /**
     * Populates the {@link #userIDComboBox} by querying all the available userID's from the database.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @since 1.0
     */
    private void populateUserIDComboBox(){
        ObservableList<Integer> userIDList = FXCollections.observableArrayList();
        DBControl.runQuery(() -> {
            String query = "SELECT User_ID FROM users ORDER BY User_ID;";
            try (Connection conn = DBControl.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while(rs.next()){
                    userIDList.add(rs.getInt(1));
                }
                Platform.runLater(() -> {
                    userIDComboBox.setItems(userIDList);
                    if (update) {
                        appointmentIDTextField.setText(Integer.toString(appointment.getAppointmentID()));
                        titleTextField.setText(appointment.getTitle());
                        descriptionTextField.setText(appointment.getDescription());
                        locationTextField.setText(appointment.getLocation());
                        contactComboBox.setValue(appointment.getContact());
                        typeTextField.setText(appointment.getType());
                        if (appointment.getStartDateTime().toLocalDate().isAfter(LocalDate.now().minusDays(1))){
                            datePicker.setValue(appointment.getStartDateTime().toLocalDate());
                        }
                        customerIDComboBox.setValue(appointment.getCustomerID());
                        userIDComboBox.setValue(appointment.getUserID());
                    }
                    loadingView(false);
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                loadingView(false);
            }
        });
    }

    /**
     * Passes the focus to the {@link #descriptionTextField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the title on the {@link #titleTextField}.
     * </p>
     * @since 1.0
     */
    public void onTitleTextField( ) {
        descriptionTextField.requestFocus();
    }

    /**
     * Passes the focus to the {@link #locationTextField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the description on the {@link #descriptionTextField}.
     * </p>
     * @since 1.0
     */
    public void onDescriptionTextField( ) {
        locationTextField.requestFocus();
    }

    /**
     * Passes the focus to the {@link #typeTextField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the location on the {@link #locationTextField}.
     * </p>
     * @since 1.0
     */
    public void onLocationTextField( ) {
        typeTextField.requestFocus();
    }

    /**
     * Passes the focus to the {@link #datePicker}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the type on the {@link #typeTextField}.
     * </p>
     * @since 1.0
     */
    public void onTypeTextField( ) {
        datePicker.requestFocus();
    }

    /**
     * Gets all the values from the TextFields and ComboBoxes and inserts or updates a record on the database depending on the {@link #update} value.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #loadingView(boolean)
     * @since 1.0
     */
    public void onSaveButton( ) {
        loadingView(true);
        DBControl.runQuery(() -> {
            String query;
            if (update){
                query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ? ," +
                        "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = (SELECT Contact_ID FROM contacts WHERE Contact_Name = ?)" +
                        "WHERE Appointment_ID = " + appointment.getAppointmentID() +";";
            } else {
                query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Created_By, Customer_ID, User_ID, Contact_ID) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,(SELECT Contact_ID FROM contacts WHERE Contact_Name = ?));";
            }
            try (Connection conn = DBControl.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, titleTextField.getText());
                preparedStatement.setString(2, descriptionTextField.getText());
                preparedStatement.setString(3, locationTextField.getText());
                preparedStatement.setString(4, typeTextField.getText());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue(), startTimeComboBox.getValue())));
                preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue(), endTimeComboBox.getValue())));
                preparedStatement.setString(7, CurrentUser.getUserName());
                preparedStatement.setInt(8, customerIDComboBox.getValue());
                preparedStatement.setInt(9,userIDComboBox.getValue());
                preparedStatement.setString(10, contactComboBox.getValue());
                int lines = preparedStatement.executeUpdate();
                Platform.runLater(() -> {
                    if (lines > 0){
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                    } else {
                        loadingView(false);
                    }
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                loadingView(false);
            }
        });
    }

    /**
     * A confirmation alert is shown, if the result is OK the program will close; otherwise the window will stay opened.
     * <p>
     *     Called when the {@link #cancelButton} is pressed.
     * </p>
     * @since 1.0
     */
    public void onCancelButton() {
        Optional<ButtonType> result = AlertControl.askConfirmation(appBundle.getString("button.cancel"), appBundle.getString("error.cancel"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * A confirmation alert is shown, if the result is OK the program will close; otherwise the window will stay opened.
     * <p>
     *     Called when the close button is pressed on the top of the window.
     * </p>
     * @param event closing window event
     * @since 1.0
     */
    public void onCloseButton(WindowEvent event) {
        Optional<ButtonType> result = AlertControl.askConfirmation(appBundle.getString("button.cancel"), appBundle.getString("error.cancel"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else {
            event.consume();
        }
    }

    /**
     * Prevents the user from trying to summit multiple login attempts by disabling the GUI interface and showing a loading animation
     * until the processes sent to the database are done processing.
     * @param state A boolean that sets the {@link #anchorPane} and the {@link #loadingAnimation}.
     * @since 1.0
     */
    private void loadingView(boolean state){
        anchorPane.setDisable(state);
        loadingAnimation.setVisible(state);
    }

    /**
     * Binds the {@link #saveButton} to the all of the TextFields, the {@link #startTimeComboBox} to the {@link #datePicker} and the {@link #endTimeComboBox} to the {@link #startTimeComboBox}.
     * <p>
     *     Prevents the program from throwing an exception if ether TextField is empty and the {@link #saveButton} is pressed.
     * </p>
     * @since 1.0
     */
    private void checkFields(){
        BooleanBinding checkFields = titleTextField.textProperty().isEmpty()
                .or(descriptionTextField.textProperty().isEmpty()
                        .or(locationTextField.textProperty().isEmpty()
                                .or(typeTextField.textProperty().isEmpty()
                                        .or(contactComboBox.valueProperty().isNull())
                                        .or(datePicker.valueProperty().isNull())
                                        .or(startTimeComboBox.valueProperty().isNull()
                                                .or(endTimeComboBox.valueProperty().isNull())
                                                .or(customerIDComboBox.valueProperty().isNull())
                                                .or(userIDComboBox.valueProperty().isNull())
                                        ))));
        saveButton.disableProperty().bind(checkFields);
        BooleanBinding checkDatePicker = datePicker.valueProperty().isNull();
        startTimeComboBox.disableProperty().bind(checkDatePicker);
        BooleanBinding checkStartTimeComboBox = startTimeComboBox.valueProperty().isNull();
        endTimeComboBox.disableProperty().bind(checkStartTimeComboBox);
    }
}
