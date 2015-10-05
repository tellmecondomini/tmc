package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.OcorrenciaTipo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OcorrenciaTipo entity.
 */
public interface OcorrenciaTipoRepository extends JpaRepository<OcorrenciaTipo,Long> {

}
