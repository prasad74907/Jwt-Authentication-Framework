package com.jwtAuth.jwtAuthenticationFramework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jwtAuth.jwtAuthenticationFramework.repository.UserRepository;

@Configuration
public class AuthConfiguration {

	/**
	 * Note:-When a client attempts to access a secured resource, such as a
	 * protected end point or page, the application intercepts the request and
	 * initiates an authentication process.The AuthenticationManager is responsible
	 * for processing this authentication request.AuthenticationManager instead
	 * of directly performing the authentication, it delegates
	 * AuthenticationProvider who is responsible for validating the credentials and
	 * authenticate the user.
	 */

	@Autowired
	private UserRepository userRepository;

	/**
	 * UserDetailsService-reponsible for loading the user details from userName. It
	 * is used by spring security's authentication mechanisms to fetch user details
	 * during the authentication process.
	 * 
	 * @return
	 */
	@Bean
	public UserDetailsService getUserDetailsService() {

		return (userEmail) -> userRepository.findByEmail(userEmail);
	}

	@Bean
	public AuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return daoAuthenticationProvider;

	}

	/**
	 * 
	 * @param authenticationConfiguration
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	
	@Bean
	public PasswordEncoder getPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}
}
