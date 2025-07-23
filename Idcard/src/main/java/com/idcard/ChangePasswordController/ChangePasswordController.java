package com.idcard.ChangePasswordController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idcard.Payload.ChangePasswordDTO;
import com.idcard.Payload.Response_Status;
import com.idcard.Service.AuthenticationService;
import com.idcard.Service.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping({"/admin", "/user"})
public class ChangePasswordController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
			@RequestHeader("Authorization") String tokenHeader) {
		Response_Status st = new Response_Status();
		 
		try {
			String msg = authenticationService.changePassword(changePasswordDTO, tokenHeader);
			st.setMessage(msg);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			st.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(st);
		}
	} 
}
