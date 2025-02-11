package lightning_productivity;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LandingPage implements Initializable {
	@FXML
	private Label status;

	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		status.setText("Reading Google Sheets data...");
		try {
			App.loadSheetData();
		} catch (IOException e) {
			status.setText("Fatal error reading Google Sheets data.");
		}
		status.setText("Ready!");
		
		Platform.runLater(() -> {
			((Stage) status.getScene().getWindow()).setScene(App.mainPage);
		});
	}
}