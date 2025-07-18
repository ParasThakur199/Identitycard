package com.idcard.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.idcard.Payload.ApiPayload;


@RestControllerAdvice
public class GlobalExceptionHandler {
	 
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiPayload>resourceNotFoundException(ResourceNotFoundException rs ){
		String msg =""; 
		String[] parts = rs.getMessage().split(":");
		 if (parts.length == 2) {
	             msg = parts[0].trim();
	             ApiPayload apidata = new ApiPayload(msg, true);
	     		return new ResponseEntity(apidata, HttpStatus.BAD_REQUEST);
	        } else {
	        	ApiPayload apidata = new ApiPayload(rs.getMessage(), false);
	    		return new ResponseEntity(apidata, HttpStatus.BAD_REQUEST);
	        }  
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>>methodArgsNotValidException(MethodArgumentNotValidException rs )
	{
		Map<String, String> resp =new HashMap<>();
		rs.getBindingResult().getAllErrors().forEach((error)->{
		String fieldname =	((FieldError)error).getField();
		String messsage = error.getDefaultMessage();
		resp.put(fieldname, messsage);
		});
		return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
		
	}

	@ExceptionHandler(DataAlreadyExistException.class)
	public ResponseEntity<String>resourcealreadyExistException(DataAlreadyExistException rs ){
		String msg =rs.getMessage();
		return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
		
	}
	
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String>BadCredentialsException(BadCredentialsException rs ){
		String msg =rs.getMessage();
		ApiPayload apidata = new ApiPayload(msg, false);
		return new ResponseEntity(apidata, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String>UsernameNotFoundException(UsernameNotFoundException rs ){
		String msg =rs.getMessage();
		ApiPayload apidata = new ApiPayload(msg, false);
		return new ResponseEntity(apidata, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String>IllegalArgumentException(IllegalArgumentException rs ){
		String msg =rs.getMessage();
		return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(SandeshException.class)
	public ResponseEntity<String>SandeshException(SandeshException rs ){
		String msg =rs.getMessage();
		return new ResponseEntity(msg, HttpStatus.OK);
		
	}
	
	@ExceptionHandler(Sessionexipred.class)
	public ResponseEntity<String>Sessionexipred(Sessionexipred rs ){
		String msg =rs.getMessage();
		return new ResponseEntity(msg, HttpStatus.CONFLICT);
		
	}
	



}
