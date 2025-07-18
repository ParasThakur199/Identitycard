package com.idcard.CommonData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.idcard.Payload.SandesResponseWrapper;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@Service
public class SMS_ProductionDelhi {
	@Value("${live.urlSandes}")
	private String dbUrl;
	@Value("${server.name}")
	private String serverName;
	@Value("${spring.mail.username}")
	private String sendfrom;

	private static final Logger logger = LoggerFactory.getLogger(SMS_ProductionDelhi.class);
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String SMTP_HOST_NAME = "164.100.2.239";
	private static final String SMTP_AUTH_USER = "support-medleapr@nic.in"; // User email
	private static final String SMTP_AUTH_PWD = "qwUOPLDtyBu3_j"; // Password

	// Sandesh Message using medLEaPR API
	@Async
	public void sendSandeshMessageMedLEaPR(@Valid String mobile, String message) {
		try {
			String url = dbUrl + "/send?receiverid=" + mobile + "&msg=" + message;
			restTemplate.getForEntity(url, SandesResponseWrapper.class);
		} catch (Exception e) {
			logger.info("Sandesh Message MedLEaPR failed due to ---" + e.getMessage());
			CompletableFuture.completedFuture("Failed: " + e.getMessage());
		}
	}

	@Async
	public void delhiotp(String mobilenumber, String msg, String templateID) {
		disableSSLValidation();
		String responseMessage = null;
		HttpURLConnection http = null;

		try {
			// Define the API URL
			URL url = new URL("https://164.100.14.211:443/failsafe/HttpLink");
			http = (HttpURLConnection) url.openConnection();
			disableSSLValidation();
			// Configure HTTP connection properties
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("Accept", "application/json");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// Prepare POST data
			String username = "digipolice.sms";
			String password = "DigNiC12345%40%23";
			String signature = "NCRBIC";
			String entityid = "1301157526071722542";
			String data = String.format(
					"username=%s&pin=%s&message=%s&mnumber=%s&signature=%s&dlt_entity_id=%s&dlt_template_id=%s",
					username, password, msg, "91" + mobilenumber, signature, entityid, templateID);

			// Send POST request
			byte[] out = data.getBytes("UTF-8");
			try (OutputStream stream = http.getOutputStream()) {
				stream.write(out);
			}

			// Get the response
			int responseCode = http.getResponseCode();
			responseMessage = http.getResponseMessage();

			// Log response
			logger.info("Response Code: " + responseCode);
			logger.info("Response Message: " + responseMessage);

			// Handle the response body if needed
			try (BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				logger.info("Response Body: " + response.toString());
			}

		} catch (Exception e) {
			logger.error("Error sending SMS to " + mobilenumber + ": " + e.getMessage(), e);
		} finally {
			if (http != null) {
				http.disconnect();
			}
		}
	}

	public void disableSSLValidation() {
		try {
			// Create a TrustManager that trusts all certificates
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting TrustManager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create a HostnameVerifier that accepts all hostnames
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Sandes Message using MedleaPR Spring boot
	public String sendSandeshMessageSpringBootMedLEaPR(@Valid String mobile, String message) {
		String response = null;
		String url = "http://localhost:8021/send?receiverid=" + mobile + "&msg=" + message;
		response = restTemplate.getForObject(url, String.class);
		return response;
	}

	@Async
	public void sendmail(@Valid @Email String useremailid, String cc, String bcc, String message, String subject) {

		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.socketFactory.port", "465");
			// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			// Create authenticator
			Authenticator auth = new SMTPAuthenticator();
			Session mailSession = Session.getDefaultInstance(props, auth);
			Transport transport = mailSession.getTransport();

			// Create and configure the message
			MimeMessage message1 = new MimeMessage(mailSession);
			message1.setContent(message, "text/html; charset=utf-8");
			message1.setSubject(subject);
			message1.setFrom(new InternetAddress("support-medleapr@nic.in")); // From address
			message1.addRecipient(Message.RecipientType.TO, new InternetAddress(useremailid));
			if (cc != null && !cc.trim().isEmpty()) {
				message1.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			transport.connect();
			transport.sendMessage(message1, message1.getAllRecipients());
			transport.close();

		} catch (Exception ex) {
			logger.info("email failed due to ---" + ex.getMessage());
			CompletableFuture.completedFuture("Failed: " + ex.getMessage());
		}

	}

	private class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
		}
	}

	public void sendmailwithattachemnt(@Valid @Email String useremailid, String cc, String bcc, String message,
			String subject, String base64Pdf, String fileName) {

		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.socketFactory.port", "465");

			Authenticator auth = new SMTPAuthenticator();
			Session mailSession = Session.getInstance(props, auth);
			Transport transport = mailSession.getTransport();

			MimeMessage message1 = new MimeMessage(mailSession);
			message1.setSubject(subject);
			message1.setFrom(new InternetAddress("support-medleapr@nic.in"));
			message1.addRecipient(Message.RecipientType.TO, new InternetAddress(useremailid));

			if (cc != null && !cc.trim().isEmpty()) {
				message1.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			if (bcc != null && !bcc.trim().isEmpty()) {
				message1.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
			}

			// Message body
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(message, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Base64 PDF attachment
			if (base64Pdf != null && !base64Pdf.isEmpty()) {
				MimeBodyPart attachmentPart = new MimeBodyPart();
				byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
				DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
				attachmentPart.setDataHandler(new DataHandler(dataSource));
				attachmentPart.setFileName(fileName != null ? fileName : "attachment.pdf");
				multipart.addBodyPart(attachmentPart);
			}
			message1.setContent(multipart);

			transport.connect();
			transport.sendMessage(message1, message1.getAllRecipients());
			transport.close();

		} catch (Exception ex) {
			logger.info("email failed due to ---" + ex.getMessage());
			CompletableFuture.completedFuture("Failed: " + ex.getMessage());
		}
	}

	public static boolean sendEmailSendWithCCBCCAttachment(String sendTo, String cc, String bcc,
			InputStream attachmentStream, String subject, String body) {
		try {
			// Set up the mail server properties

			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.socketFactory.port", "465");
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
				}
			};
			Session session = Session.getInstance(props, auth);

			// Create the message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("no-reply-medleapr@nic.in"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));

			if (cc != null && !cc.trim().isEmpty()) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}
			if (bcc != null && !bcc.trim().isEmpty()) {
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
			}
			msg.setSubject(subject);
			msg.setContent(body, "text/html");
			if (attachmentStream.available() > 0) {
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(body, "text/html");
				MimeBodyPart attachmentPart = new MimeBodyPart();
				DataSource source = new ByteArrayDataSource(attachmentStream, "application/pdf");
				attachmentPart.setDataHandler(new DataHandler(source));
				attachmentPart.setFileName("Attachment.pdf");
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(attachmentPart);
				msg.setContent(multipart);
			}
			// Send the message
			Transport.send(msg);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
