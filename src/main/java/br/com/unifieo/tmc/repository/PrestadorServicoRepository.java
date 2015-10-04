package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.PrestadorServico;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PrestadorServico entity.
 */
public interface PrestadorServicoRepository extends JpaRepository<PrestadorServico,Long> {

}
