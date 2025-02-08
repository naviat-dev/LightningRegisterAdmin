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

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {

	public static Scene landingPage;
	public static Scene mainPage;
	public static Scene registrationPage;
	public static String action;

	public static Sheets SHEETS_SERVICE;
	public static Gmail GMAIL_SERVICE;
	public static String APPLICATION_NAME = "Dominion 2K25";
	public static String SPREADHSEET_ID = "17lFflosq1LDnoBsfdFmC8fUolmnPAWdcxWPB_URtb9s";
	public static HashMap<String, Integer> COLUMN;
	public static HashMap<String, String> SHEETS;
	public static String ACTIVE_REGION;
	public static String[] TICKET;
	public static PrintService PRINTER;
	public static HashMap<Character, Double> FONT_SIZE;

	public static HashMap<String, HashMap<String, List<Object>>> registrations;

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException, GeneralSecurityException {
		landingPage = new Scene(loadFXML("LandingPage"));
		landingPage.getStylesheets().add(getClass().getResource("LandingPage.css").toExternalForm());
		mainPage = new Scene(loadFXML("MainPage"));
		registrationPage = new Scene(loadFXML("RegistrationPage"));
		stage.setScene(landingPage);
		stage.setTitle("LightningRegister Admin");
		// stage.getIcons().add(new Image(getClass().getResource("lightning_register_admin/src/main/resources/img/logo.png").toURI().toString()));
		// stage.setFullScreen(true);
		stage.show();
	}

	public static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) throws GeneralSecurityException, IOException, MessagingException, WriterException, TranscoderException, NoSuchAlgorithmException {
		// Initialise global variables
		// These must be initialised before launch() runs, otherwise problems
		action = "";
		COLUMN = new HashMap<>();
		COLUMN.put("date", 0);
		COLUMN.put("id", 1);
		COLUMN.put("flag", 2);
		COLUMN.put("firstName", 3);
		COLUMN.put("lastName", 4);
		COLUMN.put("email", 5);
		COLUMN.put("phone", 6);
		COLUMN.put("gender", 7);
		COLUMN.put("state", 8);
		COLUMN.put("age", 9);
		registrations = new HashMap<>();
		SHEETS = new HashMap<>();
		SHEETS.put("REGION_1", "Region 1");
		SHEETS.put("REGION_2", "Region 2");
		SHEETS.put("REGION_3", "Region 3");
		SHEETS.put("REGION_4", "Region 4");
		SHEETS.put("REGION_CN", "Canada");
		SHEETS.put("REGION_CR", "South America & Caribbean");
		FONT_SIZE = new HashMap<>();
		FONT_SIZE.put('A', 56.245);
		FONT_SIZE.put('B', 47.555);
		FONT_SIZE.put('C', 51.110);
		FONT_SIZE.put('D', 48.819);
		FONT_SIZE.put('E', 43.764);
		FONT_SIZE.put('F', 39.972);
		FONT_SIZE.put('G', 53.954);
		FONT_SIZE.put('H', 47.555);
		FONT_SIZE.put('I', 11.770);
		FONT_SIZE.put('J', 39.261);
		FONT_SIZE.put('K', 49.767);
		FONT_SIZE.put('L', 39.103);
		FONT_SIZE.put('M', 56.877);
		FONT_SIZE.put('N', 47.555);
		FONT_SIZE.put('O', 55.534);
		FONT_SIZE.put('P', 45.897);
		FONT_SIZE.put('Q', 55.534);
		FONT_SIZE.put('R', 49.451);
		FONT_SIZE.put('S', 46.687);
		FONT_SIZE.put('T', 46.687);
		FONT_SIZE.put('U', 47.634);
		FONT_SIZE.put('V', 53.006);
		FONT_SIZE.put('W', 74.730);
		FONT_SIZE.put('X', 54.349);
		FONT_SIZE.put('Y', 53.717);
		FONT_SIZE.put('Z', 46.529);
		FONT_SIZE.put('-', 19.986);
		FONT_SIZE.put('.', 11.454);
		FONT_SIZE.put(' ', 15.483);
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
	public static MimeMessage createTicketEmail(List<Object> user, String id) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(user.get(COLUMN.get("email")).toString().replaceAll(" ", "")));
		email.setSubject("Congratulations, You're In! Welcome to IYC 2025");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent("Hello, " + ((String)user.get(COLUMN.get("firstName"))).trim() + " " + ((String)user.get(COLUMN.get("lastName"))).trim() + "!</b><br><br>Your registration for the <b>International Youth Convention (Dominion 2025)</b> is confirmed! We're excited to officially welcome you to this life-changing event that promises to be inspiring, empowering, and filled with unforgettable moments. Whether you're a starting professional, young adult, college student, or teenager, get ready for inspiring sessions, vibrant worship, and meaningful connections that will ignite your passion and fuel your faith.<br><br><b>Registration Details:</b><br><b>Confirmation Code: </b>" + id + "<br><b>Event Date: </b>July 17 - 20, 2025<br><b>Venue: </b>10000 Kleckley Drive Houston TX, 77075, USA<br><br>Be sure to mark your calendar and keep an eye on your inbox for more updates as we count down to the convention.<br><br>Here is your event ticket. Please save this and present it at check-in.<br><br><img src=\"cid:image1\" style=\"width: 90%; height: auto;\"><br><br><b>PLEASE DO NOT MAKE DUPLICATE REGISTRATIONS.</b> If you believe that you have made a mistake on your form, or have any questions about your registration, please contact us at registration@mfmyouthministries.org or call 425-236-7364.<br><br>Stay inspired and get ready for an amazing experience!<br><br>" + "Thank you, and have a wonderful day!<br><br><b>MFM Youth Ministry</b><br><b>The Americas & Caribbean</b>", "text/html");

		// Create the image part
		MimeBodyPart imagePart = new MimeBodyPart();
		imagePart.attachFile(new File("lightning_register_admin\\src\\main\\resources\\ticket-raster.png"));
		imagePart.setContentID("<image1>");
		imagePart.setDisposition(MimeBodyPart.INLINE);

		// Combine parts into a multipart/related message
		Multipart multipart = new MimeMultipart("related");
		multipart.addBodyPart(textPart);
		multipart.addBodyPart(imagePart);

		email.setContent(multipart);
		return email;
	}

	public static MimeMessage createToddlerEmail(List<Object> user) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(user.get(COLUMN.get("email")).toString().replaceAll(" ", "")));
		email.setSubject("Congratulations, You're In! Welcome to IYC 2025");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		boolean gender = user.get(COLUMN.get("gender")).equals("Male");
		textPart.setContent("Hello,<br><br>We want to let you know that we've received your registration for " + ((String)user.get(COLUMN.get("firstName"))).trim() + " " + ((String)user.get(COLUMN.get("lastName"))).trim() + ".<br><br>However, because " + (gender ? "he" : "she") + " is younger than 7 years of age, " + (gender ? "he" : "she") + " will not be issued a participant ticket. Provisions will still be made for " + (gender ? "him" : "her") + " during the convention.<br><br>Thank you for your understanding. Please contact us at registration@mfmyouthministries.org or call 425-236-7364 if you have any questions.<br><br>God bless you.", "text/html");

		// Combine parts into a multipart/related message
		Multipart multipart = new MimeMultipart("related");
		multipart.addBodyPart(textPart);

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
	 * @throws WriterException          if there is an error writing the ticket image
	 * @throws IOException              if there is an error reading or writing to the file system
	 * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
	 */
	public static void generateTicket(List<Object> registration, String id) throws WriterException, IOException, NoSuchAlgorithmException {
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
		PrintStream ps = new PrintStream("lightning_register_admin\\src\\main\\resources\\ticket-temp.svg");
		ps.print(new String(ticketBuilder));
		ps.close();

		int firstNameLength = registration.get(COLUMN.get("firstName")).toString().length();
		int lastNameLength = registration.get(COLUMN.get("lastName")).toString().length();
		if (firstNameLength > 7 || lastNameLength > 7) {
			File resave = new File("lightning_register_admin\\src\\main\\resources\\ticket-temp.svg");
			Scanner redoScn = new Scanner(resave);
			String[] redo = new String[50];
			for (int i = 0; redoScn.hasNext(); i++) {
				redo[i] = redoScn.nextLine();
			}
			redoScn.close();
			resave.delete();
			if (firstNameLength > 7) {
				double length = 0;
				for (char c : registration.get(COLUMN.get("firstName")).toString().toCharArray()) {
					length += FONT_SIZE.get(Character.toUpperCase(c));
				}
				length += 12.008 * ((double) (firstNameLength - 1));
				double scalar = Math.min(550.0 / length, 1.0);
				redo[35] = "\t<text xml:space=\"preserve\" style=\"font-style:normal;font-variant:normal;font-weight:900;font-stretch:normal;font-size:" + (double) (78.9958 * scalar) + "px;line-height:125%;font-family:Archivo;-inkscape-font-specification:'Archivo Heavy';writing-mode:lr-tb;fill:#c3b53c;stroke-width:7.40585;fill-opacity:1\" x=\"28.004101\" y=\"193.94911\" id=\"first-name\">";
			}
			if (lastNameLength > 7) {
				double length = 0;
				for (char c : registration.get(COLUMN.get("lastName")).toString().toCharArray()) {
					length += FONT_SIZE.get(Character.toUpperCase(c));
				}
				length += 12.008 * ((double) (lastNameLength - 1));
				double scalar = Math.min(550.0 / length, 1.0);
				redo[32] = "\t<text xml:space=\"preserve\" style=\"font-style:normal;font-variant:normal;font-weight:900;font-stretch:normal;font-size:" + (double) (78.9958 * scalar) + "px;line-height:125%;font-family:Archivo;-inkscape-font-specification:'Archivo Heavy';writing-mode:lr-tb;fill:#c3b53c;stroke-width:7.40585;fill-opacity:1\" x=\"28.004101\" y=\"272.10001\" id=\"last-name\">";
				redo[33] = "\t\t<tspan sodipodi:role=\"line\" id=\"tspan469\" x=\"28.004101\" y=\"" + (double) (272.10001 - (54.349 * (1.0 - scalar))) + "\" style=\"stroke-width:7.40585;fill:#c3b53c;fill-opacity:1\">" + registration.get(COLUMN.get("lastName")).toString().toUpperCase() + "</tspan>";
			}
			StringBuilder redoBuilder = new StringBuilder();
			for (String string : redo) {
				redoBuilder.append(string + "\n");
			}
			PrintStream redoPs = new PrintStream("lightning_register_admin\\src\\main\\resources\\ticket-temp.svg");
			redoPs.print(new String(redoBuilder));
			redoPs.close();
		}
	}

	/**
	 * Scans the active Google Sheet region for registrations with empty fields. This method retrieves the values from the active region of the Google Sheet and checks each row to identify registrations with empty fields at specific column indices. It collects the indices of such registrations and returns them as a list.
	 * 
	 * @return An ArrayList of integers representing the indices of registrations with empty fields in the active region of the Google Sheet.
	 * @throws IOException If there is an error retrieving values from the Google Sheet.
	 */
	public static ArrayList<String> scanUpdate() throws IOException {
		ArrayList<String> emptyReg = new ArrayList<>();
		if (ACTIVE_REGION != null) {
			List<List<Object>> values = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:J1000").execute().getValues();
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
		for (int i = 0; i < registrationUpdates.size(); i += 2) {
			String currentID = "";
			List<Object> current = registrations.get(ACTIVE_REGION).get(registrationUpdates.get(i));
			if (current.get(COLUMN.get("age")).equals("3-6")) {
				currentID = "TODDLER";
				sendMessage(GMAIL_SERVICE, "me", createToddlerEmail(current));
			} else {
				File svgPath = new File("lightning_register_admin\\src\\main\\resources\\ticket-temp.svg");
				currentID = generateID(current.get(COLUMN.get("firstName")).toString().trim() + current.get(COLUMN.get("lastName")).toString().trim() + current.get(COLUMN.get("email")).toString().trim() + current.get(COLUMN.get("phone")).toString().trim() + current.get(COLUMN.get("gender")).toString().trim() + current.get(COLUMN.get("age")).toString().trim());
				generateTicket(current, currentID);
				new PNGTranscoder().transcode(new TranscoderInput(new FileInputStream(svgPath)), new TranscoderOutput(new FileOutputStream("lightning_register_admin\\src\\main\\resources\\ticket-raster.png")));
				svgPath.delete();
				review(current);
				if (action.equals("reject")) {
					continue;
				} else if (action.equals("flag")) {
					List<List<Object>> flag = Arrays.asList(Arrays.asList("Rejected at processing"));
					ValueRange body = new ValueRange().setValues(flag);
					SHEETS_SERVICE.spreadsheets().values().update(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + registrationUpdates.get(i + 1).replace("B", "C"), body).setValueInputOption("RAW").execute();
					continue;
				}
				sendMessage(GMAIL_SERVICE, "me", createTicketEmail(current, currentID));
			}
			List<List<Object>> newID = Arrays.asList(Arrays.asList(currentID));
			ValueRange body = new ValueRange().setValues(newID);
			SHEETS_SERVICE.spreadsheets().values().update(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + registrationUpdates.get(i + 1), body).setValueInputOption("RAW").execute();
			action = "";
			new File("lightning_register_admin\\src\\main\\resources\\ticket-raster.png").delete();
			new File("lightning_register_admin\\src\\main\\resources\\barcode-temp.png").delete();
		}
	}

	public static void review(List<Object> registration) throws IOException {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("CheckPage.fxml"));
		Parent root = loader.load();
		CheckPage controller = loader.getController();
		controller.initializeData(registration);
		Stage modalStage = new Stage();
		modalStage.setTitle("Review Details");
		modalStage.initModality(Modality.APPLICATION_MODAL);
		modalStage.setScene(new Scene(root));
		System.out.println("does this even run?");
		modalStage.showAndWait();
	}

	public static ArrayList<String> scanFlag() throws IOException {
		List<List<Object>> values = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:J1000").execute().getValues();
		ArrayList<String> flagIndex = new ArrayList<>();
		for (int i = 1; i < values.size(); i++) {
			if (!values.get(i).get(COLUMN.get("flag")).toString().equals("")) {
				flagIndex.add("C" + (i + 1));
			}
		}
		return flagIndex;
	}

	/**
	 * Removes flags from the active Google Sheet region.
	 * <p>
	 * This method scans the active region of the Google Sheet for flagged registrations and removes the flags by setting the "Flag" column to an empty string. It logs the number of flags found and their positions before removing them.
	 * </p>
	 * 
	 * @throws IOException If there is an error updating values in the Google Sheet.
	 */
	public static void flagRemove() throws IOException {
		ArrayList<String> flags = scanFlag();
		System.out.println(flags.size());
		for (int i = 0; i < flags.size(); i++) {
			System.out.println(flags.get(i));
			List<List<Object>> newFlag = Arrays.asList(Arrays.asList(""));
			ValueRange body = new ValueRange().setValues(newFlag);
			SHEETS_SERVICE.spreadsheets().values().update(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + flags.get(i), body).setValueInputOption("RAW").execute();
		}
	}

	/**
	 * Scans the active Google Sheet region for duplicate registrations. This method retrieves the values from the active region of the Google Sheet and checks each row to identify registrations with the same first and last name. It collects the indices of such registrations and returns them as a list.
	 * 
	 * @return An ArrayList of strings representing the indices of duplicate registrations in the active region of the Google Sheet.
	 * @throws IOException If there is an error retrieving values from the Google Sheet.
	 */
	public static ArrayList<String> scanDuplicate() throws IOException {
		List<List<Object>> values = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:J1000").execute().getValues();
		HashMap<String, String> uniqueRegistrations = new HashMap<>();
		HashMap<String, String> duplicateRegistrations = new HashMap<>();
		ArrayList<String> duplicateIndex = new ArrayList<>();
		for (int i = 1; i < values.size(); i++) {
			List<Object> current = values.get(i);
			String firstName = current.get(COLUMN.get("firstName")).toString().trim();
			String lastName = current.get(COLUMN.get("lastName")).toString().trim();
			String key = (firstName + lastName).toLowerCase();
			if (duplicateRegistrations.containsKey(key)) {
				duplicateIndex.add("C" + (i + 1));
				duplicateRegistrations.put(key, "C" + (i + 1));
			} else if (uniqueRegistrations.containsKey(key)) {
				duplicateIndex.add(uniqueRegistrations.get(key));
				duplicateIndex.add("C" + (i + 1));
				duplicateRegistrations.put(key, "C" + (i + 1));
				uniqueRegistrations.remove(key);
			} else {
				uniqueRegistrations.put(key, "C" + (i + 1));
			}
		}
		return duplicateIndex;
	}

	/**
	 * Updates the "Flag" column of the active Google Sheet region based on the results of the duplicate scan. This method is called at the start of the program and is used to populate the "Flag" column of the active region with the flag "Duplicate" for any duplicate registrations found. The method is also called each time the user clicks the "Scan" button in the application window.
	 * 
	 * @throws IOException If there is an error retrieving values from the Google Sheet.
	 */
	public static void duplicateUpdate() throws IOException {
		ArrayList<String> flagUpdates = scanDuplicate();
		for (int i = 0; i < flagUpdates.size(); i++) {
			List<List<Object>> newFlag = Arrays.asList(Arrays.asList("Duplicate"));
			ValueRange body = new ValueRange().setValues(newFlag);
			SHEETS_SERVICE.spreadsheets().values().update(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + flagUpdates.get(i), body).setValueInputOption("RAW").execute();
		}
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
	 * <li>Region 1!A1:J1000
	 * <li>Region 2!A1:J1000
	 * <li>Region 3!A1:J1000
	 * <li>Region 4!A1:J1000
	 * <li>Canada!A1:J1000
	 * <li>South America & Caribbean!A1:J1000
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
				List<List<Object>> registrationsTemp = SHEETS_SERVICE.spreadsheets().values().get(SPREADHSEET_ID, SHEETS.get(regionIndex[i]) + "!A1:J1000").execute().getValues();
				for (int j = 1; j < registrationsTemp.size(); j++) {
					if (registrationsTemp.get(j).get(COLUMN.get("id")).toString().isEmpty() || registrationsTemp.get(j).get(COLUMN.get("id")).toString().equals("TODDLER")) {
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
}