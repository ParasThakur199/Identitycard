package com.idcard.Service;

import com.idcard.Payload.AuthenticationResponseDto;
import com.idcard.Payload.ChangePasswordDTO;
import com.idcard.Payload.SignInRequestDto;


import jakarta.validation.Valid;

public interface AuthenticationService {
	 public AuthenticationResponseDto authenticate(SignInRequestDto signInRequestDto);


	public String changePassword(@Valid ChangePasswordDTO changePasswordDTO, String tokenHeader);
	 
	// public AuthenticationResponseDto RefreshTokenAuthentication(String refreshToken);
}
