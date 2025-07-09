package com.teamhide.playground.gatekeeper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.Duration;

@TestConfiguration
@Testcontainers
public class MySqlTestConfiguration {
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final int MYSQL_PORT = 3306;
    private static final String MYSQL_USERNAME = "testuser";
    private static final String MYSQL_PASSWORD = "testpass";
    private static final String MYSQL_DATABASE = "testdb";
    private static final String MYSQL_ROOT_PASSWORD = "testrootpass";

    @Container
    public static final GenericContainer<?> mysqlContainer =
            new GenericContainer<>(MYSQL_IMAGE)
                    .withEnv("MYSQL_DATABASE", MYSQL_DATABASE)
                    .withEnv("MYSQL_USER", MYSQL_USERNAME)
                    .withEnv("MYSQL_PASSWORD", MYSQL_PASSWORD)
                    .withEnv("MYSQL_ROOT_PASSWORD", MYSQL_ROOT_PASSWORD)
                    .withExposedPorts(MYSQL_PORT)
                    .waitingFor(Wait.forListeningPort())
                    .withStartupTimeout(Duration.ofSeconds(30));

    static {
        mysqlContainer.start();
    }

    @Bean
    public DataSource dataSource() {
        final String host = mysqlContainer.getHost();
        final Integer port = mysqlContainer.getMappedPort(MYSQL_PORT);
        final String url =
                String.format(
                        "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul",
                        host, port, MYSQL_DATABASE);

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(MYSQL_USERNAME);
        config.setPassword(MYSQL_PASSWORD);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
