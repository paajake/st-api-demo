package dev.paajake.url_shortener.user;

import com.github.javafaker.Faker;
import dev.paajake.url_shortener.config.TestContainersConfig;
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
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	private final Faker FAKER = new Faker();

	@Test
	void dbConnectionEstablished() {
		assertThat(mysqlContainer.isRunning()).isTrue();
	}

	@Test
	void findDefaultAdminByUsername() {
		User admin = userRepository.findUserByUsername("admin");
		assertThat(admin).isNotNull();
		assertThat(admin.getUsername()).isEqualTo("admin");
	}

	@Test
	void findNonExistingUserByUsername() {
		User user = userRepository.findUserByUsername("user_" + System.currentTimeMillis());
		assertThat(user).isNull();
	}

	@Test
	void addAndFindUserByUsername() {
		User userForInsert = new User();

		userForInsert.setEmail(FAKER.internet().emailAddress());
		userForInsert.setUsername(FAKER.name().username());
		userForInsert.setPassword(FAKER.internet().password());
		userForInsert.setFirstName(FAKER.name().firstName());
		userForInsert.setLastName(FAKER.name().lastName());
		userForInsert.setRole(Role.ADMIN);

		userRepository.save(userForInsert);

		User user = userRepository.findUserByUsername(userForInsert.getUsername());

		assertThat(user).isNotNull();
	}

	@Test
	void insertAndDeleteUserFoundByUserName() {
		assertThat(userRepository.findAll().size()).as("Number of User records").isEqualTo(3);

		User userForInsert = new User();

		userForInsert.setEmail(FAKER.internet().emailAddress());
		userForInsert.setUsername(FAKER.name().username());
		userForInsert.setPassword(FAKER.internet().password());
		userForInsert.setFirstName(FAKER.name().firstName());
		userForInsert.setLastName(FAKER.name().lastName());
		userForInsert.setRole(Role.USER);

		userRepository.save(userForInsert);

		assertThat(userRepository.findAll().size()).as("Number of User records").isEqualTo(4);

		User user = userRepository.findUserByUsername(userForInsert.getUsername());
		userRepository.deleteById(user.getId());

		assertThat(userRepository.findAll().size()).as("Number of User records").isEqualTo(3);
	}

}