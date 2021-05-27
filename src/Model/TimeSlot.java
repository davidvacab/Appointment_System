package Model;

import java.time.LocalTime;

/**
 * Represents a time slot for appointments.
 * <p>
 *     Used for restricting the appointments timeframes and populating the tables that use the weekly view.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class TimeSlot {
    private final LocalTime time;
    private boolean available = true;

    /**
     * Creates a TimeSlot with the specified time.
     * @param time A LocalTime containing the time of the time slot.
     * @since 1.0
     */
    public TimeSlot(LocalTime time){
        this.time = time;
    }

    /**
     * Gets the time of the time slot.
     * @return A LocalTime representing the time of the time slot.
     * @since 1.0
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the state of the time slot.
     * @param available A boolean containing the state of the time slot.
     * @since 1.0
     */
    public void setAvailable(boolean available){
        this.available = available;
    }

    /**
     * Gets the state of the time slot.
     * @return A boolean representing the state of the time slot.
     * @since 1.0
     */
    public boolean isAvailable() {
        return available;
    }
}
