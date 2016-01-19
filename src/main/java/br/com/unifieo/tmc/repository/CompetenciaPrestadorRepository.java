package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the CompetenciaPrestador entity.
 */
public interface CompetenciaPrestadorRepository extends JpaRepository<CompetenciaPrestador,Long> {

}
