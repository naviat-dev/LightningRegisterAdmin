package lightning_productivity;


public class Person {
    private String[] name;
    private String ageGroup;
    private String region;
    private String state;
    private String ID;
    private String email;
    private String phoneNumber;

    /**
     * <p>
     *     Creates a new Person.
     * </p>
     * @param name Person's name
     * @param ageGroup Person's age group
     * @param region Person's region
     * @param ID Person's unique ID
     * @param email Person's email
     * @param phoneNumber Person's phone number
     * @param state Person's state
     */
    public Person(String[] name, String ageGroup, String region, String ID , String email, String phoneNumber, String state) {
        this.name = name;
        this.ageGroup = ageGroup;
        this.region = region;
        this.ID = ID;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.state = state;
        // TODO: create a new badge for a Person as soon as a new Person is created
    }

    /**
     * <p>
     *     Gets the name of the Person.
     * </p>
     * @return Person's name
     */
    public String[] getName() {
        return this.name;
    }

    /**
     * <p>
     *     Gets the age group of the Person.
     * </p>
     * @return Person's age group
     */
    public String getAgeGroup() {
        return this.ageGroup;
    }

    /**
     * <p>
     *     Gets the region of the Person.
     * </p>
     * @return Person's region
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * <p>
     *     Gets the email of the Person.
     * </p>
     * @return Person's email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * <p>
     *     Gets the phone number of the Person.
     * </p>
     * @return Person's phone number
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * <p>
     *     Gets the state of the Person.
     * </p>
     * @return Person's state
     */
    public String getState() {
        return this.state;
    }

    /**
     * <p>
     *     Gets the unique ID of the Person.
     * </p>
     * @return Person's unique ID
     */
    public String toString() {
        return this.ID;
    }
}
