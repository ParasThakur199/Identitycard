package com.idcard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idcard.Model.UserEntity;
import com.idcard.Payload.AuthenticationResponseDto;
import com.idcard.Payload.SignInRequestDto;
import com.idcard.Payload.UserResponseDto;
import com.idcard.Repository.UserRepository;
import com.idcard.config.JwtService;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtService jwtService;

	public AuthenticationResponseDto authenticate(SignInRequestDto signInRequestDto) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(),
					signInRequestDto.getPassword()));
		} catch (Exception e) {
			throw new UsernameNotFoundException("UserName and Password not matched");
		}

		UserEntity user = userRepository.findByEmail(signInRequestDto.getUsername()).orElseThrow();
		UserResponseDto userResponseDto = UserResponseDto.builder().id(user.getId()).firstName(user.getFirstName())
				.lastName(user.getLastName()).email(user.getEmail()).userId(user.getUserId()).mobile(user.getMobile()).build();

		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponseDto.builder().token(jwtToken).userResponseDto(userResponseDto).build();
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
