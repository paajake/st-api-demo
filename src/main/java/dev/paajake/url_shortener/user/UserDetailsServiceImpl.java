package dev.paajake.url_shortener.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userDetails = userRepository.findUserByUsername(username);

		if (userDetails == null) {
			throw new UsernameNotFoundException("User with username: " + username + " not found");
		}

		// Create and return the Spring Security UserDetails object
		return org.springframework.security.core.userdetails.User.builder()
				.username(userDetails.getUsername())
				.password(userDetails.getPassword())
				.roles(userDetails.getRole().name())
				.build();
	}
}
