package dev.paajake.url_shortener.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
	Url findByShortUrlPath(String hash);
}
