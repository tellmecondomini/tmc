package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.OcorrenciaItem;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OcorrenciaItem entity.
 */
public interface OcorrenciaItemRepository extends JpaRepository<OcorrenciaItem,Long> {

}
