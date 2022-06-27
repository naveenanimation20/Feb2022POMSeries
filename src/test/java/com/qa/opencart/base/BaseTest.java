package com.qa.opencart.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.pages.CommonsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.RegisterPage;
import com.qa.opencart.pages.SearchResultsPage;

public class BaseTest {
	
	private static String SENDER = "naveenanimation20@gmail.com";
	private static String RECIPIENT = "naveenanimation20@gmail.com";
	private static String SUBJECT = "Open Cart Test Execution Report " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	private static String BODY_TEXT = "Please click on this latest Test Execution Report link:";


	DriverFactory df;
	protected Properties prop;
	public WebDriver driver;

	protected LoginPage loginPage;
	protected AccountsPage accPage;
	protected CommonsPage commPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductInfoPage productInfoPage;
	protected RegisterPage regPage;

	protected SoftAssert softAssert;

	@Parameters({ "browser", "browserversion" })
	@BeforeTest
	public void setup(String browser, String browserVersion) {
		df = new DriverFactory();
		prop = df.init_prop();

		if (browser != null) {
			prop.setProperty("browser", browser);
			prop.setProperty("browserversion", browserVersion);
		}

		driver = df.init_driver(prop);
		loginPage = new LoginPage(driver);
		softAssert = new SoftAssert();
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	@AfterSuite
	public void sendTestReports() {

		// Pass the name of the S3 bucket
		String bucket_name = "nalreportbucket";
		// Location of the report file from the project structure
		String file_path = "build/TestExecutionReport.html";
		String key_name = Paths.get(file_path).getFileName().toString();

		// Instantiate an Amazon S3 client, which will make the service call with the
		// supplied AWS credentials.
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
				.withRegion(Regions.AP_SOUTH_1).build();

		// Upload the report to S3 bucket
		try {
			s3.putObject(bucket_name, key_name, new File(file_path));
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}

		// Generate the S3 Pre-signed URL of the Test Execution Report
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket_name, key_name, HttpMethod.GET);
		// The URL expires after one day - time in milliseconds
		request.setExpiration(new Date(new Date().getTime() + 86400000));
		URL url = s3.generatePresignedUrl(request);

		// Replace the SENDER and RECIPIENT with an SES verified email(s).
		// Send the S3 Presigned URL using Simple Email Service (SES)
		Session session = Session.getDefaultInstance(new Properties());
		// Create a new MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		try {
			// Configure the email details
			message.setSubject(SUBJECT, "UTF-8");
			message.setFrom(new InternetAddress(SENDER));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT));
			message.setText("\n" + BODY_TEXT + "\n" + url.toString());
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		// Instantiate an Amazon SES client, which will make the service call with the
		// supplied AWS credentials.
		// Replace AP_SOUTH_1 with the AWS Region you're using for Amazon SES.
		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.AP_SOUTH_1).build();

			// Print the raw email content on the console
			PrintStream out = System.out;
			message.writeTo(out);

			// Send the email.
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			message.writeTo(outputStream);
			RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
			SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
			client.sendRawEmail(rawEmailRequest);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("Email Failed");
			System.err.println("Error message: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
