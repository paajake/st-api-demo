package dev.paajake.url_shortener.url;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/urls")
public class UrlController {
	private final UrlService urlService;

	public UrlController(UrlService urlService) {
		this.urlService = urlService;
	}

	@GetMapping("/{id}")
	public Url getUrlById(@PathVariable Long id) {
		return urlService.getUrlById(id);
	}

	@GetMapping("/redirect/{shortUrlPath}")
	public ResponseEntity<Void> redirect(@PathVariable String shortUrlPath) {
		return urlService.redirect(shortUrlPath);
	}

	@PostMapping
	public Url createUrl(@RequestBody Url url) {
		return urlService.createUrl(url);
	}


	@PutMapping("/{id}")
	public Url editUrl(@RequestBody Url url, @PathVariable Long id) {
		return urlService.editUrl(url, id);
	}

	@DeleteMapping("/{id}")
	public void deleteUrl(@PathVariable Long id) {
		urlService.deleteUrl(id);
	}

}
