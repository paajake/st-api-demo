package dev.paajake.url_shortener.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createUser(User user) {
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setRole(Role.USER);
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"User with this ID was NOT Found!"));
	}

	public User editUser(User user, Long id) {

		return userRepository.findById(id)
				.map(userTobeUpdated -> {

					userTobeUpdated.setEmail(user.getEmail());
					userTobeUpdated.setFirstName(user.getFirstName());
					userTobeUpdated.setLastName(user.getLastName());
					userTobeUpdated.setPhoneNumber(user.getPhoneNumber());
					userTobeUpdated.setImageUrl(user.getImageUrl());

					return userRepository.save(userTobeUpdated);
				})
				.orElseGet(() -> {
					user.setId(id);
					return userRepository.save(user);
				});
	}

	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}
