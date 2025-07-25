package com.idcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	 	private final JwtAuthenticationFilter jwtAuthFilter;
	    private final AuthenticationProvider authenticationProvider;
	    private final CorsConfig corsConfig;


	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf((csrf) -> csrf.disable())
	            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
	            .authorizeHttpRequests((requests) -> requests
	            	.requestMatchers("/alluser/**").permitAll()
	            	.requestMatchers("/auth/**").permitAll()
	                .requestMatchers("/login").permitAll() // Allow access to login
	                .requestMatchers("/user/**").permitAll() // Allow access to user endpoints
	                .requestMatchers("/admin/**").permitAll()
	                .anyRequest().permitAll()) // Allow access to all other requests
	            .sessionManagement((sessionManagement) -> sessionManagement
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authenticationProvider(authenticationProvider)
	            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

	   
}
