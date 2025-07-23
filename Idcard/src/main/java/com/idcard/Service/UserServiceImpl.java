package com.idcard.Service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.idcard.CommonData.PasswordGenerate;
import com.idcard.CommonData.ValidationFunction;
import com.idcard.Model.UserEntity;
import com.idcard.Payload.UserRequestDto;
import com.idcard.Payload.UserResponseDto;
import com.idcard.Repository.UserRepository;
import com.idcard.enums.Role;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
	@Transactional
	public UserResponseDto addUser(UserRequestDto userRequestDto) throws IOException {
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
        mail(userRequestDto.getEmail(),passwordGenerate);

//        try {
//			String pass = password1 != null ? passwordGenerate : "Test@00";
//			allOTPGenerationAndSend.sendUserIdAndPassword(user.getMobile(), user.getFirstName(), user.getPassword());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return modelMapper.map(user, UserResponseDto.class);
	}

	private void mail(String toemail,String pwd) {
		String sms_msg = "Your credential " + toemail + " with password " + pwd + " created on Portal " + serverurl
				+ ". -MedLEaPR";
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("hellosanjay10@gmail.com"); // must match spring.mail.username
		message.setTo(toemail);
		message.setSubject("Credentials");
		message.setText(sms_msg);
		mailSender.send(message);
	}

	

//	private void sendUserIdAndPasswordForStaging(String mobile, String userid, String password) {
//		String sms_msg = "Your credential " + userid + " with password " + password + " created on Portal " + serverurl
//				+ ". -MedLEaPR";
//		sms_Staging.sendMobileSMS(mobile, sms_msg, "1007170909439048677");
//	}
}
