package com.idcard.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.idcard.CommonData.Juleandate;
import com.idcard.Model.UserEntity;
import com.idcard.Payload.AuthenticationResponseDto;
import com.idcard.Payload.ChangePasswordDTO;
import com.idcard.Payload.SignInRequestDto;
import com.idcard.Payload.UserResponseDto;
import com.idcard.Repository.UserRepository;
import com.idcard.config.JwtService;
import com.idcard.exception.DataAlreadyExistException;
import com.idcard.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailsService;

	public AuthenticationResponseDto authenticate(SignInRequestDto signInRequestDto) {
		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(),
//					signInRequestDto.getPassword()));
			UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
					signInRequestDto.getUsername(), signInRequestDto.getPassword());
			authenticationManager.authenticate(authInputToken);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Wrong Credentials");
		}

		UserEntity user = userRepository.findByEmail(signInRequestDto.getUsername()).orElseThrow();
		UserResponseDto userResponseDto = UserResponseDto.builder().id(user.getId()).firstName(user.getFirstName())
				.lastName(user.getLastName()).email(user.getEmail()).userId(user.getUserId()).mobile(user.getMobile())
				.role(user.getRole().toString().toLowerCase()).build();

		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponseDto.builder().token(jwtToken).userResponseDto(userResponseDto).build();
	}

	@Override
	public String changePassword(@Valid ChangePasswordDTO dto, String tokenHeader) {
		final UserEntity claims2 = jwtService.getUserDetailsFromToken(tokenHeader);
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(claims2.getEmail());
		if (userDetails != null) {
			this.doAuthenticate(claims2.getEmail(), dto.getOldpassword());
			UserEntity user = userRepository.findByEmail(claims2.getEmail()).orElseThrow();
			String hashednewPassword = passwordEncoder.encode(dto.getNewpassword());

			user.setPassword(hashednewPassword);
			user.setChangeDate(Juleandate.getCurrentDateTime());
			user.setChangeUser(claims2.getUserId());
			userRepository.save(user);
			return "Password Changed Successfully";
		} else {
			throw new ResourceNotFoundException("Wrong userId data not found");
		}
	}

	private void doAuthenticate(String userid, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userid, password);
		try {
			authenticationManager.authenticate(authentication);

		} catch (BadCredentialsException e) {
			throw new ResourceNotFoundException(" Invalid old Password!!");
		}

	}
//	@Override
//	public AuthenticationResponseDto RefreshTokenAuthentication(String refreshToken) {
//		if (jwtUtil.validateToken(refreshToken)) {
//            String username = jwtUtil.extractUsername(refreshToken);
//            String newAccessToken = jwtUtil.generateAccessToken(username);
//            return new AuthResponse(newAccessToken, refreshToken);
//        }
//        throw new IllegalArgumentException("Invalid refresh token");
//		return null;
//	}

}
