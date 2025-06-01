package lightning_productivity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegistrationPage {
	@FXML
	private Button acceptButton;
	@FXML
	private Button rejectButton;
	
	public void accept(ActionEvent e) {
		((Stage) ((Button) e.getSource()).getScene().getWindow()).setScene(App.mainPage);
	}
}
