package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
