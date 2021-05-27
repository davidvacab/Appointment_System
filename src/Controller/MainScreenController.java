package Controller;

import Model.*;
import StaticControl.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Controller for the MainScreenView.
 * <p>
 *     Displays all the records and reports for the customers and the appointments.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class MainScreenController implements Initializable {
    public TabPane mainTabPane;
    public Tab appointmentsTab;
    public Tab customersTab;
    public Tab reportsTab;

    public ProgressIndicator appLoadingAnimation;
    public ProgressIndicator customersProgressIndicator;
    public ProgressIndicator reportsProgressIndicator;

    public TabPane appointmentsTabPane;

    public AnchorPane appointmentsAnchor;
    public AnchorPane customersAnchor;
    public AnchorPane reportsAnchor;

    public TextField appointmentsSearchBar;
    public TableView<Appointment> appointmentsTable;
    public TableColumn<Appointment, Integer> appointmentIDColumn;
    public TableColumn<Appointment, String> appointmentTitleColumn;
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    public TableColumn<Appointment, String> appointmentLocationColumn;
    public TableColumn<Appointment, String> appointmentContactColumn;
    public TableColumn<Appointment, String> appointmentTypeColumn;
    public TableColumn<Appointment, LocalDateTime> appointmentStartDateTimeColumn;
    public TableColumn<Appointment, LocalDateTime> appointmentEndDateTimeColumn;
    public TableColumn<Appointment, Integer> appointmentCustomerIDColumn;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;

    public TextField customersSearchBar;
    public TableView<Customer> customersTable;
    public TableColumn<Customer,Integer> customerIDColumn;
    public TableColumn<Customer,String> customerNameColumn;
    public TableColumn<Customer,String> customerAddressColumn;
    public TableColumn<Customer,String> customerPostalCodeColumn;
    public TableColumn<Customer,String> customerPhoneColumn;
    public TableColumn<Customer,Integer> customerDivisionColumn;
    public TableColumn<Customer, String> customerCountryColumn;
    public Button updateCustomerButton;
    public Button deleteCustomerButton;

    public TableView<WeekView> weekViewTable;
    public TableColumn<WeekView, TimeSlot> weekTimeColumn;
    public TableColumn<WeekView, String> weekSundayColumn;
    public TableColumn<WeekView, String> weekMondayColumn;
    public TableColumn<WeekView, String> weekTuesdayColumn;
    public TableColumn<WeekView, String> weekWednesdayColumn;
    public TableColumn<WeekView, String> weekThursdayColumn;
    public TableColumn<WeekView, String> weekFridayColumn;
    public TableColumn<WeekView, String> weekSaturdayColumn;
    public Label weekViewMonthName;

    public TableView<MonthView> monthViewTable;
    public TableColumn<MonthView, String> monthSundayColumn;
    public TableColumn<MonthView, String> monthMondayColumn;
    public TableColumn<MonthView, String> monthTuesdayColumn;
    public TableColumn<MonthView, String> monthWednesdayColumn;
    public TableColumn<MonthView, String> monthThursdayColumn;
    public TableColumn<MonthView, String> monthFridayColumn;
    public TableColumn<MonthView, String> monthSaturdayColumn;
    public Label monthViewMonthName;

    public TableView<YearView> typeReportTable;
    public TableColumn<YearView, String> typeColumn;
    public TableColumn<YearView, Integer> typeJanuaryColumn;
    public TableColumn<YearView, Integer> typeFebruaryColumn;
    public TableColumn<YearView, Integer> typeMarchColumn;
    public TableColumn<YearView, Integer> typeAprilColumn;
    public TableColumn<YearView, Integer> typeMayColumn;
    public TableColumn<YearView, Integer> typeJuneColumn;
    public TableColumn<YearView, Integer> typeJulyColumn;
    public TableColumn<YearView, Integer> typeAugustColumn;
    public TableColumn<YearView, Integer> typeSeptemberColumn;
    public TableColumn<YearView, Integer> typeOctoberColumn;
    public TableColumn<YearView, Integer> typeNovemberColumn;
    public TableColumn<YearView, Integer> typeDecemberColumn;
    public Label typeYearLabel;

    public TableView<WeekView> contactReportTable;
    public TableColumn<WeekView, LocalTime> contactTimeColumn;
    public TableColumn<WeekView, String> contactSundayColumn;
    public TableColumn<WeekView, String> contactMondayColumn;
    public TableColumn<WeekView, String> contactTuesdayColumn;
    public TableColumn<WeekView, String> contactWednesdayColumn;
    public TableColumn<WeekView, String> contactThursdayColumn;
    public TableColumn<WeekView, String> contactFridayColumn;
    public TableColumn<WeekView, String> contactSaturdayColumn;
    public ComboBox<String> contactComboBox;
    public Label contactLabel;
    public Label contactReportMonthLabel;

    public TableView<YearView> locationReportTable;
    public TableColumn<YearView, String> locationColumn;
    public TableColumn<YearView, Integer> locationJanuaryColumn;
    public TableColumn<YearView, Integer> locationFebruaryColumn;
    public TableColumn<YearView, Integer> locationMarchColumn;
    public TableColumn<YearView, Integer> locationAprilColumn;
    public TableColumn<YearView, Integer> locationMayColumn;
    public TableColumn<YearView, Integer> locationJuneColumn;
    public TableColumn<YearView, Integer> locationJulyColumn;
    public TableColumn<YearView, Integer> locationAugustColumn;
    public TableColumn<YearView, Integer> locationSeptemberColumn;
    public TableColumn<YearView, Integer> locationOctoberColumn;
    public TableColumn<YearView, Integer> locationNovemberColumn;
    public TableColumn<YearView, Integer> locationDecemberColumn;
    public Label locationYearLabel;

    private ObservableList<Customer> customersList;
    private ObservableList<Appointment> appointmentsList;
    private ObservableList<String> contactsList;

    private LocalDate weekViewStartDate;
    private LocalDate firstDayOfMonth;
    private LocalDate typeReportStartDate;
    private LocalDate contactReportStartDate;
    private LocalDate locationReportStartDate;

    private String selectedContact = "";
    private ResourceBundle mainBundle;
    private boolean welcome = true;

    /**
     * Only the {@link #appointmentsTab} tables are populated to save time.
     * <p>
     *     The ResourceBundle is assigned to the {@link #mainBundle} parameter which is used to localized the errors displayed.
     * </p>
     * @param location Location of the root object.
     * @param resources ResourceBundle provided in the LoginController class, used for localization of the GUI interface.
     * @see #setCellRestrictions()
     * @see #listeners()
     * @see #populateAppointmentsTable()
     * @since 1.0
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainBundle = resources;
        setCellRestrictions();
        listeners();
        populateAppointmentsTable();
    }

    /**
     * Displays an Alert window only after login containing information about any appointment starting withing 15 minutes after the login.
     * @since 1.0
     */
    private void appointmentComingSoon(){
        if (welcome){
            welcome = false;
            boolean noAppointments = true;
            for (Appointment appointment : appointmentsList) {
                LocalDateTime endRange = appointment.getStartDateTime().plusMinutes(1);
                LocalDateTime startRange = endRange.minusMinutes(16);
                if (appointment.getUserID() == CurrentUser.getUserID() &&
                        LocalDateTime.now().isAfter(startRange) &&
                        LocalDateTime.now().isBefore(endRange)) {
                    noAppointments = false;
                    int id = appointment.getAppointmentID();
                    LocalDate date = appointment.getStartDateTime().toLocalDate();
                    LocalTime time = appointment.getStartDateTime().toLocalTime();
                    int minutes = (int) Duration.between(LocalDateTime.now(), appointment.getStartDateTime()).toMinutes();
                    AlertControl.errorMessage(mainBundle.getString("error.appointmentComing.title"), mainBundle.getString("error.appointmentID") + id + "\n" +
                            "Date: " + date + "\n" +
                            mainBundle.getString("error.appointmentComing.time") + time + "\n" +
                            mainBundle.getString("error.appointmentComing.starting") + minutes + " minutes.");
                }
            }
            if (noAppointments) {
                AlertControl.errorMessage(mainBundle.getString("error.noAppointments.title"), mainBundle.getString("error.noAppointments.content"));
            }
        }
    }

    /**
     * Binds the update and delete Buttons from the {@link #customersTable} and {@link #appointmentsTable} with their respective selection.
     * <p>
     *     Listeners are added to the {@link #mainTabPane} and the {@link #contactComboBox} in order to populate their respective tables and members.<br>
     *     A lambda expressions are used in the listeners so the values can be automatically updated.
     * </p>
     * @see #populateAppointmentsTable()
     * @see #populateCustomersTable()
     * @see #populateTypeReport(LocalDate)
     * @see #populateContactReport(LocalDate)
     * @see #populateLocationReport(LocalDate)
     * @see #populateContactComboBox()
     * @since 1.0
     */
    private void listeners(){
        BooleanBinding checkAppointmentUpdateDelete = appointmentsTable.getSelectionModel().selectedItemProperty().isNull();
        updateAppointmentButton.disableProperty().bind(checkAppointmentUpdateDelete);
        deleteAppointmentButton.disableProperty().bind(checkAppointmentUpdateDelete);

        BooleanBinding checkCustomerUpdateDelete = customersTable.getSelectionModel().selectedItemProperty().isNull();
        updateCustomerButton.disableProperty().bind(checkCustomerUpdateDelete);
        deleteCustomerButton.disableProperty().bind(checkCustomerUpdateDelete);

        mainTabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (appointmentsTab.equals(newValue) && appointmentsList == null){
                populateAppointmentsTable();
            }
            if (customersTab.equals(newValue) && customersList == null){
                populateCustomersTable();
            }
            if (reportsTab.equals(newValue) && contactsList == null){
                reportsAnchor.setDisable(true);
                reportsProgressIndicator.setVisible(true);
                populateTypeReport(LocalDate.now());
                populateLocationReport(LocalDate.now());
                populateContactComboBox();
            }
        }));

        contactComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            selectedContact = newValue;
            populateContactReport(contactReportStartDate);
        }));
    }

    /**
     * Queries the appointment information from the database and uses it to populate the {@link #appointmentsList} and {@link #appointmentsTable} by creating Appointment objects.
     * <p>
     *     A loading animation is shown to prevent the user from sending more queries.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #searchAppointments()
     * @see #populateMonthView(LocalDate)
     * @see #populateWeekView(LocalDate)
     * @see #appointmentComingSoon()
     * @since 1.0
     */
    private void populateAppointmentsTable(){
        appointmentsAnchor.setDisable(true);
        appLoadingAnimation.setVisible(true);
        appointmentsList = FXCollections.observableArrayList();
        DBControl.runQuery(() -> {
            String query = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID " +
                    "FROM appointments a INNER JOIN contacts c ON a.Contact_ID = c.Contact_ID ORDER BY Appointment_ID;";
            try (Connection connection = DBControl.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {
                while(rs.next()){
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentID(rs.getInt(1));
                    appointment.setTitle(rs.getString(2));
                    appointment.setDescription(rs.getString(3));
                    appointment.setLocation(rs.getString(4));
                    appointment.setContact(rs.getString(5));
                    appointment.setType(rs.getString(6));
                    appointment.setStartDateTime(rs.getTimestamp(7).toLocalDateTime());
                    appointment.setEndDateTime(rs.getTimestamp(8).toLocalDateTime());
                    appointment.setCustomerID(rs.getInt(9));
                    appointment.setUserID(rs.getInt(10));
                    appointmentsList.add(appointment);
                }
                Platform.runLater(() -> {
                    appointmentsAnchor.setDisable(false);
                    appLoadingAnimation.setVisible(false);
                    searchAppointments();
                    populateMonthView(LocalDate.now());
                    populateWeekView(LocalDate.now());
                    appointmentComingSoon();
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                appointmentsAnchor.setDisable(false);
                appLoadingAnimation.setVisible(false);
            }
        });
    }

    /**
     * Populates the appointments by month table using the appointments queried at {@link #populateAppointmentsTable()}.
     * <p>
     *     The first day of the week {@link #getFirstDayOfWeek(LocalDate)} is determine from the provided LocalDate.<br>
     *     The {@link #firstDayOfMonth} is assigned so it can be used on the the previous {@link #onMonthPreviousButton()} and next {@link #onMonthNextButton()} buttons methods.<br>
     *     The month and year are set on the respective label.<br>
     *     Between 4 and 6 MonthView objects are created depending on the numbers of week that there are on the month using the monthStartDate and adding a week every time.<br>
     *     The objects are then added to the monthViewList and set on the the monthViewTable.
     * </p>
     * @param localDate A LocalDate used to determine the first day of the month
     * @see #getFirstDayOfWeek(LocalDate)
     * @see #onMonthPreviousButton()
     * @see #onMonthNextButton()
     * @since 1.0
     */
    private void populateMonthView(LocalDate localDate){
        ObservableList<MonthView> monthViewList = FXCollections.observableArrayList();
        firstDayOfMonth = localDate.withDayOfMonth(1);
        LocalDate startDate = getFirstDayOfWeek(firstDayOfMonth);

        String month = firstDayOfMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int year = firstDayOfMonth.getYear();
        monthViewMonthName.setText(month + " " + year);

        do {
            MonthView monthView = new MonthView(startDate);
            for (Appointment appointment: appointmentsList){
                LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
                if (appointmentDate.isAfter(startDate.minusDays(1)) && appointmentDate.isBefore(startDate.plusDays(7))){
                    monthView.addAppointment(appointment);
                }
            }
            startDate = startDate.plusWeeks(1);
            monthViewList.add(monthView);
        } while (startDate.getMonth().equals(firstDayOfMonth.getMonth()));

        if (monthViewList.size() == 4){
            monthViewTable.setFixedCellSize(140);
        } else if (monthViewList.size() == 5) {
            monthViewTable.setFixedCellSize(118);
        } else {
            monthViewTable.setFixedCellSize(100);
        }
        monthViewTable.setItems(monthViewList);
    }

    /**
     * Populates the appointments by week table using the appointments queried at {@link #populateAppointmentsTable()}.
     * <p>
     *     The first day of the week {@link #getFirstDayOfWeek(LocalDate)} is determine from the provided local date.<br>
     *     The {@link #weekViewStartDate} is assigned so it can be used on the the previous {@link #onWeekPreviousButton()} and next {@link #onWeekNextButton()} buttons methods.<br>
     *     The month(s) and year are get and set to the respective label.<br>
     *     Every column title is set depending on the day it represents.<br>
     *     A WeekView object is created for every timeSlot in the timeSlot list.<br>
     *     The objects are then added to the weekViewList and set on the the weekViewTable.
     * </p>
     * @param localDate A LocalDate used to determine the first day of the week..
     * @see #getFirstDayOfWeek(LocalDate)
     * @see #onWeekPreviousButton()
     * @see #onWeekNextButton()
     * @since 1.0
     */
    private void populateWeekView(LocalDate localDate){
        ObservableList<WeekView> weekViewList = FXCollections.observableArrayList();
        weekViewStartDate = getFirstDayOfWeek(localDate);

        String firstMonth = weekViewStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase();
        String lastMonth = weekViewStartDate.plusDays(6).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase();
        int year = weekViewStartDate.getYear();

        if (weekViewStartDate.getMonth().equals(weekViewStartDate.plusDays(6).getMonth())){
            weekViewMonthName.setText(firstMonth + " " + year);
        } else {
            weekViewMonthName.setText(firstMonth + " - " + lastMonth + " " + year);
        }

        weekSundayColumn.setText(mainBundle.getString("column.sunday") + weekViewStartDate.getDayOfMonth());
        weekMondayColumn.setText(mainBundle.getString("column.monday") + weekViewStartDate.plusDays(1).getDayOfMonth());
        weekTuesdayColumn.setText(mainBundle.getString("column.tuesday") + weekViewStartDate.plusDays(2).getDayOfMonth());
        weekWednesdayColumn.setText(mainBundle.getString("column.wednesday") + weekViewStartDate.plusDays(3).getDayOfMonth());
        weekThursdayColumn.setText(mainBundle.getString("column.thursday") + weekViewStartDate.plusDays(4).getDayOfMonth());
        weekFridayColumn.setText(mainBundle.getString("column.friday") + weekViewStartDate.plusDays(5).getDayOfMonth());
        weekSaturdayColumn.setText(mainBundle.getString("column.saturday") + weekViewStartDate.plusDays(6).getDayOfMonth());

        BusinessDay businessDay = new BusinessDay();
        for (TimeSlot timeSlot: businessDay.getList()){
            WeekView weekView = new WeekView(timeSlot.getTime());
            for (Appointment appointment: appointmentsList){
                LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
                LocalTime appointmentTime = appointment.getStartDateTime().toLocalTime();
                if (appointmentDate.isAfter(weekViewStartDate.minusDays(1)) &&
                        appointmentDate.isBefore(weekViewStartDate.plusDays(7)) &&
                        appointmentTime.equals(timeSlot.getTime())) {
                    weekView.addAppointment(appointment);
                }
            }
            weekViewList.add(weekView);
        }
        weekViewTable.setItems(weekViewList);
    }

    /**
     * Populates the customers table by sending a query to the database and creating Customer objects for every record found.
     * <p>
     *     A loading animation is shown to prevent the user from sending more queries.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #searchCustomers()
     * @since 1.0
     */
    private void populateCustomersTable(){
        customersAnchor.setDisable(true);
        customersProgressIndicator.setVisible(true);
        customersList = FXCollections.observableArrayList();
        DBControl.runQuery(() -> {
            String query = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division, Country " +
                    "FROM customers, first_level_divisions, countries " +
                    "WHERE customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.Country_ID = countries.Country_ID ORDER BY Customer_ID;";
            try (Connection connection = DBControl.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {
                while(rs.next()){
                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getInt(1));
                    customer.setName(rs.getString(2));
                    customer.setAddress(rs.getString(3));
                    customer.setPostalCode(rs.getString(4));
                    customer.setPhone(rs.getString(5));
                    customer.setDivision(rs.getString(6));
                    customer.setCountry(rs.getString(7));
                    customersList.add(customer);
                }
                Platform.runLater(() -> {
                    customersAnchor.setDisable(false);
                    customersProgressIndicator.setVisible(false);
                    searchCustomers();
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                customersAnchor.setDisable(false);
                customersProgressIndicator.setVisible(false);
            }
        });
    }

    /**
     * Populates the appointments by type table using the appointments queried at {@link #populateAppointmentsTable()}.
     * <p>
     *     The first day of the week {@link #getFirstDayOfWeek(LocalDate)} is determine from the provided local date.<br>
     *     The {@link #typeReportStartDate} is assigned so it can be used on the the previous {@link #onTypePreviousButton()} and next {@link #onTypeNextButton()} buttons methods.<br>
     *     The year is get and set to the respective label.<br>
     *     A list of different types is created from the {@link #appointmentsList}.<br>
     *     A YearView object is created for every type in the typeList list.<br>
     *     The objects are then added to the typeReportList and set on the the typReportTable.
     * </p>
     * @param localDate A LocalDate used to determine the first day of the year.
     * @see #getFirstDayOfWeek(LocalDate)
     * @see #onTypeNextButton()
     * @see #onTypePreviousButton()
     * @since 1.0
     */
    private void populateTypeReport(LocalDate localDate){
        ObservableList<YearView> typeReportList = FXCollections.observableArrayList();
        typeReportStartDate = localDate.withDayOfYear(1);
        LocalDate lastDayOfYear = localDate.withDayOfYear(365);

        typeYearLabel.setText(Integer.toString(localDate.getYear()));
        ArrayList<String> typesList = new ArrayList<>();

        for (Appointment appointment: appointmentsList){
            String type = appointment.getType();
            if (!typesList.contains(type)){
                typesList.add(type);
            }
        }
        for (String type: typesList){
            YearView yearView = new YearView(type, typeReportStartDate);
            for (Appointment appointment: appointmentsList){
                LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
                if (appointmentDate.isAfter(typeReportStartDate.minusDays(1)) &&
                        appointmentDate.isBefore(lastDayOfYear.plusDays(1)) &&
                        appointment.getType().matches(type)){
                    yearView.addAppointment(appointment);
                }
            }
            typeReportList.add(yearView);
        }
        typeReportTable.setItems(typeReportList);
    }

    /**
     * Populates the Contact Schedules table using the appointments queried at {@link #populateAppointmentsTable()} and the selection of the {@link #contactComboBox}.
     * <p>
     *     The first day of the week {@link #getFirstDayOfWeek(LocalDate)} is determine from the provided local date.<br>
     *     The {@link #contactReportStartDate} is assigned so it can be used on the the previous {@link #onContactPreviousButton()} and next {@link #onContactNextButton()} buttons methods.<br>
     *     The month(s) and year are get and set to the respective label.<br>
     *     Every column title is set depending on the day it represents.<br>
     *     A WeekView object is created for every timeSlot in the timeSlotList.<br>
     *     The objects are then added to the contactReportList and set on the the contactReportTable.
     * </p>
     * @param localDate A LocalDate used to determine the first day of the week.
     * @see #contactReportStartDate
     * @see #getFirstDayOfWeek(LocalDate)
     * @see #onContactNextButton()
     * @see #onContactPreviousButton()
     * @since 1.0
     */
    private void populateContactReport(LocalDate localDate){
        ObservableList<WeekView> contactReportList = FXCollections.observableArrayList();
        contactReportStartDate = getFirstDayOfWeek(localDate);

        String firstMonth = contactReportStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase();
        String lastMonth = contactReportStartDate.plusDays(6).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase();
        int year = contactReportStartDate.getYear();

        if (contactReportStartDate.getMonth().equals(contactReportStartDate.plusDays(6).getMonth())){
            contactReportMonthLabel.setText(firstMonth + " " + year);
        } else {
            contactReportMonthLabel.setText(firstMonth + " - " + lastMonth + " " + year);
        }

        contactSundayColumn.setText(mainBundle.getString("column.sunday") + contactReportStartDate.getDayOfMonth());
        contactMondayColumn.setText(mainBundle.getString("column.monday") + contactReportStartDate.plusDays(1).getDayOfMonth());
        contactTuesdayColumn.setText(mainBundle.getString("column.tuesday") + contactReportStartDate.plusDays(2).getDayOfMonth());
        contactWednesdayColumn.setText(mainBundle.getString("column.wednesday") + contactReportStartDate.plusDays(3).getDayOfMonth());
        contactThursdayColumn.setText(mainBundle.getString("column.thursday") + contactReportStartDate.plusDays(4).getDayOfMonth());
        contactFridayColumn.setText(mainBundle.getString("column.friday") + contactReportStartDate.plusDays(5).getDayOfMonth());
        contactSaturdayColumn.setText(mainBundle.getString("column.saturday") + contactReportStartDate.plusDays(6).getDayOfMonth());

        BusinessDay businessDay = new BusinessDay();
        for (TimeSlot timeSlot: businessDay.getList()){
            WeekView weekView = new WeekView(timeSlot.getTime());
            for (Appointment appointment: appointmentsList){
                LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
                LocalTime appointmentTime = appointment.getStartDateTime().toLocalTime();
                if (appointmentDate.isAfter(contactReportStartDate.minusDays(1)) &&
                        appointmentDate.isBefore(contactReportStartDate.plusDays(7)) &&
                        appointmentTime.equals(timeSlot.getTime()) &&
                        selectedContact != null &&
                        selectedContact.matches(appointment.getContact())){
                    weekView.addAppointment(appointment);
                }
            }
            contactReportList.add(weekView);
        }
        contactReportTable.setItems(contactReportList);
    }

    /**
     * Populates the {@link #contactComboBox} used on the {@link #reportsTab}.
     * <p>
     *     After querying the information from the database the list of contacts is set to the {@link #contactComboBox}.<br>
     *     A loading animation is shown to prevent the user from sending more queries.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @since 1.0
     */
    private void populateContactComboBox(){
        contactsList = FXCollections.observableArrayList();
        contactComboBox.getSelectionModel().clearSelection();
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
                    populateContactReport(LocalDate.now());
                    reportsAnchor.setDisable(false);
                    reportsProgressIndicator.setVisible(false);
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                reportsAnchor.setDisable(false);
                reportsProgressIndicator.setVisible(false);
            }
        });
    }

    /**
     * Populates the appointments by location table using the appointments queried at {@link #populateAppointmentsTable()}.
     * <p>
     *     The first day of the week {@link #getFirstDayOfWeek(LocalDate)} is determine from the provided local date.<br>
     *     The {@link #typeReportStartDate} is assigned so it can be used on the the previous {@link #onLocationPreviousButton()} and next {@link #onLocationNextButton()} buttons methods.<br>
     *     The year is get and set to the respective label.<br>
     *     A list of different locations is created from the {@link #appointmentsList}.<br>
     *     A YearView object is created for every location in the locationList list.<br>
     *     The objects are then added to the typeReportList and set on the the typReportTable.
     * </p>
     * @param localDate LocalDate used to determine the first day of the year.
     * @see #typeReportStartDate
     * @see #getFirstDayOfWeek(LocalDate)
     * @see #onLocationNextButton()
     * @see #onLocationPreviousButton()
     * @since 1.0
     */
    private void populateLocationReport(LocalDate localDate){
        ObservableList<YearView> locationReportList = FXCollections.observableArrayList();
        locationReportStartDate = localDate.withDayOfYear(1);
        LocalDate lastDayOfYear = localDate.withDayOfYear(365);

        locationYearLabel.setText(Integer.toString(localDate.getYear()));
        ArrayList<String> locationList = new ArrayList<>();

        for (Appointment appointment: appointmentsList){
            String location = appointment.getLocation();
            if (!locationList.contains(location)){
                locationList.add(location);
            }
        }
        for (String location: locationList){
            YearView yearView = new YearView(location, locationReportStartDate);
            for (Appointment appointment: appointmentsList){
                LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
                if (appointmentDate.isAfter(locationReportStartDate.minusDays(1)) &&
                        appointmentDate.isBefore(lastDayOfYear.plusDays(1)) &&
                        appointment.getLocation().matches(location)){
                    yearView.addAppointment(appointment);
                }
            }
            locationReportList.add(yearView);
        }
        locationReportTable.setItems(locationReportList);
    }

    /**
     * The {@link #addUpdateAppointment(boolean)} method is called and the boolean value is set to false since no update to a record is performed.
     * <p>
     *     Called when the add button from the appointments table is pressed.
     * </p>
     * @see #addUpdateAppointment(boolean)
     * @since 1.0
     */
    public void onAddAppointmentButton( ) {
        addUpdateAppointment(false);
    }

    /**
     * The {@link #addUpdateAppointment(boolean)} method is called and the boolean value is set to true since an update to a record is performed.
     * <p>
     *     Called when the update button from the appointments table is pressed.
     * </p>
     * @see #addUpdateAppointment(boolean)
     * @since 1.0
     */
    public void onUpdateAppointmentButton( ) {
        addUpdateAppointment(true);
    }

    /**
     * Opens the addUpdateAppointment window, using the update value to determine which controller to use.
     * <p>
     *     After the window is closed the {@link #populateAppointmentsTable()} will be called to show the updated information from the database.
     * </p>
     * @param update A boolean used to determine which controller and titles will be used.
     * @see #populateAppointmentsTable()
     * @since 1.0
     */
    private void addUpdateAppointment(boolean update){
        Appointment selectedAppointment = null;
        if (update){
            selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        }
        try {
            ResourceBundle appBundle = ResourceBundle.getBundle("Bundles.AddUpdateAppointment", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddUpdateAppointmentView.fxml"), appBundle);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(appBundle.getString(update ? "stage.title.update" : "stage.title.add"));
            AddUpdateAppointmentController controller = loader.getController();
            if (update) {
                controller.setAppointment(selectedAppointment);
            }
            controller.mainLabel.setText(appBundle.getString(update ? "stage.title.update" : "stage.title.add"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(controller::onCloseButton);
            stage.showAndWait();
            Platform.runLater(this::populateAppointmentsTable);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * An Alert window is shown asking for the user confirmation before deleting the appointment record. If the answer is OK,
     * a query is send using a Runnable and Alert window is shown depending on the result of the query.
     * <p>
     *     This method is called when the delete button is pressed on the appointments table.<br>
     *     The {@link #populateAppointmentsTable()} is called in order to display all updates to the appointments records.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #populateAppointmentsTable()
     * @since 1.0
     */
    public void onDeleteAppointmentButton( ) {
        Optional<ButtonType> result = AlertControl.askConfirmation(
                mainBundle.getString("confirmation.deleteAppointment.title"),
                mainBundle.getString("confirmation.deleteAppointment.content"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
            int appointmentID = selectedAppointment.getAppointmentID();
            String type = selectedAppointment.getType();
            DBControl.runQuery(() -> {
                String query = "DELETE FROM appointments WHERE Appointment_ID = ?;";
                try (Connection connection = DBControl.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, appointmentID);
                    int lines = preparedStatement.executeUpdate();
                    Platform.runLater(() -> {
                        if (lines > 0){
                            AlertControl.errorMessage(mainBundle.getString("error.success.title"),
                                    mainBundle.getString("error.appointmentID") + appointmentID + "\n" +
                                            mainBundle.getString("error.type") + type + "\n" +
                                            mainBundle.getString("error.deleteSuccess"));
                        }
                        populateAppointmentsTable();
                    });
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        }
    }

    /**
     * The {@link #addUpdateCustomer(boolean)} method is called and the boolean value is set to false since no update to a record is performed.
     * <p>
     *     Called when the add button from the customer table is pressed.
     * </p>
     * @see #addUpdateCustomer(boolean)
     * @since 1.0
     */
    public void onAddCustomerButton( ) {
        addUpdateCustomer(false);
    }

    /**
     * The {@link #addUpdateCustomer(boolean)} method is called and the boolean value is set to true since an update to a record is performed.
     * <p>
     *     Called when the update button from the customer table is pressed.
     * </p>
     * @see #addUpdateCustomer(boolean)
     * @since 1.0
     */
    public void onUpdateCustomerButton() {
        addUpdateCustomer(true);
    }

    /**
     * Opens the addUpdateCustomer window, using the update value to determine which controller to use.
     * <p>
     *     After the window is closed the {@link #populateCustomersTable()} will be called to show the updated information from the database.
     * </p>
     * @param update A boolean used to determine which controller and titles will be used.
     * @see #populateCustomersTable()
     * @since 1.0
     */
    private void addUpdateCustomer(boolean update){
        Customer selectedCustomer = null;
        if (update){
            selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        }
        try {
            ResourceBundle cusBundle = ResourceBundle.getBundle("Bundles.AddUpdateCustomer", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddUpdateCustomerView.fxml"), cusBundle);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(cusBundle.getString(update ? "stage.title.update" : "stage.title.add"));
            AddUpdateCustomerController controller = loader.getController();
            if (update){
                controller.setCustomer(selectedCustomer);
            }
            controller.mainLabel.setText(cusBundle.getString(update ? "stage.title.update" : "stage.title.add"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(controller::onCloseButton);
            stage.showAndWait();
            Platform.runLater(this::populateCustomersTable);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * An Alert window is shown asking for the user confirmation before deleting the customer record. If the answer is OK,
     * a query is send using a Runnable and Alert window is shown depending on the result of the query.
     * <p>
     *     This method is called when the delete button is pressed on the customer table.<br>
     *     The {@link #populateCustomersTable()} is called in order to display all updates to the appointments records.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #populateCustomersTable()
     * @since 1.0
     */
    public void onDeleteCustomerButton() {
        Optional<ButtonType> result = AlertControl.askConfirmation(
                mainBundle.getString("confirmation.deleteCustomer.title"),
                mainBundle.getString("confirmation.deleteCustomer.content"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            int customerID = selectedCustomer.getCustomerID();
            String customerName = selectedCustomer.getName();
            DBControl.runQuery(() -> {
                String query = "DELETE FROM customers WHERE Customer_ID = ?;";
                try (Connection connection = DBControl.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1,customerID);
                    int lines = preparedStatement.executeUpdate();
                    Platform.runLater(() -> {
                        populateAppointmentsTable();
                        if (lines > 0){
                            AlertControl.errorMessage(mainBundle.getString("error.success.title"),
                                    mainBundle.getString("error.customerID") + customerID + "\n" +
                                            mainBundle.getString("error.name") + customerName + "\n" +
                                            mainBundle.getString("error.deleteAppointmentSuccess"));
                        }
                    });
                } catch (SQLException | NullPointerException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        }
    }

    /**
     * The {@link #populateAppointmentsTable()} is called in order to update the information on the {@link #appointmentsTable}
     * <p>
     *     Called when the refresh button from the {@link #appointmentsTable} is pressed.
     * </p>
     * @see #populateAppointmentsTable()
     * @since 1.0
     */
    public void onAppointmentsRefreshButton( ) {
        populateAppointmentsTable();
    }

    /**
     * The {@link #populateCustomersTable()} is called in order to update the information on the {@link #customersTable}
     * <p>
     *     Called when the refresh button from the {@link #customersTable} is pressed.
     * </p>
     * @see #populateCustomersTable()
     * @since 1.0
     */
    public void onCustomersRefreshButton( ) {
        populateCustomersTable();
    }

    /**
     * The {@link #populateContactComboBox()},  is called in order to update the information on the {@link #contactReportTable}
     * <p>
     *     Called when the refresh button from the {@link #contactReportTable} is pressed.
     * </p>
     * @see #populateContactComboBox()
     * @since 1.0
     */
    public void onContactsRefreshButton() {
        reportsAnchor.setDisable(true);
        reportsProgressIndicator.setVisible(true);
        populateContactComboBox();
    }

    /**
     * Populates the {@link #monthViewTable} with the previous month to the one being viewed.
     * @see #populateMonthView(LocalDate)
     * @since 1.0
     */
    public void onMonthPreviousButton( ) {
        populateMonthView(firstDayOfMonth.minusMonths(1));
    }

    /**
     * Populates the {@link #monthViewTable} with the next month to the one being viewed.
     * @see #populateMonthView(LocalDate)
     * @since 1.0
     */
    public void onMonthNextButton( ) {
        populateMonthView(firstDayOfMonth.plusMonths(1));
    }

    /**
     * Uses a selection from the "appointments by month" view to populate the {@link #weekViewTable} with the date selected.
     * <p>
     *     Called every time a cell from the {@link #monthViewTable} is double-clicked.
     * </p>
     * @param mouseEvent Everytime any cell of the table is clicked.
     * @see #populateWeekView(LocalDate)
     * @since 1.0
     */
    public void onMonthSelection(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            MonthView monthView = monthViewTable.getSelectionModel().getSelectedItem();
            appointmentsTabPane.getSelectionModel().select(2);
            populateWeekView(monthView.getStartDate());
        }
    }

    /**
     * Populates the {@link #weekViewTable} with the previous week to the one being viewed.
     * @see #populateWeekView(LocalDate)
     * @since 1.0
     */
    public void onWeekPreviousButton( ) {
        populateWeekView(weekViewStartDate.minusWeeks(1));
    }

    /**
     * Populates the {@link #weekViewTable} with the next week to the one being viewed.
     * @see #populateWeekView(LocalDate)
     * @since 1.0
     */
    public void onWeekNextButton( ) {
        populateWeekView(weekViewStartDate.plusWeeks(1));
    }

    /**
     * Uses a selection from the "appointments by week" table to select an appointment from the {@link #appointmentsTable}.
     * <p>
     *     Called every time a cell from the {@link #weekViewTable} is double-clicked.
     * </p>
     * @param mouseEvent used everytime any cell of the table is clicked.
     * @since 1.0
     */
    public void onWeekSelection(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2){
            int column = weekViewTable.getSelectionModel().getSelectedCells().get(0).getColumn();
            WeekView weekView = weekViewTable.getSelectionModel().getSelectedItem();
            appointmentsTabPane.getSelectionModel().select(0);
            appointmentsTable.getSelectionModel().select(weekView.getAppointment(weekViewStartDate.plusDays(column - 1)));
        }
    }

    /**
     * Populates the {@link #typeReportTable} with the previous year to the one being viewed.
     * @see #populateTypeReport(LocalDate)
     * @since 1.0
     */
    public void onTypePreviousButton() {
        populateTypeReport(typeReportStartDate.minusYears(1));
    }

    /**
     * Populates the {@link #typeReportTable} with the next year to the one being viewed.
     * @see #populateTypeReport(LocalDate)
     * @since 1.0
     */
    public void onTypeNextButton( ) {
        populateTypeReport(typeReportStartDate.plusYears(1));
    }

    /**
     * Uses a selection from the "Appointments by type" table to populate the {@link #monthViewTable} with the date selected.
     * <p>
     *     Called every time a cell from the {@link #typeReportTable} is double-clicked.
     * </p>
     * @param mouseEvent used everytime any cell of the table is clicked.
     * @see #populateMonthView(LocalDate)
     * @since 1.0
     */
    public void onTypeReportSelection(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            int column = typeReportTable.getSelectionModel().getSelectedCells().get(0).getColumn();
            YearView yearView = typeReportTable.getSelectionModel().getSelectedItem();
            mainTabPane.getSelectionModel().select(0);
            appointmentsTabPane.getSelectionModel().select(1);
            populateMonthView(yearView.getStartDate().plusMonths(column - 1));
        }
    }

    /**
     * Populates the {@link #contactReportTable} with the previous week to the one being viewed.
     * @see #populateContactReport(LocalDate)
     * @since 1.0
     */
    public void onContactPreviousButton( ) {
        populateContactReport(contactReportStartDate.minusWeeks(1));
    }

    /**
     * Populates the {@link #contactReportTable} with the next week to the one being viewed.
     * @see #populateContactReport(LocalDate)
     * @since 1.0
     */
    public void onContactNextButton( ) {
        populateContactReport(contactReportStartDate.plusWeeks(1));
    }

    /**
     * Uses a selection from the "Contact Schedules" table to select an appointment from the {@link #appointmentsTable}.
     * <p>
     *     Called every time a cell from the {@link #contactReportTable} is double-clicked.
     * </p>
     * @param mouseEvent used everytime any cell of the table is clicked.
     * @since 1.0
     */
    public void onContactReportSelection(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2){
            int column = contactReportTable.getSelectionModel().getSelectedCells().get(0).getColumn();
            WeekView weekView = contactReportTable.getSelectionModel().getSelectedItem();
            mainTabPane.getSelectionModel().select(0);
            appointmentsTabPane.getSelectionModel().select(0);
            appointmentsTable.getSelectionModel().select(weekView.getAppointment(contactReportStartDate.plusDays(column - 1)));
        }
    }

    /**
     * Populates the {@link #locationReportTable} with the previous year to the one being viewed.
     * @see #populateLocationReport(LocalDate)
     * @since 1.0
     */
    public void onLocationPreviousButton( ) {
        populateLocationReport(locationReportStartDate.minusYears(1));
    }

    /**
     * Populates the {@link #locationReportTable} with the next year to the one being viewed.
     * @see #populateLocationReport(LocalDate)
     * @since 1.0
     */
    public void onLocationNextButton( ) {
        populateLocationReport(locationReportStartDate.plusYears(1));
    }

    /**
     * Uses a selection from the "Appointments by location" table to populate the {@link #monthViewTable} with the date selected.
     * <p>
     *     Called every time a cell from the {@link #locationReportTable} is double-clicked.
     * </p>
     * @param mouseEvent used everytime any cell of the table is clicked.
     * @see #populateMonthView(LocalDate)
     * @since 1.0
     */
    public void onLocationReportSelection(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            int column = locationReportTable.getSelectionModel().getSelectedCells().get(0).getColumn();
            YearView yearView = locationReportTable.getSelectionModel().getSelectedItem();
            mainTabPane.getSelectionModel().select(0);
            appointmentsTabPane.getSelectionModel().select(1);
            populateMonthView(yearView.getStartDate().plusMonths(column - 1));
        }
    }

    /**
     * Adds a listener to the {@link #appointmentsSearchBar} to filter the {@link #appointmentsTable} for anything input on the search bar.
     * <p>
     *     A lambda expression is use in order to automatically update the table depending on the new values.
     * </p>
     * @since 1.0
     */
    private void searchAppointments(){
        FilteredList<Appointment> appointmentsFilteredList = new FilteredList<>(appointmentsList, p -> true);
        appointmentsSearchBar.textProperty().addListener(((observable, oldValue, newValue) -> appointmentsFilteredList.setPredicate(appointment -> {
            if (newValue == null || newValue.isEmpty()){
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (Integer.toString(appointment.getAppointmentID()).contains(newValue)){
                return true;
            }
            if (appointment.getTitle().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (appointment.getDescription().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (appointment.getLocation().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (appointment.getContact().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (appointment.getType().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (appointment.getStartDateTime().toString().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (appointment.getEndDateTime().toString().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            return Integer.toString(appointment.getCustomerID()).contains(newValue);
        })));
        SortedList<Appointment> appointmentsSortedList = new SortedList<>(appointmentsFilteredList);
        appointmentsSortedList.comparatorProperty().bind(appointmentsTable.comparatorProperty());
        appointmentsTable.setItems(appointmentsSortedList);
        appointmentsTable.refresh();
    }

    /**
     * Adds a listener to the {@link #customersSearchBar} to filter the {@link #customersTable} for anything input on the search bar.
     * <p>
     *     A lambda expression is use in order to automatically update the table depending on the new values.
     * </p>
     * @since 1.0
     */
    private void searchCustomers(){
        FilteredList<Customer> customersFilteredList = new FilteredList<>(customersList, p -> true);
        customersSearchBar.textProperty().addListener(((observable, oldValue, newValue) -> customersFilteredList.setPredicate(customer -> {
            if (newValue == null || newValue.isEmpty()){
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (Integer.toString(customer.getCustomerID()).contains(newValue)){
                return true;
            }
            if (customer.getName().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (customer.getAddress().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            if (customer.getPostalCode().contains(newValue)){
                return true;
            }
            if (customer.getPhone().contains(newValue)){
                return true;
            }
            if (customer.getDivision().contains(newValue)){
                return true;
            }
            return customer.getCountry().contains(newValue);
        })));
        SortedList<Customer> customerSortedList = new SortedList<>(customersFilteredList);
        customerSortedList.comparatorProperty().bind(customersTable.comparatorProperty());
        customersTable.setItems(customerSortedList);
        customersTable.refresh();
    }

    /**
     * Determines what local day is the first day of the week in order to use it to produce WeekView objects.
     * @param startDate LocalDate used to determine what date is the first day of the week.
     * @return the local date that is the first day fo the week.
     * @since 1.0
     */
    private LocalDate getFirstDayOfWeek(LocalDate startDate){
        LocalDate firstDayOfWeek = null;
        switch (startDate.getDayOfWeek()){
            case SUNDAY:
                firstDayOfWeek = startDate;
                break;
            case MONDAY:
                firstDayOfWeek = startDate.minusDays(1);
                break;
            case TUESDAY:
                firstDayOfWeek = startDate.minusDays(2);
                break;
            case WEDNESDAY:
                firstDayOfWeek = startDate.minusDays(3);
                break;
            case THURSDAY:
                firstDayOfWeek = startDate.minusDays(4);
                break;
            case FRIDAY:
                firstDayOfWeek = startDate.minusDays(5);
                break;
            case SATURDAY:
                firstDayOfWeek = startDate.minusDays(6);
                break;
        }
        return firstDayOfWeek;
    }

    /**
     * Adds restriction to the table in order to avoid the user from reordering the columns of the tables.
     * <p>
     *     All the table columns are set a value factory in order to display the information from their respective objects.
     *     The selection model is changed to allow the single cell selection for the tables that require it.
     * </p>
     * @since 1.0
     */
    private void setCellRestrictions(){
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        appointmentEndDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        appointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        appointmentIDColumn.setReorderable(false);
        appointmentTitleColumn.setReorderable(false);
        appointmentDescriptionColumn.setReorderable(false);
        appointmentLocationColumn.setReorderable(false);
        appointmentContactColumn.setReorderable(false);
        appointmentTypeColumn.setReorderable(false);
        appointmentStartDateTimeColumn.setReorderable(false);
        appointmentEndDateTimeColumn.setReorderable(false);
        appointmentCustomerIDColumn.setReorderable(false);

        monthSundayColumn.setCellValueFactory(new PropertyValueFactory<>("sunday"));
        monthMondayColumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        monthTuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        monthWednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        monthThursdayColumn.setCellValueFactory(new PropertyValueFactory<>("Thursday"));
        monthFridayColumn.setCellValueFactory(new PropertyValueFactory<>("friday"));
        monthSaturdayColumn.setCellValueFactory(new PropertyValueFactory<>("saturday"));

        monthSundayColumn.setReorderable(false);
        monthMondayColumn.setReorderable(false);
        monthTuesdayColumn.setReorderable(false);
        monthWednesdayColumn.setReorderable(false);
        monthThursdayColumn.setReorderable(false);
        monthFridayColumn.setReorderable(false);
        monthSaturdayColumn.setReorderable(false);

        weekTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        weekSundayColumn.setCellValueFactory(new PropertyValueFactory<>("sunday"));
        weekMondayColumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        weekTuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        weekWednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        weekThursdayColumn.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        weekFridayColumn.setCellValueFactory(new PropertyValueFactory<>("friday"));
        weekSaturdayColumn.setCellValueFactory(new PropertyValueFactory<>("saturday"));

        weekTimeColumn.setReorderable(false);
        weekSundayColumn.setReorderable(false);
        weekMondayColumn.setReorderable(false);
        weekTuesdayColumn.setReorderable(false);
        weekWednesdayColumn.setReorderable(false);
        weekThursdayColumn.setReorderable(false);
        weekFridayColumn.setReorderable(false);
        weekSaturdayColumn.setReorderable(false);

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        customerIDColumn.setReorderable(false);
        customerNameColumn.setReorderable(false);
        customerAddressColumn.setReorderable(false);
        customerPostalCodeColumn.setReorderable(false);
        customerPhoneColumn.setReorderable(false);
        customerDivisionColumn.setReorderable(false);
        customerCountryColumn.setReorderable(false);

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
        typeJanuaryColumn.setCellValueFactory(new PropertyValueFactory<>("january"));
        typeFebruaryColumn.setCellValueFactory(new PropertyValueFactory<>("february"));
        typeMarchColumn.setCellValueFactory(new PropertyValueFactory<>("march"));
        typeAprilColumn.setCellValueFactory(new PropertyValueFactory<>("april"));
        typeMayColumn.setCellValueFactory(new PropertyValueFactory<>("may"));
        typeJuneColumn.setCellValueFactory(new PropertyValueFactory<>("june"));
        typeJulyColumn.setCellValueFactory(new PropertyValueFactory<>("july"));
        typeAugustColumn.setCellValueFactory(new PropertyValueFactory<>("august"));
        typeSeptemberColumn.setCellValueFactory(new PropertyValueFactory<>("september"));
        typeOctoberColumn.setCellValueFactory(new PropertyValueFactory<>("october"));
        typeNovemberColumn.setCellValueFactory(new PropertyValueFactory<>("november"));
        typeDecemberColumn.setCellValueFactory(new PropertyValueFactory<>("december"));

        typeColumn.setReorderable(false);
        typeJanuaryColumn.setReorderable(false);
        typeFebruaryColumn.setReorderable(false);
        typeMarchColumn.setReorderable(false);
        typeAprilColumn.setReorderable(false);
        typeMayColumn.setReorderable(false);
        typeJuneColumn.setReorderable(false);
        typeJulyColumn.setReorderable(false);
        typeAugustColumn.setReorderable(false);
        typeSeptemberColumn.setReorderable(false);
        typeOctoberColumn.setReorderable(false);
        typeNovemberColumn.setReorderable(false);
        typeDecemberColumn.setReorderable(false);

        contactTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        contactSundayColumn.setCellValueFactory(new PropertyValueFactory<>("sunday"));
        contactMondayColumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        contactTuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        contactWednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        contactThursdayColumn.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        contactFridayColumn.setCellValueFactory(new PropertyValueFactory<>("friday"));
        contactSaturdayColumn.setCellValueFactory(new PropertyValueFactory<>("saturday"));

        contactTimeColumn.setReorderable(false);
        contactSundayColumn.setReorderable(false);
        contactMondayColumn.setReorderable(false);
        contactTuesdayColumn.setReorderable(false);
        contactWednesdayColumn.setReorderable(false);
        contactThursdayColumn.setReorderable(false);
        contactFridayColumn.setReorderable(false);
        contactSaturdayColumn.setReorderable(false);

        locationColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
        locationJanuaryColumn.setCellValueFactory(new PropertyValueFactory<>("january"));
        locationFebruaryColumn.setCellValueFactory(new PropertyValueFactory<>("february"));
        locationMarchColumn.setCellValueFactory(new PropertyValueFactory<>("march"));
        locationAprilColumn.setCellValueFactory(new PropertyValueFactory<>("april"));
        locationMayColumn.setCellValueFactory(new PropertyValueFactory<>("may"));
        locationJuneColumn.setCellValueFactory(new PropertyValueFactory<>("june"));
        locationJulyColumn.setCellValueFactory(new PropertyValueFactory<>("july"));
        locationAugustColumn.setCellValueFactory(new PropertyValueFactory<>("august"));
        locationSeptemberColumn.setCellValueFactory(new PropertyValueFactory<>("september"));
        locationOctoberColumn.setCellValueFactory(new PropertyValueFactory<>("october"));
        locationNovemberColumn.setCellValueFactory(new PropertyValueFactory<>("november"));
        locationDecemberColumn.setCellValueFactory(new PropertyValueFactory<>("december"));

        locationColumn.setReorderable(false);
        locationJanuaryColumn.setReorderable(false);
        locationFebruaryColumn.setReorderable(false);
        locationMarchColumn.setReorderable(false);
        locationAprilColumn.setReorderable(false);
        locationMayColumn.setReorderable(false);
        locationJuneColumn.setReorderable(false);
        locationJulyColumn.setReorderable(false);
        locationAugustColumn.setReorderable(false);
        locationSeptemberColumn.setReorderable(false);
        locationOctoberColumn.setReorderable(false);
        locationNovemberColumn.setReorderable(false);
        locationDecemberColumn.setReorderable(false);

        monthViewTable.getSelectionModel().setCellSelectionEnabled(true);
        weekViewTable.getSelectionModel().setCellSelectionEnabled(true);
        typeReportTable.getSelectionModel().setCellSelectionEnabled(true);
        contactReportTable.getSelectionModel().setCellSelectionEnabled(true);
        locationReportTable.getSelectionModel().setCellSelectionEnabled(true);
    }
}
