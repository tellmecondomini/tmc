package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Agenda;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Agenda entity.
 */
public interface AgendaRepository extends JpaRepository<Agenda,Long> {

}
