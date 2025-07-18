package com.idcard.exception;

public class DataAlreadyExistException  extends RuntimeException{
	
	
	 private String message;
	    public DataAlreadyExistException(String message) {
	    	super(String.format(message));
			this.message=message;
	    }


}
