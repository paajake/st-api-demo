package dev.paajake.url_shortener.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")

public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUrlById(@PathVariable Long id) {
		return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
		return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> editUser(@RequestBody @Valid User user, @PathVariable Long id) {
		return new ResponseEntity<>(userService.editUser(user, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.valueOf(204));
	}
}
