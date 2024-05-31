package dev.paajake.url_shortener.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class TestContainersConfig {
	@Container
	@ServiceConnection
	public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8");
}
