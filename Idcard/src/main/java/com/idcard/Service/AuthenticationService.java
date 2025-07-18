package com.idcard.Service;

import com.idcard.Payload.AuthenticationResponseDto;
import com.idcard.Payload.SignInRequestDto;

public interface AuthenticationService {
	 public AuthenticationResponseDto authenticate(SignInRequestDto signInRequestDto);
	 
	// public AuthenticationResponseDto RefreshTokenAuthentication(String refreshToken);
}
