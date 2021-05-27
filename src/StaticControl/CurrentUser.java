package StaticControl;

/**
 * Represents the current user.
 * <p>
 *     Stores the current user logged into the computer for posterior use in the controllers and queries.
 * </p>
 * @author David Vaca
 * @since 1.0
 */
public class CurrentUser {
    public CurrentUser(){ }
    private static int userID = 0;
    private static String userName;

    /**
     * Gets the current user's user ID.
     * @return An int representing the current user's user ID.
     * @since 1.0
     */
    public static int getUserID() {
        return userID;
    }

    /**
     * Sets the current user's user ID.
     * @param userID An int containing the current user's user ID.
     * @since 1.0
     */
    public static void setUserID(int userID) {
        CurrentUser.userID = userID;
    }

    /**
     * Gets the current user's username.
     * @return A String representing the current user's username.
     * @since 1.0
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Sets the current user's username.
     * @param userName A String containing the current user's username.
     * @since 1.0
     */
    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

}
