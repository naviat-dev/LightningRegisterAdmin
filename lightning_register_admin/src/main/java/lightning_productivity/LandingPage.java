package lightning_productivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LandingPage {
    @FXML
    private TextField passwordField;
    @FXML
    private Label incorrectPassword;

    /**
     * Submits the password and checks if it is valid.
     *
     * @param e the ActionEvent triggered by the submit button
     * @throws IOException
     */
    public void submitPassword(ActionEvent e) throws IOException {
        System.out.println("password submitted");
        // We will need to have passwords so that unauthorised people cannot screw
        // things up
        ArrayList<String> validPasswords = new ArrayList<String>();
        Scanner getPasswords = new Scanner(
                new File("lightning_register_admin\\src\\main\\java\\lightning_productivity\\validId.txt"));
        while (getPasswords.hasNextLine()) {
            validPasswords.add(getPasswords.nextLine());
        }
        getPasswords.close();
        String enteredPassword = passwordField.getText();
        if (validPasswords.contains(enteredPassword)) {
            System.out.println("password submitted - correct password");
            // App.setRoot("LandingPage");
        } else {
            System.out.println("password submitted - incorrect password");
            incorrectPassword.setText("Incorrect Password");
        }
    }
}