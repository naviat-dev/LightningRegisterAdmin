package lightning_productivity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegistrationPage {
	@FXML
	private Button back;
	
	public void back(ActionEvent e) {
		((Stage) ((Button) e.getSource()).getScene().getWindow()).setScene(App.mainPage);
	}
}
