package com.kartik.UserManagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.UserManagement.model.User;
import com.kartik.UserManagement.repository.UserRepository;
import com.kartik.UserManagement.service.UserDetailsImplementation;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/users/me")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<UserDetailsImplementation> getCurrentUserProfile(
			@AuthenticationPrincipal UserDetailsImplementation userDetails){
		return ResponseEntity.ok(userDetails);
	}
	
	@GetMapping("/admin/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDetailsImplementation>> getAllUsers(){
		List<User> users = userRepository.findAll();
		
		List<UserDetailsImplementation> userProfiles = users.stream()
				.map(User -> UserDetailsImplementation.build(User))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(userProfiles);
	}
	
}
