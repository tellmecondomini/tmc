package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.TelefoneMorador;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TelefoneMorador entity.
 */
public interface TelefoneMoradorRepository extends JpaRepository<TelefoneMorador, Long> {

}
