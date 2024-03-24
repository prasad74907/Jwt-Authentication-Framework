package com.jwtAuth.jwtAuthenticationFramework.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignInResponseDTO {

	String jwtToken;
}
