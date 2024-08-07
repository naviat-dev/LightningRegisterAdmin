package lightning_productivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {
    @FXML
    // private 

    /**
     * Submits the password and checks if it is valid.
     *
     * @param  e  the ActionEvent triggered by the submit button
     * @throws FileNotFoundException  if the validId.txt file cannot be found
     */
    public void submitPassword (ActionEvent e) throws FileNotFoundException {
        System.out.println("password submitted");
        // We will need to have passwords so that unauthorised people cannot screw things up
        ArrayList<String> validPasswords = new ArrayList<String>();
        Scanner getPasswords = new Scanner(new File("lightning_register_admin\\src\\main\\java\\lightning_productivity\\validId.txt"));
        while (getPasswords.hasNextLine()) {
            validPasswords.add(getPasswords.nextLine());
        }
        getPasswords.close();
    }
}