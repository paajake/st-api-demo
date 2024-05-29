package dev.paajake.url_shortener.url;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/urls")
public class UrlController {
	private final UrlService urlService;

	public UrlController(UrlService urlService) {
		this.urlService = urlService;
	}

	@GetMapping
	public ResponseEntity<List<Url>> getAllUrls() {
		return new ResponseEntity<>(urlService.getAllUrls(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR @authenticatedUserService.hasId(#id)")
	public ResponseEntity<Url> getUrlById(@PathVariable Long id) {
		return new ResponseEntity<>(urlService.getUrlById(id), HttpStatus.OK);
	}

	@GetMapping("/redirect/{shortUrlPath}")
	public ResponseEntity<Void> redirect(@PathVariable String shortUrlPath) {
		return ResponseEntity
				.status(HttpStatus.MOVED_PERMANENTLY)
				.header(HttpHeaders.LOCATION, urlService.getFullUrlForRedirect(shortUrlPath))
				.build();
	}

	@PostMapping
	public ResponseEntity<Url> createUrl(@RequestBody @Valid Url url, @AuthenticationPrincipal UserDetails userDetails) {
		return new ResponseEntity<>(urlService.createUrl(url, userDetails.getUsername()), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR @authenticatedUserService.hasId(#id)")
	public ResponseEntity<Url> editUrl(@RequestBody @Valid Url url, @PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {
		return new ResponseEntity<>(urlService.editUrl(url, id, userDetails.getUsername()), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR @authenticatedUserService.hasId(#id)")
	public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
		urlService.deleteUrl(id);
		return new ResponseEntity<>(HttpStatus.valueOf(204));
	}

}
