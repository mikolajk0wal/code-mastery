package com.macias34.codemastery.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.macias34.codemastery.user.entity.UserEntity;
import com.macias34.codemastery.user.entity.UserRole;
import com.macias34.codemastery.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found"));

		List<UserRole> roles = new ArrayList<>(List.of(user.getRole()));

		return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(roles));
	}

	private Collection<GrantedAuthority> mapRolesToAuthorities(List<UserRole> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
	}

}
