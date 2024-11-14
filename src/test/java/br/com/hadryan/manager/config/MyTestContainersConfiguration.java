package br.com.hadryan.manager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static br.com.hadryan.manager.config.IntegrationTestContainers.POSTGRE_SQL_CONTAINER;

@TestConfiguration(proxyBeanMethods = false)
@Import(IntegrationTestContainers.class)
public class MyTestContainersConfiguration {

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return POSTGRE_SQL_CONTAINER;
    }

}
