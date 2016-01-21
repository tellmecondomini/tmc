package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import br.com.unifieo.tmc.domain.PrestadorServico;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AvaliaCompetencia entity.
 */
public interface AvaliaCompetenciaRepository extends JpaRepository<AvaliaCompetencia, Long> {

    AvaliaCompetencia findOneByPrestadorServicoAndCompetenciaPrestador(PrestadorServico prestadorServico, CompetenciaPrestador competenciaPrestador);
}
