package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AvaliaCompetencia entity.
 */
public interface AvaliaCompetenciaRepository extends JpaRepository<AvaliaCompetencia,Long> {

}
