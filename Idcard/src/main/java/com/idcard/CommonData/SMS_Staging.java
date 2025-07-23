package com.idcard.CommonData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.idcard.Payload.EmailRequestDTO;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@Service
public class SMS_Staging {
	@Value("${live.urlSandes}")
	private String dbUrl;
	@Value("${spring.mail.pem.file.name}")
	private String pemFileName;
	@Value("${server.name}")
	private String serverName;
	@Value("${spring.mail.username}")
	private String sendfrom;
	private static final Logger logger = LoggerFactory.getLogger(SMS_Staging.class);
	private final RestTemplate restTemplate = new RestTemplate();
	// for ladkha vm----
	private static final String SMTP_HOST_NAME = "otprelay.nic.in";
	private static final String SMTP_AUTH_USER = "support-ldk-medleapr@ladakh.gov.in"; // User email
	private static final String SMTP_AUTH_PWD = "Medleapr@2024#"; // Password
	// Sandesh Message using medLEaPR API

//	@Async
//	public void sendSandeshMessageMedLEaPR(@Valid String mobile, String message) {
//		try {
//			String url = dbUrl + "/send?receiverid=" + mobile + "&msg=" + message;
//			restTemplate.getForEntity(url, SandesResponseWrapper.class);
//		} catch (Exception e) {
//		}
//
//	}

	public String sendMobileSMS(String mobilenumber, String msg, String templateID) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		// msg set to template
//		SmsRequestDTO request = new SmsRequestDTO();
//		request.setAppname("Medleapr");
//		request.setDlt_template_id_user_creation(templateID);
//		request.setHashKey("c76adc17fb7b25a93da25ad0b65692b3592027c06c34ce35246270d704d864f1");
//		request.setMessage(msg);
//		request.setMobilenumber(mobilenumber);
//		HttpEntity<SmsRequestDTO> requestEntity = new HttpEntity<>(request, headers);
//		// send msg to mobile number ------
//		ResponseEntity<String> responseEntity = restTemplate
//				.postForEntity("http://android.dpmuhry.gov.in/api/SMSMsgSender/SendSMS", requestEntity, String.class);
//		if (responseEntity.getStatusCode().is2xxSuccessful()) {
//			return "Successfully Send!";
//		} else {
//			throw new ResourceNotFoundException("Unable to send MSG " + mobilenumber);
//		}
		return null;
	}

	@Async
	public void ladakhotp(String mobilenumber, String msg, String templateID) {
		disableSSLValidation();
		String responseMessage = null;
		HttpURLConnection http = null;
		try {
			// Define the API URL
			URL url = new URL("https://164.100.14.211:443/failsafe/MLink");
			http = (HttpURLConnection) url.openConnection();
			disableSSLValidation();
			// Configure HTTP connection properties
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("Accept", "application/json");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// Prepare POST data
			String username = "mlrldk.sms";
			String password = "xHyZagQX";
			String signature = "MLRLDK";
			String entityid = "1401435070000071979";
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
	public CompletableFuture<String> sendmail(@Valid @Email String useremailid, String cc, String bcc, String message,
			String subject) {
		String responseBody = null;
		if (!serverName.equals("ladakh")) {
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				EmailRequestDTO request = new EmailRequestDTO();
				request.setEmail_to(useremailid);
				request.setEmail_body(message);
				request.setEmail_Subject(subject);
				request.setEmail_cc(cc);
				request.setAppname("Medleapr");
				request.setEmail_bcc(bcc);
				request.setHashKey("e409fe2a8c76f9310bd809ad27e163b75083d06bcd4647fbe4849e72ac4f0a0f");
				HttpEntity<EmailRequestDTO> requestEntity = new HttpEntity<>(request, headers);
				ResponseEntity<String> responseEntity = restTemplate.postForEntity(
						"http://android.dpmuhry.gov.in/api/EmailMsgSender/Sendemail", requestEntity, String.class);
				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					responseBody = "Success";
				} else {
					responseBody = "failed";
				}
			} catch (Exception ex) {
				logger.info("email failed due to ---" + ex.getMessage());
				CompletableFuture.completedFuture("Failed: " + ex.getMessage());
			}
		} else if (serverName.equals("ladakh")) {
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
				message1.setFrom(new InternetAddress(SMTP_AUTH_USER)); // From address
				message1.addRecipient(Message.RecipientType.TO, new InternetAddress(useremailid));
				if (cc != null && !cc.trim().isEmpty()) {
					message1.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				}
				transport.connect();
				transport.sendMessage(message1, message1.getAllRecipients());
				transport.close();

				responseBody = "Success";

			} catch (Exception ex) {
				logger.info("email failed due to ---" + ex.getMessage());
				CompletableFuture.completedFuture("Failed: " + ex.getMessage());
			}

		}
		return CompletableFuture.completedFuture(responseBody);
	}

	public String sendmailforTFAAuth(@Valid @Email String useremailid, String cc, String bcc, String message,
			String subject) {
		String responseBody = null;
		if (!serverName.equals("ladakh")) {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			EmailRequestDTO request = new EmailRequestDTO();
			request.setEmail_to(useremailid);
			request.setEmail_body(message);
			request.setEmail_Subject(subject);
			request.setEmail_cc(cc);
			request.setAppname("Medleapr");
			request.setEmail_bcc(bcc);
			request.setHashKey("e409fe2a8c76f9310bd809ad27e163b75083d06bcd4647fbe4849e72ac4f0a0f");

			HttpEntity<EmailRequestDTO> requestEntity = new HttpEntity<>(request, headers);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(
					"http://android.dpmuhry.gov.in/api/EmailMsgSender/Sendemail", requestEntity, String.class);
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				responseBody = "Success";
			} else {
				responseBody = "failed";
			}

		} else if (serverName.equals("ladakh")) {
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
				message1.setFrom(new InternetAddress(SMTP_AUTH_USER)); // From address
				message1.addRecipient(Message.RecipientType.TO, new InternetAddress(useremailid));
				if (cc != null && !cc.trim().isEmpty()) {
					message1.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				}
				transport.connect();
				transport.sendMessage(message1, message1.getAllRecipients());
				transport.close();

				responseBody = "Success";

			} catch (Exception ex) {
				logger.info("email failed due to ---" + ex.getMessage());
				return "Failed: " + ex.getMessage();
			}

		}
		return responseBody;
	}

	private class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
		}
	}

	@Async
	public void ukotp(String mobilenumber, String msg, String templateID) {
		disableSSLValidation();
		String responseMessage = null;
		HttpURLConnection http = null;

		try {
			// Define the API URL
			URL url = new URL("https://164.100.14.211:443/failsafe/MLink");
			http = (HttpURLConnection) url.openConnection();
			disableSSLValidation();
			// Configure HTTP connection properties
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("Accept", "application/json");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// Prepare POST data
			String username = "ukitda.sms";
			String password = "4h6bO7i9";
			String signature = "UKITDA";
			String entityid = "1301159349918843780proj";

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

}
