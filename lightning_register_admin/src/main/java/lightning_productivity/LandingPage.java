package lightning_productivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

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
		status.setText("Authenticating Google Sheets...");
		try {
			App.SHEETS_SERVICE = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new AuthorizationCodeInstalledApp(new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(new FileInputStream("lightning_register_admin\\src\\main\\resources\\credentials.json"))), Arrays.asList(SheetsScopes.SPREADSHEETS)).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(System.getProperty("user.home") + "/.credentials/sheets.googleapis.com-java-quickstart"))).setAccessType("offline").setApprovalPrompt("force").build(), new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user")).setApplicationName(App.APPLICATION_NAME).build();
		} catch (GeneralSecurityException | IOException e) {
			status.setText("Fatal error authenticating Google Sheets.");
		}
		status.setText("Reading Google Sheets data...");
		try {
			App.loadSheetData();
		} catch (IOException e) {
			status.setText("Fatal error reading Google Sheets data.");
		}
		status.setText("Authenticating Google Gmail...");
		try {
			App.GMAIL_SERVICE = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new AuthorizationCodeInstalledApp(new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(new FileInputStream("lightning_register_admin\\src\\main\\resources\\credentials.json"))), List.of("https://www.googleapis.com/auth/gmail.send")).setAccessType("offline").build(), new LocalServerReceiver()).authorize("user")).setApplicationName(App.APPLICATION_NAME).build();
		} catch (GeneralSecurityException | IOException e) {
			status.setText("Fatal error authenticating Google Gmail.");
		}
		status.setText("Ready!");
		
		Platform.runLater(() -> {
			((Stage)status.getScene().getWindow()).setScene(App.mainPage);
		});
	}
}