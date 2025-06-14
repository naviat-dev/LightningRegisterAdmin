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
import javafx.scene.layout.GridPane;
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
	private GridPane info;
	@FXML
	private Label firstName;
	@FXML
	private Label lastName;
	@FXML
	private Label phone;
	@FXML
	private Label email;
	@FXML
	private Label age;
	@FXML
	private Label region;
	@FXML
	private Label remarks;
	@FXML
	private Button accept;
	@FXML
	private Button reject;

	public void initialize() {
		search.setVisible(true);
		labelTitle.setVisible(true);
		info.setVisible(false);
		badge.setVisible(false);
		noRegistrationFound.setVisible(false);

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
				List<Object> registrationList = App.registrations.get(regionIndex[i]).get(code);
				App.generateBadge(registrationList);
				badge.setVisible(true);
				info.setVisible(true);
				search.setVisible(false);
				labelTitle.setVisible(false);
				noRegistrationFound.setVisible(false);
				badge.setImage(new Image(new java.io.File(App.TEMP_DIR + "badge-raster.png").toURI().toString()));
				firstName.setText((String) registrationList.get(App.COLUMN.get("firstName")));
				lastName.setText((String) registrationList.get(App.COLUMN.get("lastName")));
				phone.setText((String) registrationList.get(App.COLUMN.get("phone")));
				email.setText((String) registrationList.get(App.COLUMN.get("email")));
				age.setText(String.valueOf(registrationList.get(App.COLUMN.get("age"))));
				region.setText(App.SHEETS.get(regionIndex[i]));
				// remarks.setText(App.registrations.get(regionIndex[i]).get(code).get(App.COLUMN.get("remarks")).toString());
				return;
				// initialize();
			}
		}
		// If no registration found, show the message
		noRegistrationFound.setVisible(true);
	}
}
