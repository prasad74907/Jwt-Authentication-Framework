package com.jwtAuth.jwtAuthenticationFramework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignInDTO;
import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignInResponseDTO;
import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignUpDTO;
import com.jwtAuth.jwtAuthenticationFramework.model.User;
import com.jwtAuth.jwtAuthenticationFramework.repository.UserRepository;
import com.jwtAuth.jwtAuthenticationFramework.service.LoginPageService;

@RestController()
@RequestMapping("/loginPage")
public class UserSignInSignUpController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginPageService loginPageService;

	@PostMapping("/signUp")
	public ResponseEntity<String> employeeSignUp(@RequestBody UserSignUpDTO userSignUpDTO) {

		return ResponseEntity.ok(loginPageService.userSignUp(userSignUpDTO));
	}
    
	@GetMapping("/signIn")
	public ResponseEntity<UserSignInResponseDTO> userSignIn(@RequestBody UserSignInDTO userSignInDTO) {

		return ResponseEntity.ok(loginPageService.userSignIn(userSignInDTO));

	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}

}
