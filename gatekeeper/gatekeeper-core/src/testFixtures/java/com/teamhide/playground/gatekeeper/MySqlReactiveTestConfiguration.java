package com.teamhide.playground.gatekeeper;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@TestConfiguration
@Testcontainers
public class MySqlReactiveTestConfiguration {
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
    public ConnectionFactory connectionFactory() {
        final String host = mysqlContainer.getHost();
        final Integer port = mysqlContainer.getMappedPort(MYSQL_PORT);

        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "mysql")
                        .option(HOST, host)
                        .option(PORT, port)
                        .option(USER, MYSQL_USERNAME)
                        .option(PASSWORD, MYSQL_PASSWORD)
                        .option(DATABASE, MYSQL_DATABASE)
                        .build());
    }
}
