package com.idcard.Payload;

import com.idcard.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
	 private Long id;
	 private String userId;
	 private String firstName;
	 private String lastName;
	 private String email;
	 private String mobile;
	 private String role;	
}
