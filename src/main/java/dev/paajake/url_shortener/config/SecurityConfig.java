package dev.paajake.url_shortener.config;

import dev.paajake.url_shortener.user.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity, UserDetailsServiceImpl userDetailsService)
			throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(
				AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		return
				httpSecurity
						.cors(AbstractHttpConfigurer::disable)
						.csrf(AbstractHttpConfigurer::disable)
						.authorizeHttpRequests(auth -> auth
								.requestMatchers("/error").permitAll()
								.requestMatchers("/actuator/**").permitAll()

								.requestMatchers(HttpMethod.POST, "/users").permitAll()
								.requestMatchers(HttpMethod.GET, "/urls/redirect/**").permitAll()

								.anyRequest().authenticated()
						)
						.authenticationManager(authenticationManager)
						.httpBasic(Customizer.withDefaults())
						.build();
	}
}
