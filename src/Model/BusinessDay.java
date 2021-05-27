package Model;

import java.time.*;
import java.util.ArrayList;

/**
 * Represent a business day of the company.
 * <p>
 *     Populates and contains a list of all the time slots possible for the company's business hours.
 *     <br>
 *     If this program is to be used by a different company, here is where the business hours and timezone would be adjust.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class BusinessDay {
    /**
     * Instant containing the opening time of the company in UTC.
     */
    private final Instant instant = Instant.parse("2021-01-01T13:00:00.0Z");
    private final ArrayList<TimeSlot> timeSlotsList = new ArrayList<>();
    private final LocalTime openingTime = instant.atZone(ZoneId.systemDefault()).toLocalTime();
    private final LocalTime closingTime = openingTime.plusHours(14);

    /**
     * Creates a BusinessDay with no parameter values.
     * <p>
     *     Populates the list.
     * </p>
     * The list can be retrieve with the respective getter.
     * @see #populateList()
     * @since 1.0
     */
    public BusinessDay(){
        populateList();
    }

    /**
     * Gets a list of all the time slots possible for the business hours.
     * @return An ArrayList containing all the time slots possible for the business hours.
     * @since 1.0
     */
    public ArrayList<TimeSlot> getList(){
        return timeSlotsList;
    }

    /**
     * Creates and adds all the possible time slots of a regular business day for the company.
     * <p>
     *     Values of the time and range can be adjust in order to use for a different company.
     *     <br>
     *     The base values established are: 8:00 am to 10:00 pm EST time, every 15 minutes.
     * </p>
     * @since 1.0
     */
    private void populateList(){
        LocalTime timeSlot = openingTime;
        while (timeSlot.equals(openingTime) || timeSlot.equals(closingTime) || (timeSlot.isAfter(openingTime) && timeSlot.isBefore(closingTime))){
            timeSlotsList.add(new TimeSlot(timeSlot));
            timeSlot = timeSlot.plusMinutes(15);
        }
    }
}
