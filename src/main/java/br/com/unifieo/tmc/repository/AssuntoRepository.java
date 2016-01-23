package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Assunto;
import br.com.unifieo.tmc.domain.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Assunto entity.
 */
public interface AssuntoRepository extends JpaRepository<Assunto,Long> {

    List<Assunto> findAllByCondominio(Condominio condominio);
}
