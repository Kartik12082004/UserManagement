package com.kartik.UserManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kartik.UserManagement.model.User;
import com.kartik.UserManagement.repository.UserRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
		    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		return UserDetailsImplementation.build(user);
	}

}
