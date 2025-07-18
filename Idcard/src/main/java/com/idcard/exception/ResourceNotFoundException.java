package com.idcard.exception;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
	String resource;
	String fieldname;
	long fieldvalue;
	String status;
	public ResourceNotFoundException(String resource,String fieldname,long fieldvalue) {
		super(String.format("%s%s:%s", resource,fieldname,fieldvalue));
		this.resource=resource;
		this.fieldname=fieldname;
		this.fieldvalue=fieldvalue;
		
	}
	
	public ResourceNotFoundException(String message) {
        super(message);
    }

	public ResourceNotFoundException(String message,String status) {
		super(String.format("%s:%s", message,status));
       
    }


}
