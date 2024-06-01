package dev.paajake.url_shortener.url;

import com.github.javafaker.Faker;
import dev.paajake.url_shortener.config.TestContainersConfig;
import dev.paajake.url_shortener.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.transaction.annotation.Transactional;

import static dev.paajake.url_shortener.config.TestContainersConfig.mysqlContainer;
import static org.assertj.core.api.Assertions.assertThat;

@ImportTestcontainers(value = TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Transactional
class UrlRepositoryTest {

	@Autowired
	UrlRepository urlRepository;
	@Autowired
	UserRepository userRepository;

	private final Faker FAKER = new Faker();

	@Test
	void dbConnectionEstablished() {
		assertThat(mysqlContainer.isRunning()).isTrue();
	}

	@Test
	void findDefaultAdminUrlByShortUrlPath() {
		Url url = urlRepository.findByShortUrlPath("myg");
		assertThat(url).isNotNull();
		assertThat(url.getUser().getId()).isEqualTo(1);
	}

	@Test
	void findNonExistingUserByUsername() {
		Url url = urlRepository.findByShortUrlPath("url_" + System.currentTimeMillis());
		assertThat(url).isNull();
	}

	@Test
	void addAndFindUrlByShortUrlPath() {
		Url url = new Url();
		url.setFullUrl("https://"+FAKER.internet().domainName());
		url.setShortUrlPath("xoxo");
		url.setUser(userRepository.findById(2L).get());

		urlRepository.save(url);

		Url retrievedUrl = urlRepository.findByShortUrlPath("xoxo");

		assertThat(retrievedUrl).isNotNull();
	}

	@Test
	void insertAndDeleteUrlFoundByShortUrlPath() {
		assertThat(urlRepository.findAll().size()).as("Number of URL records").isEqualTo(1);

		final String shortUrlPath = "xoxo";
		Url url = new Url();
		url.setFullUrl("https://"+FAKER.internet().domainName());
		url.setShortUrlPath(shortUrlPath);
		url.setUser(userRepository.findById(3L).get());

		urlRepository.save(url);

		assertThat(urlRepository.findAll().size()).as("Number of URL records").isEqualTo(2);

		urlRepository.deleteById(urlRepository.findByShortUrlPath(shortUrlPath).getId());

		assertThat(userRepository.findAll().size()).as("Number of User records").isEqualTo(3);
	}

}