package lightning_productivity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.batik.transcoder.TranscoderException;

import com.google.zxing.WriterException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RegistrationPage {
	@FXML
	private VBox search;
	@FXML
	private Label labelTitle;
	@FXML
	private Label noRegistrationFound;
	@FXML
	private TextField id;
	@FXML
	private ImageView badge;
	@FXML
	private Label remarks;
	@FXML
	private Button accept;
	@FXML
	private Button reject;

	private String currentRegion;
	private int currentRow;

	public void initialize() throws IOException {
		App.loadSheetData();
		search.setVisible(true);
		labelTitle.setVisible(true);
		badge.setVisible(false);
		accept.setVisible(false);
		reject.setVisible(false);
		remarks.setVisible(false);
		noRegistrationFound.setVisible(false);
		id.setText("");

		// Add listener to id TextField
		if (id != null) {
			id.textProperty().addListener((obs, oldText, newText) -> {
				if (newText.length() == 13) {
					try {
						onIdEntered(newText);
					} catch (NoSuchAlgorithmException | WriterException | IOException | TranscoderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Call your method here
				}
			});
		}
	}

	// Example method to run when 13 characters are entered
	private void onIdEntered(String code) throws NoSuchAlgorithmException, WriterException, IOException, TranscoderException {
		String[] regionIndex = { "REGION_1", "REGION_2", "REGION_3", "REGION_4", "REGION_CN", "REGION_CR" };
		for (int i = 0; i < regionIndex.length; i++) {
			if (App.registrations.get(regionIndex[i]).containsKey(code)) {
				id.setText("");
				currentRegion = App.SHEETS.get(regionIndex[i]);
				List<Object> registrationList = App.registrations.get(regionIndex[i]).get(code);
				currentRow = (Integer) registrationList.get(App.COLUMN.get("date"));
				System.out.println(registrationList.toString());
				App.generateBadge(registrationList);
				badge.setVisible(true);
				accept.setVisible(true);
				reject.setVisible(true);
				noRegistrationFound.setVisible(false);
				remarks.setVisible(true);
				remarks.setStyle("-fx-text-fill: red;");
				remarks.setText(registrationList.get(App.COLUMN.get("flag")).toString());
				badge.setImage(new Image(new java.io.File(App.TEMP_DIR + "badge-raster.png").toURI().toString()));
				return;
			}
		}
		// If no registration found, show the message
		noRegistrationFound.setVisible(true);
	}

	@FXML
	private void acceptRegistration() throws Exception {
		List<List<Object>> value = List.of(List.of("Accepted at " + java.time.LocalDateTime.now()));
		App.writeSheetData(App.SPREADHSEET_ID, currentRegion + "!C" + currentRow, value, App.CREDENTIAL);
		App.printBadge();
		// Reset the UI
		initialize();
	}
}
