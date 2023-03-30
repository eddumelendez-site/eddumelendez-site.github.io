package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcServiceConnection;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@JdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceConnectionTest {

    // tag::jdbc-service-connection[]
    @Container
    @JdbcServiceConnection // <1>
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12")
            .withCopyFileToContainer(MountableFile.forClasspathResource("schema.sql"), "/docker-entrypoint-initdb.d/schema.sql");
    // end::jdbc-service-connection[]

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void readData() {
        var user = this.jdbcTemplate.queryForMap("SELECT name, lastname FROM users WHERE username= 'eddumelendez'");
        assertThat(user).containsExactly(entry("name", "Eddú"), entry("lastname", "Meléndez"));
    }

}
