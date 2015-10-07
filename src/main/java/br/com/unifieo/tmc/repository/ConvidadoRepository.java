package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Convidado;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Convidado entity.
 */
public interface ConvidadoRepository extends JpaRepository<Convidado,Long> {

}
