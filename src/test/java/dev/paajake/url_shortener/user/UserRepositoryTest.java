package dev.paajake.url_shortener.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
	@Container
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8").withStartupTimeoutSeconds(180);
	@Autowired
	UserRepository userRepository;

	@Test
	void dbConnectionEstablished() {
		assertThat(mysqlContainer.isRunning()).isTrue();
	}

	@Test
	void findUserByUsername() {
	}
}