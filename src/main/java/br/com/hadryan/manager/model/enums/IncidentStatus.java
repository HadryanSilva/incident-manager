package br.com.hadryan.manager.model.enums;

import lombok.Getter;

@Getter
public enum IncidentStatus {

    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    CLOSED("Closed");

    private final String description;

    IncidentStatus(String description) {
        this.description = description;
    }

}
