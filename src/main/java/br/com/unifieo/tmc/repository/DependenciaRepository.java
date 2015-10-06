package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Dependencia;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dependencia entity.
 */
public interface DependenciaRepository extends JpaRepository<Dependencia,Long> {

}
