package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Topico;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Topico entity.
 */
public interface TopicoRepository extends JpaRepository<Topico,Long> {

}
