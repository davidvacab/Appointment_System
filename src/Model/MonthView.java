package Model;

import java.time.LocalDate;

/**
 * Represents the appointments per day over a week.
 * <p>
 *     Use for the tables using a monthly view.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class MonthView {
    private final LocalDate startDate;
    private String sunday;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;

    private int sundayCount;
    private int mondayCount;
    private int tuesdayCount;
    private int wednesdayCount;
    private int thursdayCount;
    private int fridayCount;
    private int saturdayCount;

    /**
     * Creates a MonthView with the specified start date and sets all the days of the week.
     * @param startDate first day of the week (not necessarily first day of a month).
     * @since 1.0
     */
    public MonthView (LocalDate startDate){
        this.startDate = startDate;
        setSunday();
        setMonday();
        setTuesday();
        setWednesday();
        setThursday();
        setFriday();
        setSaturday();
    }

    /**
     * A day of the week is determined from the appointment start date, and the counter for that day is increased by one.
     * <p>
     *     The setter is called with the updated counter.
     * </p>
     * @param appointment An appointment used for counting.
     * @since 1.0
     */
    public void addAppointment(Appointment appointment){
        switch (appointment.getStartDateTime().toLocalDate().getDayOfWeek()){
            case SUNDAY:
                sundayCount += 1;
                setSunday();
                break;
            case MONDAY:
                mondayCount += 1;
                setMonday();
                break;
            case TUESDAY:
                tuesdayCount += 1;
                setTuesday();
                break;
            case WEDNESDAY:
                wednesdayCount += 1;
                setWednesday();
                break;
            case THURSDAY:
                thursdayCount += 1;
                setThursday();
                break;
            case FRIDAY:
                fridayCount += 1;
                setFriday();
                break;
            case SATURDAY:
                saturdayCount += 1;
                setSaturday();
                break;
        }
    }

    /**
     * Returns a String based on the appointment count provided.
     * <p>
     *     An empty string is returned if the count is 0.
     * </p>
     * @param appCount number of appointments for a specific day.
     * @return A String based on the value of the appCount.
     * @since 1.0
     */
    private String getAppCountString(int appCount){
        switch (appCount){
            case 0:
                return "";
            case 1:
                return "1 appointment.";
            default:
                return appCount + " appointments.";
        }
    }

    /**
     * Sets the Sunday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setSunday() {
        int day = startDate.getDayOfMonth();
        sunday = day + "\n\n\n" + getAppCountString(sundayCount);
    }

    /**
     * Sets the Monday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setMonday() {
        int day = startDate.plusDays(1).getDayOfMonth();
        monday = day + "\n\n\n" + getAppCountString(mondayCount);
    }

    /**
     * Sets the Tuesday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setTuesday() {
        int day = startDate.plusDays(2).getDayOfMonth();
        tuesday = day + "\n\n\n" + getAppCountString(tuesdayCount);
    }

    /**
     * Sets the Wednesday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setWednesday() {
        int day = startDate.plusDays(3).getDayOfMonth();
        wednesday = day + "\n\n\n" + getAppCountString(wednesdayCount);
    }

    /**
     * Sets the Thursday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setThursday() {
        int day = startDate.plusDays(4).getDayOfMonth();
        thursday = day + "\n\n\n" + getAppCountString(thursdayCount);
    }

    /**
     * Sets the Friday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setFriday() {
        int day = startDate.plusDays(5).getDayOfMonth();
        friday = day + "\n\n\n" + getAppCountString(fridayCount);
    }

    /**
     * Sets the Saturday String.
     * <p>
     *     Gets the day of the month and the appointment count and uses is to create a String.
     * </p>
     * @see #getAppCountString(int)
     * @since 1.0
     */
    private void setSaturday() {
        int day = startDate.plusDays(6).getDayOfMonth();
        saturday = day + "\n\n\n" + getAppCountString(saturdayCount);
    }

    /**
     * Gets the first day of the week.
     * @return A LocalDate representing the first day of the week.
     * @since 1.0
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Gets the day of the month and the number of appointments for Sunday.
     * @return A String containing the day of the month and the number or appointments for Sunday.
     * @since 1.0
     */
    public String getSunday() {
        return sunday;
    }

    /**
     * Gets the day of the month and the number of appointments for Monday.
     * @return A String containing the day of the month and the number or appointments for Monday.
     * @since 1.0
     */
    public String getMonday() {
        return monday;
    }

    /**
     * Gets the day of the month and the number of appointments for Tuesday.
     * @return A String containing the day of the month and the number or appointments for Tuesday.
     * @since 1.0
     */
    public String getTuesday() {
        return tuesday;
    }

    /**
     * Gets the day of the month and the number of appointments for Wednesday.
     * @return A String containing the day of the month and the number or appointments for Wednesday.
     * @since 1.0
     */
    public String getWednesday() {
        return wednesday;
    }

    /**
     * Gets the day of the month and the number of appointments for Thursday.
     * @return A String containing the day of the month and the number or appointments for Thursday.
     * @since 1.0
     */
    public String getThursday() {
        return thursday;
    }

    /**
     * Gets the day of the month and the number of appointments for Friday.
     * @return A String containing the day of the month and the number or appointments for Friday.
     * @since 1.0
     */
    public String getFriday() {
        return friday;
    }

    /**
     * Gets the day of the month and the number of appointments for Saturday.
     * @return A String containing the day of the month and the number or appointments for Saturday.
     * @since 1.0
     */
    public String getSaturday() {
        return saturday;
    }
}
