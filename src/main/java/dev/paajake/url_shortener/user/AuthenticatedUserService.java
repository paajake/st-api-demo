package dev.paajake.url_shortener.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticatedUserService {
	private final UserRepository userRepository;

	public AuthenticatedUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean hasId(Long id) {
		String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal()).getUsername();
		User user = userRepository.findUserByUsername(username);
		return Objects.equals(user.getId(), id);
	}
}
