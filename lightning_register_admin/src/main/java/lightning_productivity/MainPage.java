package lightning_productivity;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainPage implements Initializable {
	@FXML
	private Button refresh;
	@FXML
	private Button back;
	@FXML
	private TabPane tabs;
	@FXML
	private VBox tables;
	@FXML
	private Tab region1Select;
	@FXML
	private Tab region2Select;
	@FXML
	private Tab region3Select;
	@FXML
	private Tab region4Select;
	@FXML
	private Tab regionCNSelect;
	@FXML
	private Tab regionCRSelect;
	@FXML
	private TitledPane processedRegistrationsPane;
	@FXML
	private TableView<SimpleStringProperty[]> processedRegistrationsTable;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedDate;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedId;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedFlag;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedFirstName;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedLastName;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedEmail;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedPhone;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedGender;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedState;
	@FXML
	private TableColumn<SimpleStringProperty[], String> processedAge;
	@FXML
	private TitledPane flaggedRegistrationsPane;
	@FXML
	private Button unflag;
	@FXML
	private TableView<SimpleStringProperty[]> flaggedRegistrationsTable;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedDate;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedId;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedFlag;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedFirstName;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedLastName;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedEmail;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedPhone;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedGender;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedState;
	@FXML
	private TableColumn<SimpleStringProperty[], String> flaggedAge;
	@FXML
	private TitledPane unprocessedRegistrationsPane;
	@FXML
	private Button process;
	@FXML
	private TableView<SimpleStringProperty[]> unprocessedRegistrationsTable;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedDate;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedId;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedFlag;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedFirstName;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedLastName;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedEmail;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedPhone;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedGender;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedState;
	@FXML
	private TableColumn<SimpleStringProperty[], String> unprocessedAge;

	private ObservableList<SimpleStringProperty[]> processedRegistrations;
	private SimpleStringProperty processedRegistrationsTitle;
	private ObservableList<SimpleStringProperty[]> flaggedRegistrations;
	private SimpleStringProperty flaggedRegistrationsTitle;
	private SimpleStringProperty unflagTitle;
	private ObservableList<SimpleStringProperty[]> unprocessedRegistrations;
	private SimpleStringProperty unprocessedRegistrationsTitle;
	private SimpleStringProperty processTitle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		processedRegistrations = FXCollections.observableArrayList();
		flaggedRegistrations = FXCollections.observableArrayList();
		unprocessedRegistrations = FXCollections.observableArrayList();
		App.ACTIVE_REGION = "REGION_1";
		processedDate.setCellValueFactory(cellData -> cellData.getValue()[0]);
		processedId.setCellValueFactory(cellData -> cellData.getValue()[1]);
		processedFlag.setCellValueFactory(cellData -> cellData.getValue()[2]);
		processedFirstName.setCellValueFactory(cellData -> cellData.getValue()[3]);
		processedLastName.setCellValueFactory(cellData -> cellData.getValue()[4]);
		processedEmail.setCellValueFactory(cellData -> cellData.getValue()[5]);
		processedPhone.setCellValueFactory(cellData -> cellData.getValue()[6]);
		processedGender.setCellValueFactory(cellData -> cellData.getValue()[7]);
		processedState.setCellValueFactory(cellData -> cellData.getValue()[8]);
		processedAge.setCellValueFactory(cellData -> cellData.getValue()[9]);
		flaggedDate.setCellValueFactory(cellData -> cellData.getValue()[0]);
		flaggedId.setCellValueFactory(cellData -> cellData.getValue()[1]);
		flaggedFlag.setCellValueFactory(cellData -> cellData.getValue()[2]);
		flaggedFirstName.setCellValueFactory(cellData -> cellData.getValue()[3]);
		flaggedLastName.setCellValueFactory(cellData -> cellData.getValue()[4]);
		flaggedEmail.setCellValueFactory(cellData -> cellData.getValue()[5]);
		flaggedPhone.setCellValueFactory(cellData -> cellData.getValue()[6]);
		flaggedGender.setCellValueFactory(cellData -> cellData.getValue()[7]);
		flaggedState.setCellValueFactory(cellData -> cellData.getValue()[8]);
		flaggedAge.setCellValueFactory(cellData -> cellData.getValue()[9]);
		unprocessedDate.setCellValueFactory(cellData -> cellData.getValue()[0]);
		unprocessedId.setCellValueFactory(cellData -> cellData.getValue()[1]);
		unprocessedFlag.setCellValueFactory(cellData -> cellData.getValue()[2]);
		unprocessedFirstName.setCellValueFactory(cellData -> cellData.getValue()[3]);
		unprocessedLastName.setCellValueFactory(cellData -> cellData.getValue()[4]);
		unprocessedEmail.setCellValueFactory(cellData -> cellData.getValue()[5]);
		unprocessedPhone.setCellValueFactory(cellData -> cellData.getValue()[6]);
		unprocessedGender.setCellValueFactory(cellData -> cellData.getValue()[7]);
		unprocessedState.setCellValueFactory(cellData -> cellData.getValue()[8]);
		unprocessedAge.setCellValueFactory(cellData -> cellData.getValue()[9]);
		processedRegistrationsTable.setItems(processedRegistrations);
		flaggedRegistrationsTable.setItems(flaggedRegistrations);
		unprocessedRegistrationsTable.setItems(unprocessedRegistrations);

		processedRegistrationsTitle = new SimpleStringProperty("Processed Registrations (" + processedRegistrations.size() + ")");
		flaggedRegistrationsTitle = new SimpleStringProperty("Flagged Registrations (" + flaggedRegistrations.size() + ")");
		unprocessedRegistrationsTitle = new SimpleStringProperty("Unprocessed Registrations (" + unprocessedRegistrations.size() + ")");
		processTitle = new SimpleStringProperty("Process " + unprocessedRegistrations.size() + " Registrations");
		unflagTitle = new SimpleStringProperty("Unflag " + flaggedRegistrations.size() + " Registrations");
		processedRegistrationsPane.textProperty().bind(processedRegistrationsTitle);
		flaggedRegistrationsPane.textProperty().bind(flaggedRegistrationsTitle);
		unprocessedRegistrationsPane.textProperty().bind(unprocessedRegistrationsTitle);
		process.textProperty().bind(processTitle);
		unflag.textProperty().bind(unflagTitle);
		
		tabs.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
			if (oldTab.getContent() instanceof Pane) {
				Pane oldPane = (Pane) oldTab.getContent();
				oldPane.getChildren().remove(tables);
			}
			if (newTab.getContent() instanceof Pane) {
				Pane newPane = (Pane) newTab.getContent();
				newPane.getChildren().add(tables);
			}
			try {
				regionSwitch(newTab);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		try {
			update(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears the current tables and repopulates them based on the current region in the Google Sheets. This method first clears the current tables. Then it retrieves the current region from the Google Sheets and stores the values in a HashMap. It then iterates over the key set of the HashMap and creates a SimpleStringProperty array for each registration. If the registration has an ID, it is added to the processed table. If the registration has a flag, it is added to the flagged table. Otherwise, it is added to the unprocessed table.
	 * 
	 * @throws Exception
	 */
	private void update(boolean flag) throws Exception {
		processedRegistrations.clear();
		flaggedRegistrations.clear();
		unprocessedRegistrations.clear();
		App.loadSheetData();
		HashMap<String, List<Object>> registrations = App.registrations.get(App.ACTIVE_REGION);
		if (flag) {
			App.duplicateUpdate();
		}
		List<String> keySet = new ArrayList<>(registrations.keySet());
		for (int i = 0; i < keySet.size(); i++) {
			List<Object> current = registrations.get(keySet.get(i));
			SimpleStringProperty[] currentProp = new SimpleStringProperty[10];
			for (int j = 0; j < currentProp.length; j++) {
				currentProp[j] = new SimpleStringProperty(current.get(j).toString());
			}
			if (((String)current.get(App.COLUMN.get("id"))).length() > 6) {
				processedRegistrations.add(currentProp);
			} else {
				if (((String)current.get(App.COLUMN.get("flag"))).length() > 0) {
					flaggedRegistrations.add(currentProp);
				} else {
					unprocessedRegistrations.add(currentProp);
				}
			}
		}
		processedRegistrationsTitle.set("Processed Registrations (" + processedRegistrations.size() + ")");
		flaggedRegistrationsTitle.set("Flagged Registrations (" + flaggedRegistrations.size() + ")");
		unprocessedRegistrationsTitle.set("Unprocessed Registrations (" + unprocessedRegistrations.size() + ")");
		processTitle.set("Process " + unprocessedRegistrations.size() + " Registrations");
		unflagTitle.set("Unflag " + flaggedRegistrations.size() + " Registrations");
	}

	/**
	 * Handles the button click event for the region selection buttons. Retrieves the button's ID and converts it to uppercase. It then replaces "SELECT" with nothing and "REGION" with "REGION_", and assigns the result to App.ACTIVE_REGION. Finally, it calls update() to update the table views.
	 * 
	 * @param e the ActionEvent object associated with the button click
	 * @throws Exception
	 */
	@FXML
	private void regionSwitch(Tab src) throws Exception {
		// TODO: change old button back to old color
		App.ACTIVE_REGION = src.getId().toUpperCase().replace("SELECT", "").replace("REGION", "REGION_");
		// src.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
		update(true);
	}

	@FXML
	private void process() throws Exception {
		App.batchUpdate();
		refresh(); // TODO: Change this to minimize API calls
	}

	@FXML
	private void unflag() throws Exception {
		App.flagRemove();
		App.loadSheetData();
		update(false);
	}

	@FXML
	private void refresh() throws Exception {
		App.loadSheetData();
		update(true);
	}
}
