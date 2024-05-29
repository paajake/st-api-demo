package dev.paajake.url_shortener.url;

import com.google.common.hash.Hashing;
import dev.paajake.url_shortener.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@Slf4j
public class UrlService {
	private final UrlRepository urlRepository;
	private final UserRepository userRepository;

	public UrlService(UrlRepository urlRepository, UserRepository userRepository) {
		this.urlRepository = urlRepository;
		this.userRepository = userRepository;
	}

	private boolean urlHashIsUnique(final String hash) {
		return Objects.isNull(urlRepository.findByShortUrlPath(hash));
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

	public String getFullUrlForRedirect(String shortUrlPath) {
		Url url = urlRepository.findByShortUrlPath(shortUrlPath);
		if (Objects.isNull(url)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Unknown Destination URL with this short URL Path: " + shortUrlPath);
		}

		url.setClicks(url.getClicks() + 1);
		urlRepository.save(url);

		return url.getFullUrl();
	}

	public Url createUrl(Url url, final String username) {
		if (url.getShortUrlPath() == null || url.getShortUrlPath().isEmpty()) {
			url.setShortUrlPath(getUrlPathHash(url.getFullUrl()));
		} else {
			if (!urlHashIsUnique(url.getShortUrlPath().toLowerCase())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"The short url already exists, kindly pass a unique url path");
			}
		}
		url.setUser(userRepository.findUserByUsername(username));
		return urlRepository.save(url);
	}

	public Url editUrl(Url url, Long id, final String username) {

		return urlRepository.findById(id)
				.map(urlTobeUpdated -> {
					url.setUser(userRepository.findUserByUsername(username));

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
