package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.OcorrenciaPrioridade;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OcorrenciaPrioridade entity.
 */
public interface OcorrenciaPrioridadeRepository extends JpaRepository<OcorrenciaPrioridade,Long> {

}
