package com.idcard.Payload;

import com.idcard.enums.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserRequestDto {
//	private Long userid;
	private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Role role;
}
