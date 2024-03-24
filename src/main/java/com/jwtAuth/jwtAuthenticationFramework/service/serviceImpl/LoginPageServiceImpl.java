package com.jwtAuth.jwtAuthenticationFramework.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignInDTO;
import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignInResponseDTO;
import com.jwtAuth.jwtAuthenticationFramework.DTO.UserSignUpDTO;
import com.jwtAuth.jwtAuthenticationFramework.model.Token;
import com.jwtAuth.jwtAuthenticationFramework.model.TokenStatus;
import com.jwtAuth.jwtAuthenticationFramework.model.User;
import com.jwtAuth.jwtAuthenticationFramework.repository.TokenRepository;
import com.jwtAuth.jwtAuthenticationFramework.repository.UserRepository;
import com.jwtAuth.jwtAuthenticationFramework.security.AuthService;
import com.jwtAuth.jwtAuthenticationFramework.service.LoginPageService;

@Service
public class LoginPageServiceImpl implements LoginPageService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthService authService;

	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public UserSignInResponseDTO userSignIn(UserSignInDTO userSignInDTO) {

		String accessToken = "";
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userSignInDTO.getEmail(), userSignInDTO.getPassWord()));
		if (authentication.isAuthenticated()) {
			User user = userRepository.findByEmail(userSignInDTO.getEmail());

			// Returns the token that is enabled.
			Token existingToken = tokenRepository.findByUser(user);
			if (existingToken != null) {
				/**
				 * Setting disabling the enabled token and updating in the database.
				 */
				existingToken.setTokenStatus(TokenStatus.valueOf("DISABLE"));
				tokenRepository.saveAndFlush(existingToken);
			}
			accessToken = authService.generateToken(user);
			Token token = Token.builder().token(accessToken).user(user).tokenStatus(TokenStatus.valueOf("ENABLE"))
					.build();
			tokenRepository.save(token);
		}
		return UserSignInResponseDTO.builder().jwtToken(accessToken).build();

	}

	@Override
	public String userSignUp(UserSignUpDTO userSignUpDTO) {

		User user = User.builder().firstName(userSignUpDTO.getFirstName()).lastName(userSignUpDTO.getLastName())
				.email(userSignUpDTO.getEmail()).passWord(passwordEncoder.encode(userSignUpDTO.getPassWord()))
				.confirmPassWord(passwordEncoder.encode(userSignUpDTO.getConfirmPassword())).build();

		return userRepository.save(user) != null ? "User SignUp Successful" : "User SignUp UnSuccessful";
	}

}
