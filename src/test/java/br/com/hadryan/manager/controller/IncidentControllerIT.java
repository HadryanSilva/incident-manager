package br.com.hadryan.manager.controller;

import br.com.hadryan.manager.config.IntegrationTestContainers;
import br.com.hadryan.manager.mapper.request.IncidentPostRequest;
import br.com.hadryan.manager.mapper.request.IncidentPutRequest;
import br.com.hadryan.manager.mapper.response.IncidentResponse;
import br.com.hadryan.manager.model.Incident;
import br.com.hadryan.manager.model.enums.IncidentStatus;
import br.com.hadryan.manager.repository.IncidentRepository;
import br.com.hadryan.manager.service.auth.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({IntegrationTestContainers.class})
class IncidentControllerIT {

    public static final String INCIDENT_API_URL = "/api/v1/incidents";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    private HttpHeaders headers;
    @Autowired
    private IncidentRepository incidentRepository;

    @BeforeEach
    public void setup() {
        headers = authenticate();
    }

    @DisplayName("Save an incident successfully")
    @Test
    public void testSaveIncident() {
        var incidentToSave = IncidentPostRequest.builder()
                .name("Incident 1")
                .description("Description of incident 1")
                .build();

        var entity = new HttpEntity<>(incidentToSave, headers);

        var response = restTemplate.postForEntity(INCIDENT_API_URL, entity, IncidentResponse.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(incidentToSave.getName());
        Assertions.assertThat(response.getBody().getDescription()).isEqualTo(incidentToSave.getDescription());
        Assertions.assertThat(response.getBody().getStatus()).isEqualTo(IncidentStatus.OPEN);
        Assertions.assertThat(response.getBody().getCreatedAt()).isNotNull();
    }

    @DisplayName("Find an incident by id successfully")
    @Test
    public void testFindIncidentById() {
        var entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                INCIDENT_API_URL + "/1",
                HttpMethod.GET,
                entity,
                IncidentResponse.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(1);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Incident 1");
        Assertions.assertThat(response.getBody().getDescription()).isEqualTo("Description of incident 1");
        Assertions.assertThat(response.getBody().getStatus()).isEqualTo(IncidentStatus.OPEN);
        Assertions.assertThat(response.getBody().getCreatedAt()).isNotNull();
    }

    @DisplayName("Find an incident when incident not found")
    @Test
    public void testFindByIdShouldReturn404WhenIncidentNotFound() {
        var entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                INCIDENT_API_URL + "/999",
                HttpMethod.GET,
                entity,
                Void.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @DisplayName("Update an incident successfully")
    @Test
    public void testUpdate() {
        var incidentToSave = Incident.builder()
                .name("Incident 1")
                .description("Description of incident 1")
                .status(IncidentStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        incidentRepository.save(incidentToSave);

        var incidentToUpdate = IncidentPutRequest.builder()
                .id(1L)
                .name("Incident 1 modified")
                .description("Description of incident 1 modified")
                .build();

        var entity = new HttpEntity<>(incidentToUpdate, headers);

        var response = restTemplate.exchange(
                INCIDENT_API_URL,
                HttpMethod.PUT,
                entity,
                Void.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @DisplayName("Update an incident when incident not found")
    @Test
    public void testUpdateShouldReturn404WhenIncidentNotFound() {
        var incidentToUpdate = IncidentPutRequest.builder()
                .id(999L)
                .name("Incident 1 modified")
                .description("Description of incident 1 modified")
                .build();

        var entity = new HttpEntity<>(incidentToUpdate, headers);

        var response = restTemplate.exchange(
                INCIDENT_API_URL,
                HttpMethod.PUT,
                entity,
                Void.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @Sql("/sql/insert_incidents.sql")
    public void testFindListIncidents() {
        var entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                INCIDENT_API_URL,
                HttpMethod.GET,
                entity,
                IncidentResponse[].class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).hasSize(20);

        Arrays.stream(response.getBody()).toList().forEach(incident -> {
            Assertions.assertThat(incident.getId()).isNotNull();
            Assertions.assertThat(incident.getName()).isNotNull();
            Assertions.assertThat(incident.getDescription()).isNotNull();
            Assertions.assertThat(incident.getStatus()).isNotNull();
            Assertions.assertThat(incident.getCreatedAt()).isNotNull();
        });
    }

    private HttpHeaders authenticate() {
        var authentication = new UsernamePasswordAuthenticationToken("user", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);

        var headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

}
