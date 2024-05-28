package dev.paajake.url_shortener.url;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UrlService {
	private final UrlRepository urlRepository;

	public UrlService(UrlRepository urlRepository) {
		this.urlRepository = urlRepository;
	}

	private boolean urlHashIsUnique(final String hash) {
		return urlRepository.findByShortUrlPath(hash).isEmpty();
	}

	private String getUrlPathHash(final String fullUrl) {
		final String fullHashedString = Hashing.sha256()
				.hashString(fullUrl, StandardCharsets.UTF_8)
				.toString();

		int subStringIndex = 4;
		String urlHash;
		do {
			subStringIndex++;
			urlHash = fullHashedString.substring(0, subStringIndex);
			if (subStringIndex >= fullHashedString.length()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A unique url could not be found");
			}
		} while (!urlHashIsUnique(urlHash));

		log.info(String.format("Shortened hash for `%s` is `%s`", fullUrl, urlHash));
		return urlHash;
	}

	public Url getUrlById(Long id) {
		return urlRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL with this ID was NOT Found!"));
	}

	public ResponseEntity<Void> redirect(String shortUrlPath) {
		List<Url> urls = urlRepository.findByShortUrlPath(shortUrlPath);
		if (urls.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Unknown Destination URL with this short URL Path: " + shortUrlPath);
		}

		Url url = urls.getFirst();
		url.setClicks(url.getClicks() + 1);
		urlRepository.save(url);

		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, urls.getFirst()
				.getFullUrl()).build();
	}

	public Url createUrl(Url url) {
		if (url.getShortUrlPath() == null || url.getShortUrlPath().isEmpty()) {
			url.setShortUrlPath(getUrlPathHash(url.getFullUrl()));
		} else {
			if (!urlHashIsUnique(url.getShortUrlPath().toLowerCase())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"The short url already exists, kindly pass a unique url path");
			}
		}



		return urlRepository.save(url);
	}

	public Url editUrl(Url url, Long id) {

		return urlRepository.findById(id)
				.map(urlTobeUpdated -> {

					if (!Objects.isNull(url.getShortUrlPath()) && !url.getShortUrlPath().isEmpty()) {
						if (!urlHashIsUnique(url.getShortUrlPath().toLowerCase())) {
							throw new ResponseStatusException(HttpStatus.CONFLICT,
									"The short url already exists, kindly pass a unique url path");
						}
						urlTobeUpdated.setShortUrlPath(url.getShortUrlPath());
					}

					if (!Objects.isNull(url.getFullUrl()) && !url.getFullUrl().isEmpty()) {
						urlTobeUpdated.setFullUrl(url.getFullUrl());
					}

					return urlRepository.save(urlTobeUpdated);
				})
				.orElseGet(() -> {
					url.setId(id);
					return urlRepository.save(url);
				});
	}

	public void deleteUrl(Long id) {
		urlRepository.deleteById(id);
	}

}
