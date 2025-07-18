package com.idcard.Payload;

import lombok.Data;

@Data
public class Response_Status {
	private String message;
//	private String error;
//	private String status;
//	private String timestamp;

	public Response_Status(String message) {
		super();
		this.message = message;
	}

	public Response_Status() {
		super();
	}
}
