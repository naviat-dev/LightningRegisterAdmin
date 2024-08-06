package lightning_productivity;


import java.util.ArrayList;

public class Party {
    private ArrayList<Person> members;

    /**
     * <p>
     *     Creates a new Party.
     * </p>
     * @param members members of the Party
    */
    public Party(ArrayList<Person> members) {
        this.members = members;
    }

    /**
     * <p>
     *     Adds a Person to the Party.
     * </p>
     * @param person Person to add
     * @return true if the Person was successfully added
    */
    public boolean addPerson(Person person) {
        return this.members.add(person);
    }
}
