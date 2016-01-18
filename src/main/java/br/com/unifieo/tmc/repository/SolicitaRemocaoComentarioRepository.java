package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.SolicitaRemocaoComentario;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SolicitaRemocaoComentario entity.
 */
public interface SolicitaRemocaoComentarioRepository extends JpaRepository<SolicitaRemocaoComentario,Long> {

}
