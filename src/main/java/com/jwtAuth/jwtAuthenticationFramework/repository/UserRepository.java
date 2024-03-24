package com.jwtAuth.jwtAuthenticationFramework.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwtAuth.jwtAuthenticationFramework.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
