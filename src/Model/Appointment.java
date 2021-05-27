package Model;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Represents an appointment.
 * @author David Vaca
 * @since 1.0
 */
public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerID;
    private int userID;

    /**
     * Creates an appointment with no parameter values.
     */
    public Appointment(){ }

    /**
     * Gets the appointment's ID.
     * @return An int representing the appointment's ID.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Sets the appointment's ID.
     * @param appointmentID An int containing the appointment's ID.
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Gets the appointment's title.
     * @return A String representing the appointment's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the appointment's title.
     * @param title A String containing the appointment's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the appointment's description.
     * @return A String representing the appointment's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the appointment's description.
     * @param description A String containing the appointment's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the appointment's location.
     * @return A String representing the appointment's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the appointment's location.
     * @param location A String containing the appointment's location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the appointment's contact name.
     * @return A String representing the appointment's contact name.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the appointment's contact.
     * @param contact A String containing the appointment's contact name.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets the appointment's type.
     * @return A String representing the appointment's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the appointment's type.
     * @param type A String containing the appointment's type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the appointment's start Date and Time.
     * @return A LocalDateTime representing the appointment's start Date and Time.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the appointment's start Date and Time.
     * @param startDateTime A LocalDateTime containing the appointment's start Date and Time.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the appointment's end Date and Time.
     * @return A LocalDateTime representing the appointment's end Date and Time.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the appointment's end Date and Time.
     * @param endDateTime A LocalDateTime containing the appointment's end Date and Time.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the appointment's customer ID.
     * @return An int representing the appointment's customer ID.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets the appointment's customer ID.
     * @param customerID An int containing the appointment's customer ID.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the appointment's user ID.
     * @return An int representing the appointment's user ID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the appointment's user ID.
     * @param userID An int containing the appointment's user ID.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets all the appointment's parameter values.
     * @return A String with all the appointment's parameter values.
     * @since 1.0
     */
    public String toString(){
        if (Locale.getDefault().getLanguage().matches("fr")) {
            return "Identification: " + appointmentID + "\n" +
                    "Titre: " + title + "\n" +
                    "Genre: " + type + "\n" +
                    "La Description: " + description + "\n" +
                    "Date: " + startDateTime.toLocalDate() + "\n" +
                    "Heure: " + startDateTime.toLocalTime() + " - " + endDateTime.toLocalTime() + "\n" +
                    "Identification du Client: " + customerID;
        } else {
            return "ID: " + appointmentID + "\n" +
                    "Title: " + title + "\n" +
                    "Type: " + type + "\n" +
                    "Description: " + description + "\n" +
                    "Date: " + startDateTime.toLocalDate() + "\n" +
                    "Time: " + startDateTime.toLocalTime() + " - " + endDateTime.toLocalTime() + "\n" +
                    "Customer ID: " + customerID;
        }
    }
}
