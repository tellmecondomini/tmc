package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Morador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Morador entity.
 */
public interface MoradorRepository extends JpaRepository<Morador, Long> {

    Morador findOneByEmail(String email);

    List<Morador> findAllByCondominio(Condominio condominio);
}
