package com.idcard.Payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDTO {
	
	@NotNull(message = "Enter New Password")
	private String newpassword;
	@NotNull(message = "Enter Confirm Password")
	private String confirmpassword;
	@NotNull(message = "Enter Old Password")
	private String oldpassword;
}
