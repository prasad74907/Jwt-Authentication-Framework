package com.jwtAuth.jwtAuthenticationFramework.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDTO {

	String firstName;
	String lastName;
	String email;
	String passWord;
}
