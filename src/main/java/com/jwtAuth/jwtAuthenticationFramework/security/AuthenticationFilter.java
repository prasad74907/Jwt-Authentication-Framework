package com.jwtAuth.jwtAuthenticationFramework.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwtAuth.jwtAuthenticationFramework.model.Token;
import com.jwtAuth.jwtAuthenticationFramework.model.User;
import com.jwtAuth.jwtAuthenticationFramework.repository.TokenRepository;
import com.jwtAuth.jwtAuthenticationFramework.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException,IOException {
		logger.info(request.getServletPath());
		String authHeader = request.getHeader("Authorization");
		String path = request.getServletPath();
		/**
		 * For SignIn and SignUp no token will be sent in Header
		 */
		if (path.equals("/loginPage/signUp") || path.equals("/loginPage/signIn")) {
			filterChain.doFilter(request, response);
			return;
		} else if (authHeader == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Authorization Header Missing");
			return;
		}
		String authToken = authHeader.substring(7);
		String email = "";

		/**
		 * Handling Exceptions in case of Invalid Token and Token Expired
		 */
		try {
			email = authService.getUserEmail(authToken);
			User user = userRepository.findByEmail(email);
			List<Token> listOfTokens = tokenRepository.findAllDisabledTokens(user);
			List<String> disabledTokens = listOfTokens.stream().map(token -> token.getToken())
					.collect(Collectors.toList());
			if (disabledTokens.contains(authToken)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid Token.Token is Disabled");
				return;
			}
		} catch (SignatureException signatureException) {
			logger.info(signatureException.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid Token");
			return;
		} catch (ExpiredJwtException expiredJwtException) {
			logger.info(expiredJwtException.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token Expired.Please SignIn Again");
			return;
		}

		/**
		 * SecurityContext represents the security state of the application running.It
		 * contains the information of the current user,their authentication status and
		 * granted authorities.
		 * 
		 */
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			/**
			 * In Spring security UserDetails interface provides convenient way to represent
			 * user details and makes it easy to integrate with spring security's
			 * authentication and authorization mechanisms.
			 */
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			/**
			 * UserNamePasswordAuthenticationToken Simple representation of userName and
			 * password.
			 */
			UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());

			userNamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));

			/**
			 * SecurityContextHolder is used to access the security context that holds the
			 * authentication and authorization information for the current user and we can
			 * also check whether current user is authenticated or not.
			 */
			SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);
			filterChain.doFilter(request, response);
		}

	}

}
