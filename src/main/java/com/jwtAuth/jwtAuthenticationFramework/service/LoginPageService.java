package com.jwtAuth.jwtAuthenticationFramework.service;

import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignInDTO;
import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignInResponseDTO;
import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignUpDTO;

public interface LoginPageService {

	UserSignInResponseDTO userSignIn(UserSignInDTO userSignInDTO);

	String userSignUp(UserSignUpDTO userSignUpDTO);
}
