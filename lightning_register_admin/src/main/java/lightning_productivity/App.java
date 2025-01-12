package lightning_productivity;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.pdf417.PDF417Writer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static Sheets SHEETS_SERVICE;
    private static Gmail GMAIL_SERVICE;
    private static String APPLICATION_NAME = "Dominion 2K25";
    private static String SPREADHSEET_ID = "17lFflosq1LDnoBsfdFmC8fUolmnPAWdcxWPB_URtb9s";
    private static HashMap<String, Integer> COLUMN;
    private static List<List<Object>> REGION_1;
    private static List<List<Object>> REGION_2;
    private static List<List<Object>> REGION_3;
    private static List<List<Object>> REGION_4;
    private static List<List<Object>> REGION_CA;
    private static List<List<Object>> REGION_CB;
    private static String[] TICKET;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        scene = new Scene(loadFXML("LandingPage"), Color.rgb(203, 51, 152));
        scene.getStylesheets().add(getClass().getResource("LandingPage.css").toExternalForm());
        stage.setScene(scene);

        stage.setTitle("LightningRegister Admin");
        stage.getIcons().add(new Image(getClass().getResource("lightning_register_admin/src/main/resources/img/logo.png").toURI().toString()));
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

    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws GeneralSecurityException, IOException, MessagingException, WriterException, TranscoderException, NoSuchAlgorithmException {
        COLUMN = new HashMap<>();
		COLUMN.put("id", 1);
		COLUMN.put("flag", 2);
		COLUMN.put("firstName", 3);
		COLUMN.put("lastName", 4);
		COLUMN.put("email", 5);
		COLUMN.put("phone", 6);
		COLUMN.put("gender", 7);
		COLUMN.put("age", 8);
		COLUMN.put("size", 9);
		TICKET = new String[50];
		Scanner ticketReader = new Scanner(new File("lightning_register_admin\\src\\main\\resources\\ticket-template.svg"));
		int lineNumber = 0;
		while (ticketReader.hasNext()) {
			TICKET[lineNumber] = ticketReader.nextLine();
			lineNumber++;
		}
		ticketReader.close();

		SHEETS_SERVICE = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new AuthorizationCodeInstalledApp(new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(new FileInputStream("lightning_register_admin\\src\\main\\resources\\credentials.json"))), Arrays.asList(SheetsScopes.SPREADSHEETS)).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(System.getProperty("user.home") + "/.credentials/sheets.googleapis.com-java-quickstart"))).setAccessType("offline").build(), new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user")).setApplicationName(APPLICATION_NAME).build();
		GMAIL_SERVICE = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new AuthorizationCodeInstalledApp(new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(new FileInputStream("lightning_register_admin\\src\\main\\resources\\credentials.json"))), List.of("https://www.googleapis.com/auth/gmail.send")).setAccessType("offline").build(), new LocalServerReceiver()).authorize("user")).setApplicationName(APPLICATION_NAME).build();
		
		// loadSheetData();
		String range = "Region 3!A1:Z1000";

		List<List<Object>> values = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, range).execute().getValues();
		for (int i = 1; i < values.size(); i++) {
			List<Object> current = values.get(i);
			if (!current.get(1).toString().equals("") || !current.get(2).toString().equals("")) {
				continue;
			} else {
				File svgPath = new File("lightning_register_admin\\src\\main\\resources\\ticket-temp.svg");
				String currentID = generateID(current.get(COLUMN.get("firstName")).toString() + current.get(COLUMN.get("lastName")).toString() + current.get(COLUMN.get("email")).toString() + current.get(COLUMN.get("phone")).toString() + current.get(COLUMN.get("gender")).toString() + current.get(COLUMN.get("age")).toString());
				generateTicket(current, currentID, svgPath);
				new PNGTranscoder().transcode(new TranscoderInput(new FileInputStream(svgPath)), new TranscoderOutput(new FileOutputStream("lightning_register_admin\\src\\main\\resources\\ticket-raster" + i + ".png")));
				svgPath.delete();
				sendMessage(GMAIL_SERVICE, "me", createTicketEmail(current, "lightning_register_admin\\src\\main\\resources\\ticket-raster" + i + ".png"));
				List<List<Object>> newID = Arrays.asList(Arrays.asList(currentID));
				ValueRange body = new ValueRange().setValues(newID);
				SHEETS_SERVICE.spreadsheets().values().update(SPREADHSEET_ID, "Region 3!B" + (i + 1), body).setValueInputOption("RAW").execute();
			}
		}

        launch();
    }

    public static MimeMessage createTicketEmail(List<Object> user, String ticketPath) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(user.get(COLUMN.get("email")).toString().replaceAll(" ", "")));
		email.setSubject("Congratulations! Here is your ticket for Dominion 2K25");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(
			"<b>Hey there, " + user.get(COLUMN.get("firstName")) + "!</b><br><br>" + 
			"Thanks for registering for the 2025 International Youth Convention. You have become part of a community of thousands of people just like you.<br><br>" + 
			"Below you will find your ticket for the convention.<br><br>" + 
			"<img src=\"cid:image1\" style=\"width: 90%; height: auto;\"><br><br>" + 
			"<b>Please do not register again.</b> If you believe that you have made a mistake on your form, or have any questions about your registration, please contact us at registration@mfmyouthministries.org.<br><br>" + 
			"Thank you, and have a wonderful day!",
			"text/html");

		// Create the image part
		MimeBodyPart imagePart = new MimeBodyPart();
		imagePart.attachFile(new File(ticketPath));
		imagePart.setContentID("<image1>");
		imagePart.setDisposition(MimeBodyPart.INLINE);

		// Combine parts into a multipart/related message
		Multipart multipart = new MimeMultipart("related");
		multipart.addBodyPart(textPart);
		multipart.addBodyPart(imagePart);

		email.setContent(multipart);
		return email;
	}

	public static void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);

		service.users().messages().send(userId, message).execute();
		System.out.println("Email sent successfully!");
	}

	public static String generateID(String input) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		long number = 0;
		for (int i = 0; i < Math.min(8, hashBytes.length); i++) {
			number = (number << 8) | (hashBytes[i] & 0xFF);
		}
		number = Math.abs(number);
		StringBuilder result = new StringBuilder();
		int base = 34;
		char[] digits = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
		for (int i = 0; i < 13; i++) {
			long remainder = (long) number % base;
			result.append(digits[(int) remainder]);
			number /= base;
		}
		return result.reverse().toString();
	}

	public static void generateTicket(List<Object> registration, String id, File path) throws WriterException, IOException, NoSuchAlgorithmException {
		String[] currentTicket = TICKET.clone();
		File barcodeSave = new File("lightning_register_admin\\src\\main\\resources\\barcode-temp.png");
		MatrixToImageWriter.writeToPath(new PDF417Writer().encode(id, BarcodeFormat.PDF_417, 1000, 1000), "PNG", barcodeSave.toPath());
		BufferedImage barcode = ImageIO.read(barcodeSave);
		barcode = barcode.getSubimage(30, 30, barcode.getWidth() - 60, barcode.getHeight() - 60);

		int width = barcode.getHeight();
        int height = barcode.getWidth();
        BufferedImage barcodeRot = new BufferedImage(width, height, barcode.getType());
        Graphics2D g2d = barcodeRot.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.translate(width / 2.0, height / 2.0);
        transform.rotate(Math.toRadians(90));
        transform.translate(-height / 2.0, -width / 2.0);
        g2d.setTransform(transform);
        g2d.drawImage(barcode, 0, 0, null);
        g2d.dispose();
		
		ImageIO.write(barcodeRot, "png", barcodeSave);
        String base64Image = Base64.getEncoder().encodeToString(Files.readAllBytes(barcodeSave.toPath()));
		
		currentTicket[33] = "\t\t<tspan sodipodi:role=\"line\" id=\"tspan469\" x=\"28.004101\" y=\"272.10001\" style=\"stroke-width:7.40585;fill:#c3b53c;fill-opacity:1\">" + registration.get(COLUMN.get("lastName")).toString().toUpperCase() + "</tspan>";
		currentTicket[36] = "\t\t<tspan sodipodi:role=\"line\" id=\"tspan362\" x=\"28.004101\" y=\"193.94911\" style=\"stroke-width:7.40585;fill:#c3b53c;fill-opacity:1\">" + registration.get(COLUMN.get("firstName")).toString().toUpperCase() + "</tspan>";
		currentTicket[45] = "\t<image width=\"78\" height=\"336\" preserveAspectRatio=\"none\" xlink:href=\"data:image/png;base64," + base64Image + "\" id=\"image3110\" x=\"1066\" y=\"30\" inkscape:label=\"barcode\" />";
		currentTicket[47] = "\t\t<tspan sodipodi:role=\"line\" id=\"tspan485\" x=\"-190\" y=\"1190\" text-anchor=\"middle\" style=\"stroke-width:1.51053;-inkscape-font-specification:'Inter Semi-Bold';font-family:Inter;font-weight:600;font-style:normal;font-stretch:normal;font-variant:normal\">" + id + "</tspan>";
		StringBuilder ticketBuilder = new StringBuilder();
		for (String string : currentTicket) {
			ticketBuilder.append(string + "\n");
		}
		PrintStream ps = new PrintStream(path);
		ps.print(new String(ticketBuilder));
		ps.close();
	}

	public static void loadSheetData() throws IOException {
		REGION_1 = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, "Region 1!A1:Z1000").execute().getValues();
		REGION_2 = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, "Region 2!A1:Z1000").execute().getValues();
		REGION_3 = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, "Region 3!A1:Z1000").execute().getValues();
		REGION_4 = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, "Region 4!A1:Z1000").execute().getValues();
		REGION_CA = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, "Region CA!A1:Z1000").execute().getValues();
		REGION_CB = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, "Region CB!A1:Z1000").execute().getValues();
	}
}