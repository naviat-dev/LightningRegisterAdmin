package lightning_productivity;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CheckPage {
	@FXML
	private ImageView ticketView;
	@FXML
	private Label firstName;
	@FXML
	private Label lastName;
	@FXML
	private Label gender;
	@FXML
	private Label email;
	@FXML
	private Label phone;
	@FXML
	private Label age;
	@FXML
	private Button accept;
	@FXML
	private Button flag;
	@FXML
	private Button reject;

	/**
	 * Initializes the UI elements of the modal window with the given registration's information.
	 * 
	 * @param registration the registration information to display
	 */
	@FXML
	public void initializeData(List<Object> registration) {
		ticketView.setImage(new Image(new File(App.TEMP_DIR + "ticket-raster.png").toURI().toString()));
		firstName.setText(registration.get(App.COLUMN.get("firstName")).toString().trim());
		lastName.setText(registration.get(App.COLUMN.get("lastName")).toString().trim());
		gender.setText(registration.get(App.COLUMN.get("gender")).toString().trim());
		email.setText(registration.get(App.COLUMN.get("email")).toString().trim());
		phone.setText(registration.get(App.COLUMN.get("phone")).toString().trim());
		age.setText(registration.get(App.COLUMN.get("age")).toString().trim());
	}

	@FXML
	public void buttonAction(ActionEvent e) {
		Button src = (Button) e.getSource();
		App.action = src.getId();
		((Stage) src.getScene().getWindow()).close();
	}
}
