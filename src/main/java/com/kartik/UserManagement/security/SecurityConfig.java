package com.kartik.UserManagement.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import com.kartik.UserManagement.security.jwt.AuthEntryPointJwt;
import com.kartik.UserManagement.security.jwt.JwtFilter;
import com.kartik.UserManagement.security.oauth2.CustomOAuth2UserService;
import com.kartik.UserManagement.security.oauth2.OAuth2LoginSuccessHandler;
import com.kartik.UserManagement.service.UserDetailsServiceImplementation;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	UserDetailsServiceImplementation userDetailsService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(withDefaults())
		.csrf(csrf -> csrf.disable())
		.exceptionHandling(Exception -> Exception.authenticationEntryPoint(unauthorizedHandler))
		.sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth ->
		auth.requestMatchers("/api/truth/**").permitAll()
		.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
		.requestMatchers("/actuator/**").hasRole("ADMIN")
		.anyRequest().authenticated()
		)
		.oauth2Login(oauth2 ->
		oauth2
		.userInfoEndpoint(UserInfo -> 
		UserInfo.userService(customOAuth2UserService)
		)
		.successHandler((oAuth2LoginSuccessHandler)
		));
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
}
