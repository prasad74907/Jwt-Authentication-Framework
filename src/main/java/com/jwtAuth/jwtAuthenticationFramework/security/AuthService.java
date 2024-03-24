package com.jwtAuth.jwtAuthenticationFramework.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class AuthService {

	/**
	 * Base64 encoded key that is used to digitally sign the token and same is
	 * verified while extracting claims from token
	 */
	private static final String SECRET_KEY = "1dedad4a7523e2338d618d18e319f420ff4c240eeef9866f58bc50c06e2f6419";

	/**
	 * Note:- We make use of JJWT(Java JWT) library to create , validate , parse
	 * JSON Web Token to extract claims from the token. Dependencies for the same
	 * need to be added in POM.xml file.
	 */

	/**
	 * Method to extract email from authToken
	 * 
	 * @param authToken
	 * @return
	 * @throws InvalidTokenException
	 * @throws SignatureException
	 */
	public String getUserEmail(String authToken) {

		return extractClaims(authToken).getSubject();

	}

	/**
	 * Method to check if the token is expired or not.
	 * 
	 * @param authToken
	 * @return
	 * @throws InvalidTokenException
	 * @throws SignatureException
	 */
	public boolean isTokenExpired(String authToken) {

		return getTokenExpirationDate(authToken).before(new Date());
	}

	/**
	 * Method to extract claims.
	 * 
	 * @param jwtToken
	 * @return
	 * @throws InvalidTokenException
	 */
	public Claims extractClaims(String jwtToken) {

		/**
		 * JWTS is a utility class provided by JJWT to create , parse JSON web token to
		 * extract claims.
		 */
		Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
		return claims;
	}

	/**
	 * Method to get the signing Key.
	 * 
	 * @return
	 */
	private Key getSigningKey() {
		byte[] signingKey = Decoders.BASE64.decode(SECRET_KEY);

		// Creating new Secret-key using HMAC-SHA algorithm
		return Keys.hmacShaKeyFor(signingKey);
	}

	/**
	 * Method to extract token expiration date.
	 * 
	 * @param authToken
	 * @return
	 * @throws InvalidTokenException
	 * @throws SignatureException
	 */
	private Date getTokenExpirationDate(String authToken) {
		return extractClaims(authToken).getExpiration();
	}

	/**
	 * Method to generate token.
	 * 
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {

		return Jwts.builder().setClaims(new HashMap<String, Object>()).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60))).signWith(getSigningKey())
				.compact();

	}

}
