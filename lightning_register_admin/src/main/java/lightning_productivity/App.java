package lightning_productivity;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
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

	public static String SPREADHSEET_ID = "17lFflosq1LDnoBsfdFmC8fUolmnPAWdcxWPB_URtb9s";
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String SHEETS_URL = "https://sheets.googleapis.com/v4/spreadsheets";
	public static HttpRequestInitializer CREDENTIAL;
	public static HashMap<String, Integer> COLUMN;
	public static HashMap<String, String> SHEETS;
	public static HashMap<String, String> MEALS;
	public static String ACTIVE_REGION;
	public static String[] TICKET;
	public static String[] BADGE;
	public static PrintService PRINTER;
	public static HashMap<Character, Double> FONT_SIZE_TICKET;
	public static String TEMP_DIR = System.getProperty("java.io.tmpdir") + "lightning_register_admin\\";

	public static HashMap<String, HashMap<String, List<Object>>> registrations;

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException, GeneralSecurityException {
		landingPage = new Scene(loadFXML("LandingPage"));
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

	public static void main(String[] args) throws Exception {
		// Initialise global variables
		// These must be initialised before launch() runs, otherwise problems
		File tempDir = new File(TEMP_DIR);
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
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
		FONT_SIZE_TICKET = new HashMap<>();
		FONT_SIZE_TICKET.put('A', 56.245);
		FONT_SIZE_TICKET.put('B', 47.555);
		FONT_SIZE_TICKET.put('C', 51.110);
		FONT_SIZE_TICKET.put('D', 48.819);
		FONT_SIZE_TICKET.put('E', 43.764);
		FONT_SIZE_TICKET.put('F', 39.972);
		FONT_SIZE_TICKET.put('G', 53.954);
		FONT_SIZE_TICKET.put('H', 47.555);
		FONT_SIZE_TICKET.put('I', 11.770);
		FONT_SIZE_TICKET.put('J', 39.261);
		FONT_SIZE_TICKET.put('K', 49.767);
		FONT_SIZE_TICKET.put('L', 39.103);
		FONT_SIZE_TICKET.put('M', 56.877);
		FONT_SIZE_TICKET.put('N', 47.555);
		FONT_SIZE_TICKET.put('O', 55.534);
		FONT_SIZE_TICKET.put('P', 45.897);
		FONT_SIZE_TICKET.put('Q', 55.534);
		FONT_SIZE_TICKET.put('R', 49.451);
		FONT_SIZE_TICKET.put('S', 46.687);
		FONT_SIZE_TICKET.put('T', 46.687);
		FONT_SIZE_TICKET.put('U', 47.634);
		FONT_SIZE_TICKET.put('V', 53.006);
		FONT_SIZE_TICKET.put('W', 74.730);
		FONT_SIZE_TICKET.put('X', 54.349);
		FONT_SIZE_TICKET.put('Y', 53.717);
		FONT_SIZE_TICKET.put('Z', 46.529);
		FONT_SIZE_TICKET.put('-', 19.986);
		FONT_SIZE_TICKET.put('.', 11.454);
		FONT_SIZE_TICKET.put(' ', 15.483);
		MEALS = new HashMap<>();
		MEALS.put("pizza-cheese", "01");
		MEALS.put("pizza-chicken", "02");
		MEALS.put("pizza-pepperoni", "03");
		MEALS.put("hot-dog", "04");
		MEALS.put("chicken-bake", "05");
		MEALS.put("popeyes", "06");
		MEALS.put("turkey-sub", "07");
		MEALS.put("tuna-sub", "08");
		MEALS.put("veggie-sub", "09");
		MEALS.put("chicken-sandwich", "10");
		MEALS.put("nigerian-chicken", "11");
		MEALS.put("caribbean", "12");
		MEALS.put("chinese-chow", "13");
		MEALS.put("chinese-rice", "14");
		MEALS.put("american-mac", "15");
		MEALS.put("american-corn", "16");
		MEALS.put("none", "--");
		TICKET = new String[50];
		Scanner ticketReader = new Scanner(new File("lightning_register_admin\\src\\main\\resources\\ticket-template.svg"));
		int lineNumber = 0;
		while (ticketReader.hasNext()) {
			TICKET[lineNumber] = ticketReader.nextLine();
			lineNumber++;
		}
		ticketReader.close();
		BADGE = new String[43];
		Scanner badgeReader = new Scanner(new File("lightning_register_admin\\src\\main\\resources\\badge-template.svg"));
		lineNumber = 0;
		while (badgeReader.hasNext()) {
			BADGE[lineNumber] = badgeReader.nextLine();
			lineNumber++;
		}
		badgeReader.close();
		CREDENTIAL = loadCredentials();

		// Printer setup
		for (PrintService printer : PrintServiceLookup.lookupPrintServices(null, null)) {
			if (printer.getName().equalsIgnoreCase("EXAMPLE")) { // TODO: Change this to your printer name
				PRINTER = printer;
				break;
			}
		}
		System.out.println(PRINTER == null ? "Printer not found. Badge printing will be unavailable." : "Printer found. Badge printing will be available.");
		loadSheetData();
		/* Scanner foodScn = new Scanner(new File("D:\\King\\Downloads\\food.csv"));
		Scanner notFoodScn = new Scanner(new File("D:\\King\\Downloads\\foodnot.csv"));
		HashSet<String> notFoodies = new HashSet<>();
		List<String> errors = new ArrayList<>();
		while (notFoodScn.hasNext()) {
			notFoodies.add(notFoodScn.nextLine().split(",")[0]);
		}
		while (foodScn.hasNext()) {
			List<Object> lineList = Arrays.asList((Object[]) foodScn.nextLine().split(","));
			if (!(lineList.get(9).equals("3-6") || lineList.get(9).equals("35-44") || lineList.get(9).equals("45-Above")) && !notFoodies.contains(lineList.get(1))) {
				try {
				sendMessage(createMealEmail(lineList, (String) lineList.get(1)), CREDENTIAL);
				Thread.sleep(5000);
				} catch (Exception e) {
					errors.add((String) lineList.get(1));
				}
			}
		}
		for (String s : errors) {
			System.out.println(s);
		} */

		launch();
	}

	public static List<List<Object>> readSheetData(String spreadsheetId, String range, HttpRequestInitializer credential) throws Exception {
		HttpRequest request = HTTP_TRANSPORT.createRequestFactory(credential).buildGetRequest(new GenericUrl(SHEETS_URL + "/" + spreadsheetId + "/values/" + range));
		request.setParser(GsonFactory.getDefaultInstance().createJsonObjectParser());

		// Parse the JSON response
		Map<String, Object> response = request.execute().parseAs(Map.class);
		List<List<Object>> values = ((List<List<Object>>) response.get("values"));
		for (int i = values.size() - 1; i >= 0; i--) {
			if (values.get(i).size() != 10) {
				values.remove(i);
			}
		}
		return (List<List<Object>>) response.get("values");
	}

	public static void writeSheetData(String spreadsheetId, String range, List<List<Object>> data, HttpRequestInitializer credential) throws Exception {
		String url = SHEETS_URL + "/" + spreadsheetId + "/values/" + range + "?valueInputOption=RAW";
		Map<String, Object> body = new HashMap<>();
		body.put("values", data);

		HttpContent content = new JsonHttpContent(GsonFactory.getDefaultInstance(), body);
		HttpRequest request = HTTP_TRANSPORT.createRequestFactory(credential).buildPutRequest(new GenericUrl(url), content);
		request.setParser(GsonFactory.getDefaultInstance().createJsonObjectParser());
		request.execute();
	}

	public static void appendSheetData(String spreadsheetId, String range, List<List<Object>> data, HttpRequestInitializer credential) throws Exception {
		String url = SHEETS_URL + "/" + spreadsheetId + "/values/" + range + ":append?valueInputOption=RAW";
		Map<String, Object> body = new HashMap<>();
		body.put("values", data);

		HttpContent content = new JsonHttpContent(GsonFactory.getDefaultInstance(), body);
		HttpRequest request = HTTP_TRANSPORT.createRequestFactory(credential).buildPostRequest(new GenericUrl(url), content);
		request.setParser(GsonFactory.getDefaultInstance().createJsonObjectParser());

		request.execute();
	}

	public static HttpRequestInitializer loadCredentials() throws Exception {
		JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
		// Load the client secrets JSON file
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream("lightning_register_admin\\src\\main\\resources\\credentials.json")));

		// Set up the authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, List.of("https://www.googleapis.com/auth/spreadsheets", "https://www.googleapis.com/auth/gmail.send")).setAccessType("offline") // "offline" ensures a refresh token is granted
				.setApprovalPrompt("force") // Forces re-prompt for user consent
				.build();

		// Use a local server receiver to handle the redirect from the user's browser
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

		// Perform the authorization
		Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

		return credential;
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
	public static String createTicketEmail(List<Object> user, String id) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(user.get(COLUMN.get("email")).toString().replaceAll(" ", "")));
		email.setSubject("Congratulations, You're In! Welcome to IYC 2025");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent("Hello, " + ((String) user.get(COLUMN.get("firstName"))).trim() + " " + ((String) user.get(COLUMN.get("lastName"))).trim() + "!</b><br><br>Your registration for the <b>International Youth Convention (Dominion 2025)</b> is confirmed! We're excited to officially welcome you to this life-changing event that promises to be inspiring, empowering, and filled with unforgettable moments. Whether you're a starting professional, young adult, college student, or teenager, get ready for inspiring sessions, vibrant worship, and meaningful connections that will ignite your passion and fuel your faith.<br><br><b>Registration Details:</b><br><b>Confirmation Code: </b>" + id + "<br><b>Event Date: </b>July 17 - 20, 2025<br><b>Venue: </b>10000 Kleckley Drive Houston TX, 77075, USA<br><br>Be sure to mark your calendar and keep an eye on your inbox for more updates as we count down to the convention.<br><br>Here is your event ticket. Please save this and present it at check-in.<br><br><img src=\"cid:image1\" style=\"width: 90%; height: auto;\"><br><br><b>PLEASE DO NOT MAKE DUPLICATE REGISTRATIONS.</b> If you believe that you have made a mistake on your form, or have any questions about your registration, please contact us at registration@mfmyouthministries.org or call 425-236-7364.<br><br>Stay inspired and get ready for an amazing experience!<br><br>" + "Thank you, and have a wonderful day!<br><br><b>MFM Youth Ministry</b><br><b>The Americas & Caribbean</b>", "text/html");

		// Create the image part
		MimeBodyPart imagePart = new MimeBodyPart();
		imagePart.attachFile(new File(TEMP_DIR + "ticket-raster.png"));
		imagePart.setContentID("<image1>");
		imagePart.setDisposition(MimeBodyPart.INLINE);

		// Combine parts into a multipart/related message
		Multipart multipart = new MimeMultipart("related");
		multipart.addBodyPart(textPart);
		multipart.addBodyPart(imagePart);

		email.setContent(multipart);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		return Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
	}

	public static String createToddlerEmail(List<Object> user) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(user.get(COLUMN.get("email")).toString().replaceAll(" ", "")));
		email.setSubject("Congratulations, You're In! Welcome to IYC 2025");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		boolean gender = user.get(COLUMN.get("gender")).equals("Male");
		textPart.setContent("Hello,<br><br>We want to let you know that we've received your registration for " + ((String) user.get(COLUMN.get("firstName"))).trim() + " " + ((String) user.get(COLUMN.get("lastName"))).trim() + ".<br><br>However, because " + (gender ? "he" : "she") + " is younger than 7 years of age, " + (gender ? "he" : "she") + " will not be issued a participant ticket. Provisions will still be made for " + (gender ? "him" : "her") + " during the convention.<br><br>Thank you for your understanding. Please contact us at registration@mfmyouthministries.org or call 425-236-7364 if you have any questions.<br><br>God bless you.", "text/html");

		// Combine parts into a multipart/related message
		Multipart multipart = new MimeMultipart("related");
		multipart.addBodyPart(textPart);

		email.setContent(multipart);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		return Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
	}

	public static String createMealEmail(List<Object> user, String id) throws MessagingException, IOException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom("mfmyouthministry@gmail.com");
		email.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(((String) user.get(COLUMN.get("email"))).replaceAll(" ", "")));
		email.setSubject("Your Access to Complimentary Meals - Time is Running Out!");

		// Create the email body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent("We still haven't heard from you yet. Please submit your choices by <b>Sunday, June 22, 2025</b> to secure your meals for the convention.<br>" +
				"<b>Hello " + ((String) user.get(COLUMN.get("firstName"))).trim() + ",</b><br>" + //
				"We're excited to welcome you to the <b>Dominion Power - International Youth Convention 2025!</b><br>" + //
				"As an eligible participant, you are entitled to complimentary lunch and dinner throughout the event (Thursday to Saturday). To help you choose your meals ahead of time, we've introduced the <b>Dominion Dining Portal</b> - a simple way to customize your dining experience.<br><br>" + //
				"<b>Your Confirmation Code:<br>" + //
				id + "</b><br>" + //
				"You'll need this code to access the portal and make your meal selections.<br><br>" + //
				"<b>What to Do Next:</b><br>" + //
				"1. Visit the <b>Dominion Dining Portal</b>: dominion2025.github.io/food. If you're trying to view our website on your phone and it doesn't look quite right, no worries - it works best in desktop mode.<br>" + //
				"For iPhone (Safari): Tap the 'AA' icon in the address bar, then select 'Request Desktop Website'.<br>" + //
				"For Android (Chrome): Tap the three dots in the upper right corner, then check the box for 'Desktop Site'.<br>" + //
				"2. Enter your confirmation code.<br>" + //
				"3. Choose your lunch and dinner options for each day.<br>" + //
				"4. Submit your selections by <b>Sunday, June 22, 2025.</b><br><br>" + //
				"In addition to the complimentary meals, a <b>variety of meals and drinks</b> will be available on-site for purchase. So, whether you want a snack between sessions or an extra treat, you'll have plenty of options to choose from!<br>" + //
				"If you need help at any stage, feel free to reach out to our support team at <b>coordinator@mfmyouthministries.org  (425-236-7364)</b><br>" + //
				"We're looking forward to seeing you in Houston!<br>" + //
				"God bless you,<br><br>" + //
				"<b>Convention Planning Team</b><br>" + //
				"MFM Youth Ministry (The Americas & Caribbean)<br>" + //
				"mfmym.org", "text/html");

		Multipart multipart = new MimeMultipart("related");
		multipart.addBodyPart(textPart);

		email.setContent(multipart);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		return Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
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
	public static void sendMessage(String email, HttpRequestInitializer credential) throws Exception {
		// Step 2: Construct the HTTP request
		String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

		// The request body is a JSON object with the "raw" field containing the encoded email
		Map<String, String> emailContent = new HashMap<>();
		emailContent.put("raw", email);

		HttpContent content = new JsonHttpContent(GsonFactory.getDefaultInstance(), emailContent);

		// Step 3: Create and send the HTTP request
		HttpRequest request = HTTP_TRANSPORT.createRequestFactory(credential).buildPostRequest(new GenericUrl(url), content);
		request.setParser(GsonFactory.getDefaultInstance().createJsonObjectParser());

		// Execute the request and parse the response
		Map<String, Object> response = request.execute().parseAs(Map.class);

		// Step 4: Print the response for debugging or confirmation
		System.out.println("Email sent successfully: " + response);
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
		File barcodeSave = new File(TEMP_DIR + "barcode-temp.png");
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
		PrintStream ps = new PrintStream(TEMP_DIR + "ticket-temp.svg");
		ps.print(new String(ticketBuilder));
		ps.close();

		int firstNameLength = registration.get(COLUMN.get("firstName")).toString().length();
		int lastNameLength = registration.get(COLUMN.get("lastName")).toString().length();
		if (firstNameLength > 7 || lastNameLength > 7) {
			File resave = new File(TEMP_DIR + "ticket-temp.svg");
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
					length += FONT_SIZE_TICKET.get(Character.toUpperCase(c));
				}
				length += 12.008 * ((double) (firstNameLength - 1));
				double scalar = Math.min(550.0 / length, 1.0);
				redo[35] = "\t<text xml:space=\"preserve\" style=\"font-style:normal;font-variant:normal;font-weight:900;font-stretch:normal;font-size:" + (double) (78.9958 * scalar) + "px;line-height:125%;font-family:Archivo;-inkscape-font-specification:'Archivo Heavy';writing-mode:lr-tb;fill:#c3b53c;stroke-width:7.40585;fill-opacity:1\" x=\"28.004101\" y=\"193.94911\" id=\"first-name\">";
			}
			if (lastNameLength > 7) {
				double length = 0;
				for (char c : registration.get(COLUMN.get("lastName")).toString().toCharArray()) {
					length += FONT_SIZE_TICKET.get(Character.toUpperCase(c));
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
			PrintStream redoPs = new PrintStream(TEMP_DIR + "ticket-temp.svg");
			redoPs.print(new String(redoBuilder));
			redoPs.close();
		}
	}

	/**
	 * Generates a badge image from the given registration information.
	 *
	 * @param registration the registration information
	 * @throws WriterException          if there is an error writing the badge image
	 * @throws IOException              if there is an error reading or writing to the file system
	 * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
	 * @throws TranscoderException      if there is an error transcoding the image
	 */
	public static void generateBadge(List<Object> registration) throws WriterException, IOException, NoSuchAlgorithmException, TranscoderException {
		String[] currentBadge = BADGE.clone();
		File barcodeSave = new File(TEMP_DIR + "barcode-temp.png");
		MatrixToImageWriter.writeToPath(new PDF417Writer().encode(registration.get(COLUMN.get("id")).toString(), BarcodeFormat.PDF_417, 1000, 1000), "PNG", barcodeSave.toPath());
		BufferedImage barcode = ImageIO.read(barcodeSave);
		barcode = barcode.getSubimage(30, 30, barcode.getWidth() - 60, barcode.getHeight() - 60);

		ImageIO.write(barcode, "png", barcodeSave);
		String base64Image = Base64.getEncoder().encodeToString(Files.readAllBytes(barcodeSave.toPath()));

		currentBadge[5] = "<image width=\"1050\" height=\"244\" id=\"img2\" xlink:href=\"data:image/png;base64," + base64Image + "\"/>";
		if (registration.get(COLUMN.get("age")).equals("7-11")) {
			currentBadge[18] = ".s8 { fill: #bad9f7 }";
		} else if (registration.get(COLUMN.get("age")).equals("12-18")) {
			currentBadge[18] = ".s8 { fill: #fad1bb }";
		} else if (registration.get(COLUMN.get("age")).equals("19-24")) {
			currentBadge[18] = ".s8 { fill: #c6f2b6 }";
		} else if (registration.get(COLUMN.get("age")).equals("25-34")) {
			currentBadge[18] = ".s8 { fill: #faf2bb }";
		} else if (registration.get(COLUMN.get("age")).equals("35-44")) {
			currentBadge[18] = ".s8 { fill: #d1bbfa }";
		} else if (registration.get(COLUMN.get("age")).equals("45-Above")) {
			currentBadge[18] = ".s8 { fill: #fafafa }";
		}
		currentBadge[22] = "<text id=\"code-readout\" x=\"205\" y=\"1782.7\"><tspan class=\"t1\">" + registration.get(COLUMN.get("id")).toString().toUpperCase() + "</tspan></text>";
		if (registration.get(COLUMN.get("age")).equals("35-44") || registration.get(COLUMN.get("age")).equals("45-Above")) {
			for (int i = 24; i < 33; i++) {
				currentBadge[i] = "";
			}
		} else {
			URL url = URI.create("https://food-e9814-default-rtdb.firebaseio.com/users.json").toURL();
			try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
				Gson gson = new Gson();
				Map<String, Object> data = gson.fromJson(reader, Map.class);
				String[] foodOptions = new String[] { "--", "--", "--", "--", "--", "--" };
				Object userData = data.get(registration.get(COLUMN.get("id")));
				if (userData instanceof Map<?, ?>) {
					Map<?, ?> userMap = (Map<?, ?>) userData;
					foodOptions[0] = MEALS.getOrDefault(userMap.get("thursdayLunch"), "--");
					foodOptions[1] = MEALS.getOrDefault(userMap.get("thursdayDinner"), "--");
					foodOptions[2] = MEALS.getOrDefault(userMap.get("fridayLunch"), "--");
					foodOptions[3] = MEALS.getOrDefault(userMap.get("fridayDinner"), "--");
					foodOptions[4] = MEALS.getOrDefault(userMap.get("saturdayLunch"), "--");
					foodOptions[5] = MEALS.getOrDefault(userMap.get("saturdayDinner"), "--");
				}
				currentBadge[25] = "<text id=\"food-s2\" x=\"101.823\" y=\"1751.857\"><tspan x=\"101.823\" y=\"1781.857\" class=\"t2\">" + foodOptions[5] + "</tspan></text>";
				currentBadge[26] = "<text id=\"food-s1\" x=\"101.823\" y=\"1701.819\"><tspan x=\"101.823\" y=\"1731.819\" class=\"t2\">" + foodOptions[4] + "</tspan></text>";
				currentBadge[27] = "<text id=\"food-f2\" x=\"100.433\" y=\"1615.642\"><tspan x=\"100.433\" y=\"1645.642\" class=\"t2\">" + foodOptions[3] + "</tspan></text>";
				currentBadge[28] = "<text id=\"food-f1\" x=\"100.433\" y=\"1565.604\"><tspan x=\"100.433\" y=\"1595.604\" class=\"t2\">" + foodOptions[2] + "</tspan></text>";
				currentBadge[29] = "<text id=\"food-t2\" x=\"101.823\" y=\"1479.428\"><tspan x=\"101.823\" y=\"1509.428\" class=\"t2\">" + foodOptions[1] + "</tspan></text>";
				currentBadge[30] = "<text id=\"food-t1\" x=\"101.823\" y=\"1429.39\"><tspan x=\"101.823\" y=\"1459.39\" class=\"t2\">" + foodOptions[0] + "</tspan></text>";
			}
		}
		currentBadge[36] = "<text id=\"first-name\" x=\"50%\" y=\"830\" text-anchor=\"middle\" dominant-baseline=\"middle\" style=\"transform: none;\"><tspan x=\"50%\" dy=\"0\" class=\"t7\">" + registration.get(COLUMN.get("firstName")) + "</tspan></text>";
		currentBadge[37] = "<text id=\"first-name\" x=\"50%\" y=\"980\" text-anchor=\"middle\" dominant-baseline=\"middle\" style=\"transform: none;\"><tspan x=\"50%\" dy=\"0\" class=\"t7\">" + registration.get(COLUMN.get("lastName")) + "</tspan></text>";
		StringBuilder badgeBuilder = new StringBuilder();
		for (String string : currentBadge) {
			badgeBuilder.append(string + "\n");
		}
		PrintStream ps = new PrintStream(TEMP_DIR + "badge-temp.svg");
		ps.print(new String(badgeBuilder));
		ps.close();
		new PNGTranscoder().transcode(new TranscoderInput(new FileInputStream(TEMP_DIR + "badge-temp.svg")), new TranscoderOutput(new FileOutputStream(TEMP_DIR + "badge-raster.png")));
	}

	/**
	 * Scans the active Google Sheet region for registrations with empty fields. This method retrieves the values from the active region of the Google Sheet and checks each row to identify registrations with empty fields at specific column indices. It collects the indices of such registrations and returns them as a list.
	 * 
	 * @return An ArrayList of integers representing the indices of registrations with empty fields in the active region of the Google Sheet.
	 * @throws Exception If there is an error retrieving values from the Google Sheet.
	 */
	public static ArrayList<String> scanUpdate() throws Exception {
		ArrayList<String> emptyReg = new ArrayList<>();
		if (ACTIVE_REGION != null) {
			List<List<Object>> values = readSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:J1000", CREDENTIAL);
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
	 * @throws Exception If there is an error processing the batch update.
	 */
	public static void batchUpdate() throws Exception {
		ArrayList<String> registrationUpdates = scanUpdate();
		for (int i = 0; i < registrationUpdates.size(); i += 2) {
			String currentID = "";
			List<Object> current = registrations.get(ACTIVE_REGION).get(registrationUpdates.get(i));
			if (current.get(COLUMN.get("age")).equals("3-6")) {
				currentID = "TODDLER";
				sendMessage(createToddlerEmail(current), CREDENTIAL);
			} else {
				File svgPath = new File(TEMP_DIR + "ticket-temp.svg");
				currentID = generateID(current.get(COLUMN.get("firstName")).toString().trim() + current.get(COLUMN.get("lastName")).toString().trim() + current.get(COLUMN.get("email")).toString().trim() + current.get(COLUMN.get("phone")).toString().trim() + current.get(COLUMN.get("gender")).toString().trim() + current.get(COLUMN.get("age")).toString().trim());
				generateTicket(current, currentID);
				new PNGTranscoder().transcode(new TranscoderInput(new FileInputStream(svgPath)), new TranscoderOutput(new FileOutputStream(TEMP_DIR + "ticket-raster.png")));
				svgPath.delete();
				review(current);
				if (action.equals("reject")) {
					continue;
				} else if (action.equals("flag")) {
					List<List<Object>> flag = Arrays.asList(Arrays.asList("Rejected at processing"));
					writeSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + registrationUpdates.get(i + 1).replace("B", "C"), flag, CREDENTIAL);
					continue;
				}
				sendMessage(createTicketEmail(current, currentID), CREDENTIAL);
				if (!current.get(COLUMN.get("age")).equals("35-44") && !current.get(COLUMN.get("age")).equals("45-Above")) {
					// Otherwise, send meal email
					try {
						String email = createMealEmail(current, currentID);
						System.out.println("Sending meal email to: " + current.get(COLUMN.get("email")));
						sendMessage(email, CREDENTIAL);
						Thread.sleep(5000); // Sleep to avoid rate limiting
					} catch (Exception e) {
						System.out.println("Failed to send meal email to: " + current.get(COLUMN.get("email")));
					}
				}
			}
			List<List<Object>> newID = Arrays.asList(Arrays.asList(currentID));
			writeSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + registrationUpdates.get(i + 1), newID, CREDENTIAL);
			action = "";
			new File(TEMP_DIR + "ticket-raster.png").delete();
			new File(TEMP_DIR + "barcode-temp.png").delete();
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
		modalStage.showAndWait();
	}

	public static ArrayList<String> scanFlag() throws Exception {
		List<List<Object>> values = readSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:J1000", CREDENTIAL);
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
	 * @throws Exception
	 */
	public static void flagRemove() throws Exception {
		ArrayList<String> flags = scanFlag();
		for (int i = 0; i < flags.size(); i++) {
			List<List<Object>> newFlag = Arrays.asList(Arrays.asList(""));
			writeSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + flags.get(i), newFlag, CREDENTIAL);
		}
	}

	/**
	 * Scans the active Google Sheet region for duplicate registrations. This method retrieves the values from the active region of the Google Sheet and checks each row to identify registrations with the same first and last name. It collects the indices of such registrations and returns them as a list.
	 * 
	 * @return An ArrayList of strings representing the indices of duplicate registrations in the active region of the Google Sheet.
	 * @throws Exception
	 */
	public static ArrayList<String> scanDuplicate() throws Exception {
		List<List<Object>> values = readSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!A1:J1000", CREDENTIAL);
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
	 * @throws Exception
	 */
	public static void duplicateUpdate() throws Exception {
		ArrayList<String> flagUpdates = scanDuplicate();
		for (int i = 0; i < flagUpdates.size(); i++) {
			List<List<Object>> newFlag = Arrays.asList(Arrays.asList("Duplicate"));
			writeSheetData(SPREADHSEET_ID, SHEETS.get(ACTIVE_REGION) + "!" + flagUpdates.get(i), newFlag, CREDENTIAL);
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
				List<List<Object>> registrationsTemp = readSheetData(SPREADHSEET_ID, SHEETS.get(regionIndex[i]) + "!A:J", CREDENTIAL);
				for (int j = 1; j < registrationsTemp.size(); j++) {
					if (registrationsTemp.get(j).get(COLUMN.get("id")).toString().isEmpty() || registrationsTemp.get(j).get(COLUMN.get("id")).toString().equals("TODDLER")) {
						registrations.get(regionIndex[i]).put(registrationsTemp.get(j).get(COLUMN.get("date")).toString(), registrationsTemp.get(j));
					} else {
						registrationsTemp.get(j).set(0, j + 1);
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