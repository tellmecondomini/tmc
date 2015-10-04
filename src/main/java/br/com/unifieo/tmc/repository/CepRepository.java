package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Cep;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cep entity.
 */
public interface CepRepository extends JpaRepository<Cep,Long> {

}
