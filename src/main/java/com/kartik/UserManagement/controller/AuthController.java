package com.kartik.UserManagement.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.UserManagement.dto.request.LoginRequest;
import com.kartik.UserManagement.dto.request.SignupRequest;
import com.kartik.UserManagement.dto.response.JwtResponse;
import com.kartik.UserManagement.dto.response.MessageResponse;
import com.kartik.UserManagement.model.ERole;
import com.kartik.UserManagement.model.Role;
import com.kartik.UserManagement.model.User;
import com.kartik.UserManagement.repository.RoleRepository;
import com.kartik.UserManagement.repository.UserRepository;
import com.kartik.UserManagement.security.jwt.JwtService;
import com.kartik.UserManagement.service.UserDetailsImplementation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/truth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	  @Autowired
	  UserRepository userRepository;

	  @Autowired
	  RoleRepository roleRepository;

	  @Autowired
	  PasswordEncoder encoder;

	  @Autowired
	  JwtService jwtService;

	  @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);    
	    UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();  
	    String jwt = jwtService.GenerateToken(userDetails.getUsername());
	    
	    List<String> roles = userDetails.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());

	    return ResponseEntity.ok(new JwtResponse(jwt, 
	                         userDetails.getId(), 
	                         userDetails.getUsername(), 
	                         userDetails.getEmail(), 
	                         roles));
	  }

	  @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Email is already in use!"));
	    } 

	    User user = new User(signUpRequest.getUsername(), 
	               signUpRequest.getEmail(),
	               encoder.encode(signUpRequest.getPassword()));

	   Set<Role> roles = new HashSet<>();
	   Role userRole = roleRepository.findByName(ERole.ROLE_USER)
			   .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
	   roles.add(userRole);

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
}
