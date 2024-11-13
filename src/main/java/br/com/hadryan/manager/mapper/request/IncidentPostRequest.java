package br.com.hadryan.manager.mapper.request;

import br.com.hadryan.manager.model.enums.IncidentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class IncidentPostRequest {

    private String name;

    private String description;

    private IncidentStatus status;

}
