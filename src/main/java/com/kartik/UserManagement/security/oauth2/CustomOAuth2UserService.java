package com.kartik.UserManagement.security.oauth2;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kartik.UserManagement.model.ERole;
import com.kartik.UserManagement.model.Role;
import com.kartik.UserManagement.model.User;
import com.kartik.UserManagement.repository.RoleRepository;
import com.kartik.UserManagement.repository.UserRepository;
import com.kartik.UserManagement.service.UserDetailsImplementation;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User();
            user.setEmail(email);

            String username = oAuth2User.getAttribute("name");
            if (!StringUtils.hasText(username)) {
                username = email.split("@")[0];
            }
            
            if (userRepository.existsByUsername(username)) {
                username = username + "_" + System.currentTimeMillis();
            }
            user.setUsername(username);
            
            user.setPassword("");

            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            user.setRoles(roles);
            
            userRepository.save(user);
        }
        
        UserDetailsImplementation userDetails = UserDetailsImplementation.build(user);
        userDetails.setAttributes(oAuth2User.getAttributes());
        
        return userDetails;
    } 
	
}
