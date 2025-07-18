package com.idcard.Service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.idcard.CommonData.PasswordGenerate;
import com.idcard.CommonData.ValidationFunction;
import com.idcard.Model.UserEntity;
import com.idcard.Payload.UserRequestDto;
import com.idcard.Payload.UserResponseDto;
import com.idcard.Repository.UserRepository;
import com.idcard.enums.Role;




@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidationFunction validationFunction;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	ModelMapper modelMapper = new ModelMapper();
	

	@Override
	public UserResponseDto addUser(UserRequestDto userRequestDto) throws IOException {
		String passwordGenerate = "";
		UserEntity user = new UserEntity();
		
		passwordGenerate=PasswordGenerate.createPassword();
		String password1 = validationFunction.convertSHA256(passwordGenerate);
		String encryptedPassword = passwordEncoder.encode(password1 != null ? password1
				: "6a62655eed819ce425894c0c3a18bd7dd469d5b6ce7835fff8b0cb064a129f06");
		
		user.setFirstName(userRequestDto.getFirstName());
		user.setLastName(userRequestDto.getLastName());
		user.setRole(Role.User);
		user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(encryptedPassword));
        user.setMobile(userRequestDto.getMobile());
        userRepository.save(user);
        
//        try {
//			String pass = password1 != null ? passwordGenerate : "Test@00";
//			allOTPGenerationAndSend.sendUserIdAndPassword(user.getMobile(), user.getFirstName(), user.getPassword());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return modelMapper.map(user, UserResponseDto.class);
	}
}
