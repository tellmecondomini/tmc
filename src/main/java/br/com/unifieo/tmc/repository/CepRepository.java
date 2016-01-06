package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Cep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Cep entity.
 */
public interface CepRepository extends JpaRepository<Cep, Long> {

    Cep findOneByCep(String cep);
}
