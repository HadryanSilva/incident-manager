package br.com.hadryan.manager.mapper;

import br.com.hadryan.manager.mapper.request.IncidentPostRequest;
import br.com.hadryan.manager.mapper.response.IncidentResponse;
import br.com.hadryan.manager.model.Incident;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    Incident postToIncident(IncidentPostRequest incidentPostRequest);

    IncidentResponse incidentToResponse(Incident incident);

}
