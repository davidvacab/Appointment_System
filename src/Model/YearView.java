package Model;

import java.time.LocalDate;

/**
 * Represents the number of appointments by month for a specific characteristic.
 * <p>
 *     Use for tables using a yearly view.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class YearView {
    private final String property;
    private final LocalDate startDate;
    private int january;
    private int february;
    private int march;
    private int april;
    private int may;
    private int june;
    private int july;
    private int august;
    private int september;
    private int october;
    private int november;
    private int december;

    /**
     * Creates a YearView with the specified property and start date.
     * @param property Characteristic use to count the appointments.
     * @param startDate first day of the year.
     * @since 1.0
     */
    public YearView(String property, LocalDate startDate){
        this.property = property;
        this.startDate = startDate;
    }

    /**
     * A month is determined from the appointment start date, and the counter for that month is increased by one.
     * @param appointment An appointment used for counting.
     * @since 1.0
     */
    public void addAppointment(Appointment appointment){
        switch (appointment.getStartDateTime().getMonth()){
            case JANUARY:
                january += 1;
                break;
            case FEBRUARY:
                february += 1;
                break;
            case MARCH:
                march += 1;
                break;
            case APRIL:
                april += 1;
                break;
            case MAY:
                may += 1;
                break;
            case JUNE:
                june += 1;
                break;
            case JULY:
                july += 1;
                break;
            case AUGUST:
                august += 1;
                break;
            case SEPTEMBER:
                september += 1;
                break;
            case OCTOBER:
                october += 1;
                break;
            case NOVEMBER:
                november += 1;
                break;
            case DECEMBER:
                december += 1;
                break;
            default:
                System.out.println("Not a month.");
        }
    }

    /**
     * Gets a the year view's property.
     * @return A String representing a property or characteristic.
     * @since 1.0
     */
    public String getProperty() {
        return property;
    }

    /**
     * Gets the year view's first day of the year.
     * @return A LocalDate representing the first day of the year.
     * @since 1.0
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Gets the appointment's count for the specific property for January.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getJanuary() {
        return january;
    }

    /**
     * Gets the appointment's count for the specific property for February.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getFebruary() {
        return february;
    }

    /**
     * Gets the appointment's count for the specific property for March.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getMarch() {
        return march;
    }

    /**
     * Gets the appointment's count for the specific property for April.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getApril() {
        return april;
    }

    /**
     * Gets the appointment's count for the specific property for May.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getMay() {
        return may;
    }

    /**
     * Gets the appointment's count for the specific property for June.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getJune() {
        return june;
    }

    /**
     * Gets the appointment's count for the specific property for July.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getJuly() {
        return july;
    }

    /**
     * Gets the appointment's count for the specific property for August.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getAugust() {
        return august;
    }

    /**
     * Gets the appointment's count for the specific property for September.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getSeptember() {
        return september;
    }

    /**
     * Gets the appointment's count for the specific property for October.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getOctober() {
        return october;
    }

    /**
     * Gets the appointment's count for the specific property for November.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getNovember() {
        return november;
    }

    /**
     * Gets the appointment's count for the specific property for December.
     * @return An int representing the appointment's count for a specific property.
     * @since 1.0
     */
    public int getDecember() {
        return december;
    }
}
