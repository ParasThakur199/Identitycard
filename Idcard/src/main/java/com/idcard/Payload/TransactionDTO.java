package com.idcard.Payload;

import java.util.Date;

//import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionDTO {
	private Long id;

	private String stateCode;
    private String transactionNumber;
    private Date year;
    private String serialNumber;
}
