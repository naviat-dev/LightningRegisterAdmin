package lightning_productivity;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LandingPage implements Initializable {
	@FXML
	private Button checkIn;
	@FXML
	private Button process;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			App.loadSheetData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void checkIn() {
		((Stage) checkIn.getScene().getWindow()).setScene(App.registrationPage);
	}

	@FXML
	public void process() {
		((Stage) process.getScene().getWindow()).setScene(App.mainPage);
	}
}