package com.idcard.Payload;

import lombok.Data;

@Data
public class Login_Token_DTO {
	private String userid;
	private String healthinstitutecode;
	private String scode;
	private String dcode;
	private String usertype;
	private String labcode; 
	private String ipaddress;
	private String IsmedleaperLite;
	private String userip;
	private String servertype;
	private String key;
}
