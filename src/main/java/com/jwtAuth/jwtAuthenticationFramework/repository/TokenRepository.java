package com.jwtAuth.jwtAuthenticationFramework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jwtAuth.jwtAuthenticationFramework.model.Token;
import com.jwtAuth.jwtAuthenticationFramework.model.User;

public interface TokenRepository extends JpaRepository<Token, Long> {

	@Query("Select t from Token t where t.user=:user and t.tokenStatus='ENABLE'")
	Token findByUser(@Param("user") User user);
	
	@Query("Select t from Token t where t.user=:user and t.tokenStatus='DISABLE'")
	List<Token> findAllDisabledTokens(@Param("user") User user);
}
