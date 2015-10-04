package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Condominio;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Condominio entity.
 */
public interface CondominioRepository extends JpaRepository<Condominio,Long> {

}
