package com.jwtAuth.jwtAuthenticationFramework.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
public class SecurityFilter {

	@Autowired
	private AuthenticationFilter authenticationFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.csrf(csrfProtection -> csrfProtection.ignoringRequestMatchers("/loginPage/signUp","/loginPage/signIn")
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.authorizeHttpRequests(
						authentication -> authentication.requestMatchers("/loginPage/signUp", "/loginPage/signIn")
								.permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}

}
