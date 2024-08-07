package lightning_productivity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        scene = new Scene(loadFXML("MainFrame"), Color.rgb(203, 51, 152));
        stage.setScene(scene);

        stage.setTitle("LightningRegister Admin");
        stage.getIcons().add(new Image(getClass().getResource("logo.png").toURI().toString()));
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
        // We will need to have passwords so that unauthorised people cannot screw things up
        ArrayList<String> validPasswords = new ArrayList<String>();
        Scanner getPasswords = new Scanner(
                new File("lightning_register_admin\\src\\main\\java\\lightning_productivity\\validId.txt"));
        while (getPasswords.hasNextLine()) {
            validPasswords.add(getPasswords.nextLine());
        }
        getPasswords.close();

        // Register all fonts needed for the program
        File[] fonts = new File("lightning_register_admin\\src\\main\\java\\lightning_productivity\\fonts").listFiles();
        for (File font : fonts) {
            // new Font();
        }

        launch();
    }

}