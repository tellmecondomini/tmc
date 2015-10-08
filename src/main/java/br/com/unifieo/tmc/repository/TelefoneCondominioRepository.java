package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.TelefoneCondominio;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TelefoneCondominio entity.
 */
public interface TelefoneCondominioRepository extends JpaRepository<TelefoneCondominio,Long> {

}
