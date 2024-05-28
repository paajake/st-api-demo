package dev.paajake.url_shortener.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")

public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public User getUrlById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@PutMapping("/{id}")
	public User editUser(@RequestBody User user, @PathVariable Long id) {
		return userService.editUser(user, id);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
}
