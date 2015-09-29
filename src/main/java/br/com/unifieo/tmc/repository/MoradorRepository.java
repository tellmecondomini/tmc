package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Morador;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Morador entity.
 */
public interface MoradorRepository extends JpaRepository<Morador,Long> {

}
