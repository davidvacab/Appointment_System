package Model;

/**
 * Represents a customer.
 * @author David Vaca
 * @since 1.0
 */
public class Customer {
    private int customerID;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String division;
    private String country;

    /**
     * Creates a customer with no parameter values.
     * @since 1.0
     */
    public Customer(){
    }

    /**
     * Gets the customer's ID.
     * @return An int representing the customer's ID.
     * @since 1.0
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets the customer's ID.
     * @param customerID An int containing the customer's ID.
     * @since 1.0
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the customer's name.
     * @return A String representing the customer's name.
     * @since 1.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer's name.
     * @param name A String containing the customer's name.
     * @since 1.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the customer's address.
     * @return A String representing the customer's address.
     * @since 1.0
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the customer's address.
     * @param address A String containing the customer's address.
     * @since 1.0
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the customer's postal code.
     * @return A String representing the customer's postal code.
     * @since 1.0
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the customer's postal code.
     * @param postalCode A String containing the customer's postal code.
     * @since 1.0
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the customer's phone number.
     * @return A String representing the customer's phone number.
     * @since 1.0
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer's phone number.
     * @param phone A String containing the customer's phone number.
     * @since 1.0
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the customer's division.
     * @return A String representing the customer's division.
     * @since 1.0
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the customer's division.
     * @param division A String containing the customer's division.
     * @since 1.0
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the customer's country.
     * @return A String representing the customer's country.
     * @since 1.0
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the customer's country.
     * @param country A String containing the customer's country.
     * @since 1.0
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
