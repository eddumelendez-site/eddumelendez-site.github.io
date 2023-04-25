package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

// tag::dev-time[]
public class TestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.from(DemoApplication::main).with(DemoConfiguration.class).run(args);
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class DemoConfiguration {

        @Bean
        @ServiceConnection
        public PostgreSQLContainer<?> redisContainer() {
            PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:12")
                    .withCopyFileToContainer(MountableFile.forClasspathResource("schema.sql"), "/docker-entrypoint-initdb.d/schema.sql");
            return postgres;
        }

        @Bean
        ApplicationRunner runner(JdbcTemplate jdbcTemplate) {
            return args -> {
                jdbcTemplate.queryForList("SELECT name, lastname FROM users")
                        .forEach(System.out::println);
            };
        }

    }

}
// end::dev-time[]
