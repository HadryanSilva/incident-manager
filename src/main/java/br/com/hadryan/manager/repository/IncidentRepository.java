package br.com.hadryan.manager.repository;

import br.com.hadryan.manager.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    @Query(value = "SELECT * FROM incident ORDER BY created_at DESC LIMIT 20", nativeQuery = true)
    List<Incident> findLastIncidents();

}
