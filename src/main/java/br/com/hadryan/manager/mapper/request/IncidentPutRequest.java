package br.com.hadryan.manager.mapper.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentPutRequest {

    private Long id;

    private String name;

    private String description;

}
