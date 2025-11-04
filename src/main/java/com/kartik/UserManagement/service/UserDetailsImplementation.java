package com.kartik.UserManagement.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kartik.UserManagement.model.User;

import lombok.Getter;

public class UserDetailsImplementation implements UserDetails, OAuth2User {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private Long id;

	private String username;

	@Getter
	private String email;
	
	@JsonIgnore
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	private Map<String, Object> attributes;
	
	public UserDetailsImplementation(Long id, String username, String email, String password,	
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	public static UserDetailsImplementation build(User user) {
	    List<GrantedAuthority> authorities = user.getRoles().stream()
	        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
	        .collect(Collectors.toList());

	    return new UserDetailsImplementation(
	            user.getId(), 
	            user.getUsername(), 
	            user.getEmail(),
	            user.getPassword(), 
	            authorities);
	  }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
	  return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	  return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	  return true;
	}
	
	@Override
	  public boolean isEnabled() {
	    return true;
	  }

	@Override
	public boolean equals(Object o) {
	  if (this == o)
	    return true;
	  if (o == null || getClass() != o.getClass())
	    return false;
	  UserDetailsImplementation user = (UserDetailsImplementation) o;
	  return Objects.equals(id, user.id);
	  }

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return this.username;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
