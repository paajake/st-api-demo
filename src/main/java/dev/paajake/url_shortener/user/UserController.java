package dev.paajake.url_shortener.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR @authenticatedUserService.hasId(#id)")
	public ResponseEntity<User> getUrlById(@PathVariable Long id) {
		return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
		return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUrls() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR @authenticatedUserService.hasId(#id)")
	public ResponseEntity<User> editUser(@RequestBody @Valid User user, @PathVariable Long id) {
		return new ResponseEntity<>(userService.editUser(user, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR @authenticatedUserService.hasId(#id)")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.valueOf(204));
	}
}
