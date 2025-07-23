package com.idcard.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {

	private String Email_to;
	private String Email_cc;
	private String Email_bcc;
	private String Email_Subject;
	private String Email_body;
	private String Appname;
	private String HashKey;
	private String Emailattachement; // Add this line

}
