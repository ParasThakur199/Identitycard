package com.idcard.Service;

import java.io.IOException;

import com.idcard.Payload.UserRequestDto;
import com.idcard.Payload.UserResponseDto;


public interface UserService {
	 public UserResponseDto addUser(UserRequestDto userRequestDto) throws IOException;
	 
}
