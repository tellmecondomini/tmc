package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Dependente;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dependente entity.
 */
public interface DependenteRepository extends JpaRepository<Dependente,Long> {

}
