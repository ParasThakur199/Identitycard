package com.idcard.exception;

public class Sessionexipred extends RuntimeException{

	
	 private String message;
	    public Sessionexipred(String message) {
	    	super(String.format(message));
			this.message=message;
	    }
}
