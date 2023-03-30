package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@JdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DynamicPropertySourceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // tag::container[]
    @Container // <1>
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12") // <2>
            .withCopyFileToContainer(MountableFile.forClasspathResource("schema.sql"), "/docker-entrypoint-initdb.d/schema.sql"); // <3>
    // end::container[]

    // tag::dynamic-property-source[]
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) { // <1>
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
    // end::dynamic-property-source[]

    @Test
    void readData() {
        var user = this.jdbcTemplate.queryForMap("SELECT name, lastname FROM users WHERE username= 'eddumelendez'");
        assertThat(user).containsExactly(entry("name", "Eddú"), entry("lastname", "Meléndez"));
    }

}
