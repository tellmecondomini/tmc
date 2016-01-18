package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Categoria;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Categoria entity.
 */
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

}
