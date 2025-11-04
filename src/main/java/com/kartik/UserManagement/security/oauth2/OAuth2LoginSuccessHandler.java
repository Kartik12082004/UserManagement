package com.kartik.UserManagement.security.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.kartik.UserManagement.security.jwt.JwtService;
import com.kartik.UserManagement.service.UserDetailsImplementation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
    private JwtService jwtService;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String jwt = jwtService.GenerateToken(userDetails.getUsername());

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", jwt)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
	
}
