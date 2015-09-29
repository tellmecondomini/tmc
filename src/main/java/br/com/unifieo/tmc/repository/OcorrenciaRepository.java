package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Ocorrencia entity.
 */
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia,Long> {

}
