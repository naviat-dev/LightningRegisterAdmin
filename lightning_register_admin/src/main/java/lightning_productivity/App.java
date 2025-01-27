package lightning_productivity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
// import org.w3c.dom.svg.SVGDocument;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.pdf417.PDF417Writer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	public static Scene landingPage;
	public static Scene mainPage;

	public static Sheets SHEETS_SERVICE;
	public static Gmail GMAIL_SERVICE;
	public static String APPLICATION_NAME = "Dominion 2K25";
	public static String SPREADHSEET_ID = "17lFflosq1LDnoBsfdFmC8fUolmnPAWdcxWPB_URtb9s";
	public static HashMap<String, Integer> COLUMN;
	public static HashMap<String, String> SHEETS;
	public static String ACTIVE_REGION;
	public static String[] TICKET;
	public static PrintService PRINTER;

	public static HashMap<String, HashMap<String, List<Object>>> registrations;

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException, GeneralSecurityException {
		landingPage = new Scene(loadFXML("LandingPage"));
		landingPage.getStylesheets().add(getClass().getResource("LandingPage.css").toExternalForm());
		mainPage = new Scene(loadFXML("MainPage"));
		stage.setScene(landingPage);
		stage.setTitle("LightningRegister Admin");
		// stage.getIcons().add(new Image(getClass().getResource("lightning_register_admin/src/main/resources/img/logo.png").toURI().toString()));
		// stage.setFullScreen(true);
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		landingPage.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) throws GeneralSecurityException, IOException, MessagingException, WriterException, TranscoderException, NoSuchAlgorithmException {
		// Initialise global variables
		// These must be initialised before launch() runs, otherwise problems
		COLUMN = new HashMap<>();
		COLUMN.put("date", 0);
		COLUMN.put("id", 1);
		COLUMN.put("flag", 2);
		COLUMN.put("firstName", 3);
		COLUMN.put("lastName", 4);
		COLUMN.put("email", 5);
		COLUMN.put("phone", 6);
		COLUMN.put("gender", 7);
		COLUMN.put("age", 8);
		COLUMN.put("size", 9);
		registrations = new HashMap<>();
		SHEETS = new HashMap<>();
		SHEETS.put("REGION_1", "Region 1");
		SHEETS.put("REGION_2", "Region 2");
		SHEETS.put("REGION_3", "Region 3");
		SHEETS.put("REGION_4", "Region 4");
		SHEETS.put("REGION_CN", "Canada");
		SHEETS.put("REGION_CR", "South America & Caribbean");
		TICKET = new String[50];
		Scanner ticketReader = new Scanner(new File("lightning_register_admin\\src\\main\\resources\\ticket-template.svg"));
		int lineNumber = 0;
		while (ticketReader.hasNext()) {
			TICKET[lineNumber] = ticketReader.nextLine();
			lineNumber++;
		}
		ticketReader.close();

		// Printer setup
		for (PrintService printer : PrintServiceLookup.lookupPrintServices(null, null)) {
			if (printer.getName().equalsIgnoreCase("EXAMPLE")) { // TODO: Change this to your printer name
				PRINTER = printer;
				break;
			}
		}
		System.out.println(PRINTER == null ? "Printer not found. Badge printing will be unavailable." : "Printer found. Badge printing will be available.");

		launch();
	}

	/**
	 * Creates a MimeMessage with a ticket attached to be sent to a registrant.
	 * 
	 * @param user       A row from the spreadsheet containing the registrant's information
	 * @param ticketPath The path to the ticket image to be sent
	 * @return A MimeMessage object with the email body and ticket attached
	 * @throws MessagingException If there is a problem creating the email
	 * @throws IOException        If there is a problem reading the ticket image
	 */
	public static MimeMessage createTicketEmail(List<Object> user, String ticketPath) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(user.get(COLUMN.get("email")).toString().replaceAll(" ", "")));
		email.setSubject("Congratulations! Here is your ticket for Dominion 2K25");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent("<b>NOTE: This email is solely for testing purposes only. You're getting this email because you included your email as part of our test registration form. These tickets are NOT valid to be submitted to Dominion 2K25. If you wish to be removed from the list, reply to this email and we'll get you sorted. Thanks!</b><br><br><br><b>Hey there, " + user.get(COLUMN.get("firstName")) + "!</b><br><br>" + "Thanks for registering for the 2025 International Youth Convention. You have become part of a community of thousands of people just like you.<br><br>" + "Below you will find your ticket for the convention.<br><br>" + "<img src=\"cid:image1\" style=\"width: 90%; height: auto;\"><br><br>" + "<b>Please do not register again.</b> If you believe that you have made a mistake on your form, or have any questions about your registration, please contact us at registration@mfmyouthministries.org.<br><br>" + "Thank you, and have a wonderful day!", "text/html");

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

	/**
	 * Send an email using the Gmail API.
	 * 
	 * @param service the Gmail service to use
	 * @param userId  the user ID to send the message to
	 * @param email   the email message to send
	 * @throws MessagingException if there's a problem sending the message
	 * @throws IOException        if there's a problem writing to the output stream
	 */
	public static void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);

		service.users().messages().send(userId, message).execute();
		System.out.println("Email sent successfully!");
	}

	/**
	 * Generates a unique 13-character alphanumeric identifier from the given string input. The identifier is a base-32 encoded hash of the input, with the following properties:
	 * <ul>
	 * <li>The identifier is case-insensitive</li>
	 * <li>The identifier is 13 characters long</li>
	 * <li>The identifier does not contain the characters "I" or "O"</li>
	 * <li>The identifier is unique for any given input string</li>
	 * </ul>
	 * 
	 * @param input the input string
	 * @return the generated identifier
	 * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
	 */
	public static String generateID(String input) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		long number = 0;
		for (int i = 0; i < Math.min(8, hashBytes.length); i++) {
			number = (number << 8) | (hashBytes[i] & 0xFF);
		}
		number = Math.abs(number);
		StringBuilder result = new StringBuilder();
		char[] digits = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
		int base = digits.length;
		for (int i = 0; i < 13; i++) {
			long remainder = (long) number % base;
			result.append(digits[(int) remainder]);
			number /= base;
		}
		return result.reverse().toString();
	}

	/**
	 * Generates a ticket image from the given registration information and unique identifier.
	 *
	 * @param registration the registration information
	 * @param id           the unique identifier for the ticket
	 * @param path         the path where the ticket image is to be saved
	 * @throws WriterException          if there is an error writing the ticket image
	 * @throws IOException              if there is an error reading or writing to the file system
	 * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
	 */
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

	/**
	 * Processes a batch update of registrations by generating tickets, sending emails, and updating the Google Sheets with generated IDs.
	 * <p>
	 * This method performs the following steps for each registration:
	 * </p>
	 * <ol>
	 * <li>Scans for updates and retrieves the list of registrations.</li>
	 * <li>Generates a unique ID for each registration.</li>
	 * <li>Creates a ticket image and saves it as a temporary SVG file.</li>
	 * <li>Converts the SVG file to a PNG image.</li>
	 * <li>Sends an email with the ticket as an attachment to the registrant.</li>
	 * <li>Updates the Google Sheets with the generated ID for the registration.</li>
	 * </ol>
	 * 
	 * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
	 * @throws IOException              if there is an error reading or writing files.
	 * @throws TranscoderException      if there is an error transcoding the SVG to PNG.
	 * @throws WriterException          if there is an error with the barcode writer.
	 * @throws MessagingException       if there is an error sending the email.
	 */
	public static void batchUpdate() throws NoSuchAlgorithmException, IOException, TranscoderException, WriterException, MessagingException {
		ArrayList<String> registrationUpdates = scanUpdate();
		for (int i = 0; i < registrationUpdates.size(); i+=2) {
			List<Object> current = registrations.get(ACTIVE_REGION).get(registrationUpdates.get(i));
			File svgPath = new File("lightning_register_admin\\src\\main\\resources\\ticket-temp.svg");
			String currentID = generateID(current.get(COLUMN.get("firstName")).toString() + current.get(COLUMN.get("lastName")).toString() + current.get(COLUMN.get("email")).toString() + current.get(COLUMN.get("phone")).toString() + current.get(COLUMN.get("gender")).toString() + current.get(COLUMN.get("age")).toString());
			generateTicket(current, currentID, svgPath);
			new PNGTranscoder().transcode(new TranscoderInput(new FileInputStream(svgPath)), new TranscoderOutput(new FileOutputStream("lightning_register_admin\\src\\main\\resources\\ticket-raster.png")));
			svgPath.delete();
			sendMessage(GMAIL_SERVICE, "me", createTicketEmail(current, "lightning_register_admin\\src\\main\\resources\\ticket-raster.png"));
			List<List<Object>> newID = Arrays.asList(Arrays.asList(currentID));
			ValueRange body = new ValueRange().setValues(newID);
			SHEETS_SERVICE.spreadsheets().values().update(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + registrationUpdates.get(i + 1), body).setValueInputOption("RAW").execute();
		}
	}

	/**
	 * Scans the active Google Sheet region for registrations with empty fields. This method retrieves the values from the active region of the Google Sheet and checks each row to identify registrations with empty fields at specific column indices. It collects the indices of such registrations and returns them as a list.
	 * 
	 * @return An ArrayList of integers representing the indices of registrations with empty fields in the active region of the Google Sheet.
	 * @throws IOException              If there is an error retrieving values from the Google Sheet.
	 * @throws NoSuchAlgorithmException If a required cryptographic algorithm is not available.
	 * @throws TranscoderException      If there is an error during SVG to PNG transcoding.
	 * @throws WriterException          If there is an error with the barcode writer.
	 * @throws MessagingException       If there is an error sending the email.
	 */
	public static ArrayList<String> scanUpdate() throws IOException, NoSuchAlgorithmException, TranscoderException, WriterException, MessagingException {
		ArrayList<String> emptyReg = new ArrayList<>();
		if (ACTIVE_REGION != null) {
			List<List<Object>> values = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:K1000").execute().getValues();
			for (int i = 1; i < values.size(); i++) {
				List<Object> current = values.get(i);
				if (current.get(1).toString().equals("") && current.get(2).toString().equals("")) {
					emptyReg.add((String) current.get(0));
					emptyReg.add("B" + (i + 1));
				}
			}
		}
		return emptyReg;
	}

	/**
	 * Loads the data from the Google Sheets document into memory.
	 * <p>
	 * This method is called at the start of the program and is used to populate the various REGION variables with the data from the Google Sheets document.
	 * <p>
	 * The data is loaded from the Google Sheets document using the Google Sheets API.
	 * <p>
	 * The data is loaded from the following ranges:
	 * <ul>
	 * <li>Region 1!A1:K1000
	 * <li>Region 2!A1:K1000
	 * <li>Region 3!A1:K1000
	 * <li>Region 4!A1:K1000
	 * <li>Canada!A1:K1000
	 * <li>South America & Caribbean!A1:K1000
	 * </ul>
	 * <p>
	 * The data is stored in a HashMap containing the following variables:
	 * <ul>
	 * <li>REGION_1
	 * <li>REGION_2
	 * <li>REGION_3
	 * <li>REGION_4
	 * <li>REGION_CN
	 * <li>REGION_CR
	 * </ul>
	 * <p>
	 * 
	 * @throws IOException if there is a problem loading the data from the Google Sheets document.
	 */
	public static void loadSheetData() throws IOException {
		registrations.put("REGION_1", new HashMap<>());
		registrations.put("REGION_2", new HashMap<>());
		registrations.put("REGION_3", new HashMap<>());
		registrations.put("REGION_4", new HashMap<>());
		registrations.put("REGION_CN", new HashMap<>());
		registrations.put("REGION_CR", new HashMap<>());
		String[] regionIndex = { "REGION_1", "REGION_2", "REGION_3", "REGION_4", "REGION_CN", "REGION_CR" };
		for (int i = 0; i < 6; i++) {
			try {
				List<List<Object>> registrationsTemp = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, SHEETS.get(regionIndex[i]) + "!A1:K1000").execute().getValues();

				for (int j = 1; j < registrationsTemp.size(); j++) {
					if (registrationsTemp.get(j).get(COLUMN.get("id")).toString().isEmpty()) {
						registrations.get(regionIndex[i]).put(registrationsTemp.get(j).get(COLUMN.get("date")).toString(), registrationsTemp.get(j));
					} else {
						registrations.get(regionIndex[i]).put(registrationsTemp.get(j).get(COLUMN.get("id")).toString(), registrationsTemp.get(j));
					}
				}
			} catch (Exception e) {
				System.err.println("Error while fetching data for " + regionIndex[i] + ": " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/*
	 * public static void test() { // Load SVG file String parser = XMLResourceDescriptor.getXMLParserClassName(); SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser); SVGDocument svgDoc = factory.createSVGDocument(new File("ticket.svg").toURI().toString()); // Modify the text element dynamically Element textElement = svgDoc.getElementById("textElementId"); // Use your text element ID textElement.setTextContent("DynamicText"); // Render SVG and measure text size GVTBuilder builder = new GVTBuilder(); BridgeContext ctx = new BridgeContext(svgDoc); GraphicsNode graphicsNode = builder.build(ctx, svgDoc); GraphicsNode textNode = ctx.getGraphicsNode(textElement); if (textNode != null) { float textWidth = textNode.getBounds().getWidth(); System.out.println("Text Width: " + textWidth + "px"); } else { System.out.println("Text element not found!"); } }
	 */
}