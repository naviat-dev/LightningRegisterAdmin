package lightning_productivity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        scene = new Scene(loadFXML("LandingPage"), Color.rgb(203, 51, 152));
        scene.getStylesheets().add(getClass().getResource("LandingPage.css").toExternalForm());
        stage.setScene(scene);

        stage.setTitle("LightningRegister Admin");
        stage.getIcons().add(new Image(getClass().getResource("img/logo.png").toURI().toString()));
        // stage.setFullScreen(true);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Register all fonts needed for the program
        File[] fonts = new File("lightning_register_admin\\src\\main\\java\\lightning_productivity\\fonts").listFiles();
        for (File font : fonts) {
            // TODO: store fonts locally within the program
        }

        launch();
    }

}