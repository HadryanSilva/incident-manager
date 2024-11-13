package br.com.hadryan.manager.mapper;

import br.com.hadryan.manager.mapper.request.IncidentPostRequest;
import br.com.hadryan.manager.mapper.response.IncidentResponse;
import br.com.hadryan.manager.model.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    Incident postToIncident(IncidentPostRequest incidentPostRequest);

    IncidentResponse incidentToResponse(Incident incident);

}
