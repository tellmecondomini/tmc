package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Categoria entity.
 */
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

}
