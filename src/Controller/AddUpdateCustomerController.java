package Controller;

import Model.Customer;
import StaticControl.*;
import StaticControl.CurrentUser;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.text.MaskFormatter;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the addUpdateCustomerView.
 * <p>
 *     Adds or updates the customer records on the database.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class AddUpdateCustomerController implements Initializable {
    public Label mainLabel;
    public TextField customerIDTextField;
    public TextField nameTextField;
    public TextField addressTextField;
    public TextField postalCodeTextField;
    public TextField phoneTextField;
    public ComboBox<String> countriesComboBox;
    public ComboBox<String> divisionsComboBox;
    public Button saveButton;
    public Button cancelButton;
    public AnchorPane anchorPane;
    public ProgressIndicator loadingAnimation;

    private boolean update = false;
    private Customer customer;
    private ResourceBundle cusBundle;

    /**
     * Different methods which create listeners are called.
     * <p>
     *     The ResourceBundle is assigned to the {@link #cusBundle} parameter which is used to localized the errors displayed.
     * </p>
     * @param location Location for the root object.
     * @param resources ResourceBundle provided on the MainScreenController class, used for localization of the GUI interface.
     * @see #validateInput()
     * @see #checkFields()
     * @see #populateCountriesComboBox()
     * @see #populateDivisionsComboBox()
     * @since 1.0
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cusBundle = resources;
        validateInput();
        checkFields();
        populateCountriesComboBox();
        populateDivisionsComboBox();
    }

    /**
     * Sets the selected customer and the update value to true.
     * <p>
     *     If a customer record needs to be updated, this method is called in order to set the {@link #update} value to true,
     *     and all the TextFields and ComboBoxes are set to the customer parameters values.
     * </p>
     * @param customer Selected customer from the customers table on the main screen view.
     * @since 1.0
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        update = true;
    }

    /**
     * Prevents the user from trying to summit multiple login attempts by disabling the GUI interface and showing a loading animation
     * until the processes sent to the database are done processing.
     * @param state A boolean that sets the {@link #anchorPane} and {@link #loadingAnimation}.
     * @since 1.0
     */
    private void loadingView(boolean state){
        anchorPane.setDisable(state);
        loadingAnimation.setVisible(state);
    }

    /**
     * Prevents the user from inputting correct information in the {@link #postalCodeTextField} and {@link #phoneTextField}.
     * <p>
     *     Any character not allowed will be automatically deleted.
     * </p>
     * @since 1.0
     */
    private void validateInput(){
        postalCodeTextField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches(".{0,5}") ? change : null)));
        phoneTextField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("\\d{0,12}") ? change : null)));
    }

    /**
     * Binds the {@link #saveButton} to the all of the TextFields. Therefore preventing the
     * program from throwing an exception if ether TextField is empty and the {@link #saveButton} is pressed.
     * @since 1.0
     */
    private void checkFields(){
        BooleanBinding checkFields = nameTextField.textProperty().isEmpty()
                .or(addressTextField.textProperty().isEmpty()
                        .or(postalCodeTextField.textProperty().length().isNotEqualTo(5)
                                .or(phoneTextField.textProperty().length().lessThan(10)
                                        .or(phoneTextField.textProperty().length().isEqualTo(11))
                                                .or(countriesComboBox.valueProperty().isNull()
                                                        .or(divisionsComboBox.valueProperty().isNull())))));
        saveButton.disableProperty().bind(checkFields);
        BooleanBinding checkCountriesComoBox = countriesComboBox.valueProperty().isNull();
        divisionsComboBox.disableProperty().bind(checkCountriesComoBox);
    }

    /**
     * Queries a list of available countries from the database and assigns the values to the {@link #countriesComboBox}.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     If the {@link #update} value is true, here all applicable the parameters from the {@link #customer} will be set.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #loadingView(boolean)
     * @since 1.0
     */
    private void populateCountriesComboBox(){
        loadingView(true);
        ObservableList<String> countriesList = FXCollections.observableArrayList();
        DBControl.runQuery(() -> {
            String query = "SELECT Country FROM countries;";
            try (Connection conn = DBControl.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while(rs.next()){
                    countriesList.add(rs.getString(1));
                }
                Platform.runLater(() -> {
                    countriesComboBox.setItems(countriesList);
                    if (update){
                        customerIDTextField.setText(Integer.toString(customer.getCustomerID()));
                        nameTextField.setText(customer.getName());
                        addressTextField.setText(customer.getAddress());
                        postalCodeTextField.setText(customer.getPostalCode());
                        phoneTextField.setText(customer.getPhone().replace("-", ""));
                        countriesComboBox.setValue(customer.getCountry());
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
     * Queries a list of available divisions from the database base on the selected country from the {@link #countriesComboBox} and assigns the values to the {@link #countriesComboBox}.
     * <p>
     *     The {@link #loadingView(boolean)} is set to true to prevent user errors.<br>
     *     If the {@link #update} value is true, here all applicable the parameters from the {@link #customer} will be set.<br>
     *     A Runnable method is used in order to prevent the Application thread from freezing due to the time it takes to retrieve the information from the database.<br>
     *     The Platform.runLater() lets the app finish the runnable task and then calls an action to be perform on the javafx thread. Therefore preventing a exception
     *     for trying to call javafx methods in a different thread.<br>
     *     A lambda expression is used to make easier use of the Runnable and the Platform.runLater.
     * </p>
     * @see #loadingView(boolean)
     * @since 1.0
     */
    private void populateDivisionsComboBox(){
        countriesComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadingView(true);
            ObservableList<String> divisionsList = FXCollections.observableArrayList();
            DBControl.runQuery(() -> {
                String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = (SELECT Country_ID FROM countries WHERE Country = ?);";
                try (Connection conn = DBControl.getConnection();
                     PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, newValue);
                    ResultSet rs = preparedStatement.executeQuery();
                    while(rs.next()){
                        divisionsList.add(rs.getString(1));
                    }
                    Platform.runLater(() -> {
                        divisionsComboBox.setItems(divisionsList);
                        loadingView(false);
                        if (update) {
                            divisionsComboBox.setValue(customer.getDivision());
                            nameTextField.requestFocus();
                        }
                    });
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    loadingView(false);
                }
            });
        });
    }

    /**
     * Passes the focus to the {@link #addressTextField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the name on the {@link #nameTextField}.
     * </p>
     * @since 1.0
     */
    public void onNameTextField( ) {
        addressTextField.requestFocus();
    }

    /**
     * Passes the focus to the {@link #postalCodeTextField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the address on the {@link #addressTextField}.
     * </p>
     * @since 1.0
     */
    public void onAddressTextField( ) {
        postalCodeTextField.requestFocus();
    }

    /**
     * Passes the focus to the {@link #phoneTextField}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the postal code on the {@link #postalCodeTextField}.
     * </p>
     * @since 1.0
     */
    public void onPostalCodeTextField( ) {
        phoneTextField.requestFocus();
    }

    /**
     * Passes the focus to the {@link #countriesComboBox}.
     * <p>
     *     Called when the ENTER key is pressed while inputting the phone number on the {@link #phoneTextField}.
     * </p>
     * @since 1.0
     */
    public void onPhoneTextField( ) {
        countriesComboBox.requestFocus();
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
                query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Updated_By = ?, " +
                        "Division_ID = (SELECT Division_ID FROM first_level_divisions WHERE Division = ?) " +
                        "WHERE Customer_ID = " + customer.getCustomerID() +" ;";
            } else {
                query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Division_ID) " +
                        "VALUES (?,?,?,?,?,(SELECT Division_ID FROM first_level_divisions WHERE Division = ?));";
            }
            try (Connection conn = DBControl.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, nameTextField.getText());
                preparedStatement.setString(2, addressTextField.getText());
                preparedStatement.setString(3, postalCodeTextField.getText());
                preparedStatement.setString(4, phoneFormatter(phoneTextField.getText()));
                preparedStatement.setString(5, CurrentUser.getUserName());
                preparedStatement.setString(6, divisionsComboBox.getValue());
                int lines = preparedStatement.executeUpdate();
                Platform.runLater(() -> {
                    if (lines > 0) {
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                    }
                });
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                loadingView(false);
            }
        });
    }

    /**
     * Called when the {@link #cancelButton} is pressed.
     * <p>
     *     A confirmation alert is shown, if the result is OK the program will close; otherwise the window will stay opened.
     * </p>
     * @since 1.0
     */
    public void onCancelButton() {
        Optional<ButtonType> result = AlertControl.askConfirmation(cusBundle.getString("button.cancel"), cusBundle.getString("error.cancel"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Called when the close button is pressed on the top of the window.
     * <p>
     *     A confirmation alert is shown, if the result is OK the program will close; otherwise the window will stay opened.
     * </p>
     * @param event Closing window event.
     * @since 1.0
     */
    public void onCloseButton(WindowEvent event) {
        Optional<ButtonType> result = AlertControl.askConfirmation(cusBundle.getString("button.cancel"), cusBundle.getString("error.cancel"));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else {
            event.consume();
        }
    }

    /**
     * Formats the phone numbers depending on the length of 10 or 12.
     * @param phoneNumber Phone number to be formatted.
     * @return The formatted phone number.
     * @since 1.0
     */
    private String phoneFormatter(String phoneNumber){
        MaskFormatter maskFormatter;
        String phoneFormatted = "";
        String phoneMask;
        if (phoneNumber.length() == 10){
            phoneMask = "###-###-####";
        } else {
            phoneMask = "##-###-###-####";
        }
        try {
            maskFormatter = new MaskFormatter(phoneMask);
            maskFormatter.setValueContainsLiteralCharacters(false);
            phoneFormatted = maskFormatter.valueToString(phoneNumber);
        } catch (ParseException e){
            System.out.println(e.getMessage());
        }
        return phoneFormatted;
    }

}
