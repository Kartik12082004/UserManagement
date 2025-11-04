package com.kartik.UserManagement;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.UserManagement.model.ERole;
import com.kartik.UserManagement.model.Role;
import com.kartik.UserManagement.model.User;
import com.kartik.UserManagement.repository.RoleRepository;
import com.kartik.UserManagement.repository.UserRepository;

@Component
public class AdminUserInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
		Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseGet(() -> {
            System.out.println(">>> Creating ROLE_USER <<<");
            Role newRole = new Role();
            newRole.setName(ERole.ROLE_USER);
            return roleRepository.save(newRole);
        });

		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseGet(() -> {
            System.out.println(">>> Creating ROLE_ADMIN <<<");
            Role newRole = new Role();
            newRole.setName(ERole.ROLE_ADMIN);
            return roleRepository.save(newRole);
        });
        
        if(!userRepository.existsByUsername("admin")) {
        	User adminUser = new User();
        	adminUser.setUsername("admin");
        	adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("password123"));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            adminUser.setRoles(roles);
            userRepository.save(adminUser);
            
            System.out.println(">>> CREATED DEFAULT ADMIN USER <<<");
        }
		
	}

}
