package com.idcard.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idcard.Payload.UserRequestDto;
import com.idcard.Payload.UserResponseDto;
import com.idcard.Service.UserService;

@RestController
@RequestMapping("/alluser")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto userRequestDto) throws IOException {
		return new ResponseEntity<>(userService.addUser(userRequestDto), HttpStatus.CREATED);
	}
}