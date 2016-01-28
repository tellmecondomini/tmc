package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.PrestadorServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AvaliaCompetencia entity.
 */
public interface AvaliaCompetenciaRepository extends JpaRepository<AvaliaCompetencia, Long> {

    List<AvaliaCompetencia> findAllByPrestadorServicoAndCompetenciaPrestador(PrestadorServico prestadorServico, CompetenciaPrestador competenciaPrestador);

    List<AvaliaCompetencia> findAllByPrestadorServicoAndCompetenciaPrestadorAndMorador(PrestadorServico prestadorServico, CompetenciaPrestador competenciaPrestador, Morador morador);
}
