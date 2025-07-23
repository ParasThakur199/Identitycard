package com.idcard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idcard.Payload.AuthenticationResponseDto;
import com.idcard.Payload.SignInRequestDto;
import com.idcard.Service.AuthenticationService;



@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
    private AuthenticationService authenticationService;
	
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(signInRequestDto));
    }
    
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
//        return ResponseEntity.ok(authenticationService.RefreshTokenAuthentication(refreshToken));
//    }
    
}
