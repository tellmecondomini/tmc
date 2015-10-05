package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.OcorrenciaSubItem;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OcorrenciaSubItem entity.
 */
public interface OcorrenciaSubItemRepository extends JpaRepository<OcorrenciaSubItem,Long> {

}
