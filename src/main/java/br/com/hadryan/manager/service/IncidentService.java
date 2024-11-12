package br.com.hadryan.manager.service;

import br.com.hadryan.manager.exception.NotFoundException;
import br.com.hadryan.manager.mapper.IncidentMapper;
import br.com.hadryan.manager.mapper.request.IncidentPostRequest;
import br.com.hadryan.manager.mapper.request.IncidentPutRequest;
import br.com.hadryan.manager.mapper.response.IncidentResponse;
import br.com.hadryan.manager.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    private final IncidentMapper incidentMapper;

    public List<IncidentResponse> findAll() {
        log.info("Finding all incidents");
        return incidentRepository.findAll()
                .stream()
                .map(incidentMapper::incidentToResponse)
                .toList();
    }

    public IncidentResponse findById(Long id) {
        log.info("Finding incident by id: {}", id);
        var incident = incidentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident not found"));
        return incidentMapper.incidentToResponse(incident);
    }

    public List<IncidentResponse> findLastIncidents() {
        log.info("Finding last incidents");
        return incidentRepository.findLastIncidents()
                .stream()
                .map(incidentMapper::incidentToResponse)
                .toList();
    }

    public IncidentResponse save(IncidentPostRequest request) {
        log.info("Saving incident: {}", request.getName());
        var incidentToSave = incidentMapper.postToIncident(request);
        incidentToSave.setCreatedAt(LocalDateTime.now());
        var savedIncident = incidentRepository.save(incidentToSave);
        return incidentMapper.incidentToResponse(savedIncident);
    }

    public void update(IncidentPutRequest request) {
        log.info("Updating incident by id: {}", request.getId());
        var incidentToUpdate = incidentRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Incident not found"));
        incidentToUpdate.setName(request.getName());
        incidentToUpdate.setDescription(request.getDescription());
        incidentToUpdate.setUpdatedAt(LocalDateTime.now());
        incidentRepository.save(incidentToUpdate);
    }

    public void delete(Long id) {
        log.info("Deleting incident by id: {}", id);
        var incidentToDelete = incidentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident not found"));
        incidentRepository.delete(incidentToDelete);
    }

}
