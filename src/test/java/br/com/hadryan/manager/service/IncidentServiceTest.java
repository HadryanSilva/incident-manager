package br.com.hadryan.manager.service;

import br.com.hadryan.manager.exception.NotFoundException;
import br.com.hadryan.manager.mapper.IncidentMapper;
import br.com.hadryan.manager.mapper.request.IncidentPostRequest;
import br.com.hadryan.manager.mapper.request.IncidentPutRequest;
import br.com.hadryan.manager.mapper.response.IncidentResponse;
import br.com.hadryan.manager.model.Incident;
import br.com.hadryan.manager.model.enums.IncidentStatus;
import br.com.hadryan.manager.repository.IncidentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @InjectMocks
    private IncidentService incidentService;

    @Mock
    private IncidentMapper incidentMapper;

    @Mock
    private IncidentRepository incidentRepository;

    private List<Incident> incidents;

    private List<IncidentResponse> incidentResponses;

    @BeforeEach
    void setup() {
        incidents = getIncidents();
        incidentResponses = getIncidentResponses();
    }

    @DisplayName("Find all incidents successfully")
    @Test
    public void testFindAll() {
        when(incidentRepository.findAll()).thenReturn(incidents);

        when(incidentMapper.incidentToResponse(Mockito.any(Incident.class)))
                .thenReturn(incidentResponses.get(0), incidentResponses.get(1), incidentResponses.get(2));

        var incidentsFound = incidentService.findAll();

        Assertions.assertThat(incidentsFound).isNotNull();
        Assertions.assertThat(incidentsFound).hasSize(3);

        incidentsFound.forEach(incidentResponse -> {
            Assertions.assertThat(incidentResponse).isNotNull();
            Assertions.assertThat(incidentResponse.getId()).isNotNull();
            Assertions.assertThat(incidentResponse.getName()).isNotNull();
            Assertions.assertThat(incidentResponse.getDescription()).isNotNull();
            Assertions.assertThat(incidentResponse.getStatus()).isNotNull();
        });
    }

    @DisplayName("Find incident by id successfully")
    @Test
    public void testFindById() {
        when(incidentRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(incidents.getFirst()));

        when(incidentMapper.incidentToResponse(Mockito.any(Incident.class)))
                .thenReturn(incidentResponses.getFirst());

        var incidentFound = incidentService.findById(1L);

        Assertions.assertThat(incidentFound).isNotNull();
        Assertions.assertThat(incidentFound.getId()).isEqualTo(1L);
        Assertions.assertThat(incidentFound.getName()).isEqualTo("Incident 1");
        Assertions.assertThat(incidentFound.getDescription()).isEqualTo("Description 1");
        Assertions.assertThat(incidentFound.getStatus()).isEqualTo(IncidentStatus.OPEN);
    }

    @DisplayName("Save an incident successfully")
    @Test
    public void testSave() {
        var incidentPost = IncidentPostRequest.builder()
                .name("Incident 4")
                .description("Description 4")
                .build();

        var incidentToSave = Incident.builder()
                .name("Incident 4")
                .description("Description 4")
                .status(IncidentStatus.OPEN)
                .build();

        when(incidentMapper.postToIncident(Mockito.any(IncidentPostRequest.class)))
                .thenReturn(incidentToSave);

        when(incidentRepository.save(Mockito.any(Incident.class)))
                .thenReturn(incidentToSave);

        when(incidentMapper.incidentToResponse(Mockito.any(Incident.class)))
                .thenReturn(IncidentResponse.builder()
                        .id(4L)
                        .name("Incident 4")
                        .description("Description 4")
                        .status(IncidentStatus.OPEN)
                        .build());

        var incidentSaved = incidentService.save(incidentPost);

        Assertions.assertThat(incidentSaved).isNotNull();
        Assertions.assertThat(incidentSaved.getId()).isEqualTo(4L);
        Assertions.assertThat(incidentSaved.getName()).isEqualTo("Incident 4");
        Assertions.assertThat(incidentSaved.getDescription()).isEqualTo("Description 4");
        Assertions.assertThat(incidentSaved.getStatus()).isEqualTo(IncidentStatus.OPEN);
    }

    @DisplayName("Update an incident successfully")
    @Test
    public void testUpdate() {
        var incidentPut = IncidentPutRequest.builder()
                .id(1L)
                .name("Incident 1 Updated")
                .description("Description 1 Updated")
                .status(IncidentStatus.CLOSED)
                .build();

        var incidentToUpdate = Incident.builder()
                .id(1L)
                .name("Incident 1 Updated")
                .description("Description 1 Updated")
                .status(IncidentStatus.CLOSED)
                .build();

        when(incidentRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(incidents.getFirst()));

        when(incidentRepository.save(Mockito.any(Incident.class)))
                .thenReturn(incidentToUpdate);

        incidentService.update(incidentPut);

        Assertions.assertThat(incidents.getFirst().getName()).isEqualTo("Incident 1 Updated");
        Assertions.assertThat(incidents.getFirst().getDescription()).isEqualTo("Description 1 Updated");
        Assertions.assertThat(incidents.getFirst().getStatus()).isEqualTo(IncidentStatus.CLOSED);
        Assertions.assertThat(incidents.getFirst().getUpdatedAt()).isNotNull();
        Assertions.assertThat(incidents.getFirst().getClosedAt()).isNotNull();
    }

    @DisplayName("Update when incident not found")
    @Test
    public void testUpdateWhenNotFoundIncident() {
        var incidentPut = IncidentPutRequest.builder()
                .id(1L)
                .name("Incident 1 Updated")
                .description("Description 1 Updated")
                .status(IncidentStatus.CLOSED)
                .build();

        when(incidentRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.empty());

        Assertions.assertThatThrownBy(() -> incidentService.update(incidentPut))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Incident not found\"");
    }

    @DisplayName("Delete an incident successfully")
    @Test
    public void testDelete() {
        when(incidentRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(incidents.getFirst()));

        incidentService.delete(1L);

        Mockito.verify(incidentRepository, Mockito.times(1)).delete(Mockito.any(Incident.class));
    }

    private List<Incident> getIncidents() {
        return List.of(
                Incident.builder()
                        .id(1L)
                        .name("Incident 1")
                        .description("Description 1")
                        .status(IncidentStatus.OPEN)
                        .build(),
                Incident.builder()
                        .id(2L)
                        .name("Incident 2")
                        .description("Description 2")
                        .status(IncidentStatus.OPEN)
                        .build(),
                Incident.builder()
                        .id(3L)
                        .name("Incident 3")
                        .description("Description 3")
                        .status(IncidentStatus.OPEN)
                        .build()
        );
    }

    private List<IncidentResponse> getIncidentResponses() {
        return List.of(
                IncidentResponse.builder()
                        .id(1L)
                        .name("Incident 1")
                        .description("Description 1")
                        .status(IncidentStatus.OPEN)
                        .build(),
                IncidentResponse.builder()
                        .id(2L)
                        .name("Incident 2")
                        .description("Description 2")
                        .status(IncidentStatus.OPEN)
                        .build(),
                IncidentResponse.builder()
                        .id(3L)
                        .name("Incident 3")
                        .description("Description 3")
                        .status(IncidentStatus.OPEN)
                        .build()
        );
    }
  
}