package br.com.hadryan.manager.controller;

import br.com.hadryan.manager.mapper.request.IncidentPostRequest;
import br.com.hadryan.manager.mapper.request.IncidentPutRequest;
import br.com.hadryan.manager.mapper.response.IncidentResponse;
import br.com.hadryan.manager.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> findAll() {
        return ResponseEntity.ok(incidentService.findAll());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<IncidentResponse>> findLastIncidents() {
        return ResponseEntity.ok(incidentService.findLastIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> save(@RequestBody IncidentPostRequest request) {
        var incident = incidentService.save(request);
        return ResponseEntity.created(URI.create("/api/v1/incidents/" + incident.getId())).body(incident);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody IncidentPutRequest request) {
        incidentService.update(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
