package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Assunto;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Assunto entity.
 */
public interface AssuntoRepository extends JpaRepository<Assunto,Long> {

}
