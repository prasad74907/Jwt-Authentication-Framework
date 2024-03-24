package com.jwtAuth.jwtAuthenticationFramework.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInDTO {

	String email;
	String passWord;
}
