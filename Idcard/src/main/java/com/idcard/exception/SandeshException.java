package com.idcard.exception;

public class SandeshException extends RuntimeException{

	
	 private String message;
	    public SandeshException(String message) {
	    	super(String.format(message));
			this.message=message;
	    }
}
