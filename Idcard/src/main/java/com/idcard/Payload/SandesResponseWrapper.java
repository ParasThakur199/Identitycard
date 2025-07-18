package com.idcard.Payload;

import com.spring.medleaper.OtpGenrationDTO.DataStatus;

import lombok.Data;

@Data
public class SandesResponseWrapper {
	private String status;
    private DataStatus data;
}
