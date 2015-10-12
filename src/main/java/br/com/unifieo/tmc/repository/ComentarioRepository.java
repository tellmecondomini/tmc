package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Comentario;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Comentario entity.
 */
public interface ComentarioRepository extends JpaRepository<Comentario,Long> {

}
