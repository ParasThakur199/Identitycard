package com.idcard.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.idcard.CommonData.PasswordGenerate;
import com.idcard.CommonData.ValidationFunction;
import com.idcard.Model.UserEntity;
import com.idcard.Payload.EmailRequestDTO;
import com.idcard.Payload.UserRequestDto;
import com.idcard.Payload.UserResponseDto;
import com.idcard.Repository.UserRepository;
import com.idcard.enums.Role;


import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	//@Autowired
	//private SMS_ProductionDelhi sms;

	@Autowired
	private ValidationFunction validationFunction;
	@Value("${server.portal.url}")
	private String serverurl;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;
	ModelMapper modelMapper = new ModelMapper();
	private final RestTemplate restTemplate = new RestTemplate();
	//@Autowired
	//private SMS_Staging sms_Staging;

	@Override
	public UserResponseDto addUser(UserRequestDto userRequestDto)  {
		String passwordGenerate = "";
		UserEntity user = new UserEntity();

		passwordGenerate = PasswordGenerate.createPassword();
		String password1 = validationFunction.convertSHA256(passwordGenerate);
		String encryptedPassword = passwordEncoder.encode(
				password1 != null ? password1 : "6a62655eed819ce425894c0c3a18bd7dd469d5b6ce7835fff8b0cb064a129f06");
		user.setFirstName(userRequestDto.getFirstName());
		user.setLastName(userRequestDto.getLastName());
		user.setRole(userRequestDto.getRole());
		
		
		// sendUserIdAndPasswordForStaging(userRequestDto.getMobile(),userRequestDto.getEmail(),passwordGenerate);
		//sms.sendmail(userRequestDto.getEmail(), "", "", "test", "test");

		String email = userRequestDto.getEmail();
		int atIndex = email.indexOf("@");
		String userId = email.substring(0, atIndex);
		user.setUserId(userId);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(passwordGenerate));
//        user.setPassword(passwordEncoder.encode(encryptedPassword));
        user.setMobile(userRequestDto.getMobile());
        userRepository.save(user);
        try {
			mail(userRequestDto.getEmail(),passwordGenerate);
		} catch (Exception e) {
			System.err.println("Mail sending failed: " + e.getMessage());
		}

		return modelMapper.map(user, UserResponseDto.class);
	}

//	private void mail(String toemail,String pwd) throws ConnectException {
//		String sms_msg = "Your credential " + toemail + " with password " + pwd + " created on Portal " + serverurl
//				+ ". -NIC,Haryana";
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("hellosanjay10@gmail.com"); //username
//		message.setTo(toemail);
//		message.setSubject("Credentials");
//		message.setText(sms_msg);
//		mailSender.send(message);
//	}

	
	@Async
	public CompletableFuture<String> mail(@Valid @Email String toemail, String pwd) {
		String responseBody = null;
			try {
				String sms_msg = "Your credential " + toemail + " with password " + pwd + " created on Portal " + serverurl
						+ ". -NIC,Haryana";
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				EmailRequestDTO request = new EmailRequestDTO();
				request.setEmail_to(toemail);
				request.setEmail_body(sms_msg);
				request.setEmail_Subject("ID card NIC credentials");
				request.setEmail_cc("");
				request.setAppname("Medleapr");
				request.setEmail_bcc("");
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
				CompletableFuture.completedFuture("Failed: " + ex.getMessage());
			}
		return CompletableFuture.completedFuture(responseBody);
	}

	
}
