package Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents the appointments of a time slot over a week.
 * <p>
 *     Use for tables using a weekly view of the appointments.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class WeekView {
    private final LocalTime timeSlot;
    private Appointment sunday;
    private Appointment monday;
    private Appointment tuesday;
    private Appointment wednesday;
    private Appointment thursday;
    private Appointment friday;
    private Appointment saturday;

    /**
     * Creates a WeekView with the specified time slot.
     * @param timeSlot A LocalTime that will be displayed on the first column of the table.
     * @since 1.0
     */
    public WeekView(LocalTime timeSlot){
        this.timeSlot = timeSlot;
    }

    /**
     * A day of the week is determined from the appointment start date, and the appointment for that day is set.
     * @param appointment appointment that matches the time slot.
     * @since 1.0
     */
    public void addAppointment(Appointment appointment){
        switch (appointment.getStartDateTime().toLocalDate().getDayOfWeek()){
            case SUNDAY:
                sunday = appointment;
                break;
            case MONDAY:
                monday = appointment;
                break;
            case TUESDAY:
                tuesday = appointment;
                break;
            case WEDNESDAY:
                wednesday = appointment;
                break;
            case THURSDAY:
                thursday = appointment;
                break;
            case FRIDAY:
                friday = appointment;
                break;
            case SATURDAY:
                saturday = appointment;
                break;
        }
    }

    /**
     * Gets the appointment which start date matches the provided LocalDate.
     * @param localDate A LocalDate matching the date of an appointment for the week.
     * @return The appointment matching the local date.
     * @since 1.0
     */
    public Appointment getAppointment(LocalDate localDate){
        Appointment appointment = null;
        switch (localDate.getDayOfWeek()){
            case SUNDAY:
                appointment = sunday;
                break;
            case MONDAY:
                appointment = monday;
                break;
            case TUESDAY:
                appointment = tuesday;
                break;
            case WEDNESDAY:
                appointment = wednesday;
                break;
            case THURSDAY:
                appointment = thursday;
                break;
            case FRIDAY:
                appointment = friday;
                break;
            case SATURDAY:
                appointment = saturday;
                break;
        }
        return appointment;
    }

    /**
     * Gets the time slot of the week view.
     * @return A LocalTime containing the time slot of the week view.
     * @since 1.0
     */
    public LocalTime getTimeSlot() {
        return timeSlot;
    }

    /**
     * Gets a String form of the Sunday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getSunday() {
        return (sunday != null ? sunday.toString() : "");
    }

    /**
     * Gets a String form of the Monday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getMonday() {
        return (monday != null ? monday.toString() : "");
    }

    /**
     * Gets a String form of the Tuesday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getTuesday() {
        return (tuesday != null ? tuesday.toString() : "");
    }

    /**
     * Gets a String form of the Wednesday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getWednesday() {
        return (wednesday != null ? wednesday.toString() : "");
    }

    /**
     * Gets a String form of the Thursday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getThursday() {
        return (thursday != null ? thursday.toString() : "");
    }

    /**
     * Gets a String form of the Friday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getFriday() {
        return (friday != null ? friday.toString() : "");
    }

    /**
     * Gets a String form of the Saturday appointment.
     * @return A String form of the appointment.
     * @since 1.0
     */
    public String getSaturday() {
        return (saturday != null ? saturday.toString() : "");
    }
}
