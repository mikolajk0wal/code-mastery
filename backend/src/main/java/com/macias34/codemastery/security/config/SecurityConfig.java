package com.macias34.codemastery.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.macias34.codemastery.security.jwt.JwtAuthenticationFilter;
import com.macias34.codemastery.security.jwt.JwtEntryPoint;
import com.macias34.codemastery.security.service.CustomUserDetailsService;
import com.macias34.codemastery.user.entity.UserRole;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private CustomUserDetailsService userDetailService;
	private JwtEntryPoint authEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors().and() // <-- DODANE: włączenie CORS w Spring Security
				.csrf().disable()
				.exceptionHandling()
				.authenticationEntryPoint(authEntryPoint)
				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.requestMatchers(HttpMethod.POST, "/information-page", "/lesson", "/property", "/chapter", "/category",
						"/course")
				.hasAuthority(UserRole.ADMIN.name())
				.requestMatchers(HttpMethod.PATCH, "/information-page", "/lesson", "/property", "/chapter", "/category",
						"/course")
				.hasAuthority(UserRole.ADMIN.name())
				.requestMatchers(HttpMethod.DELETE, "/information-page", "/lesson", "/property", "/chapter",
						"/category", "/course")
				.hasAuthority(UserRole.ADMIN.name())
				.requestMatchers(HttpMethod.PATCH, "/user/{id}").authenticated()
				.anyRequest().permitAll()
				.and().httpBasic();

		http.addFilterBefore(jwtAuthenticationFilter(),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList(
				"https://www.code-mastery.app",
				"https://code-mastery.app",
				"http://localhost:3000"
		));

		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));

		configuration.setAllowCredentials(true);

		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

}