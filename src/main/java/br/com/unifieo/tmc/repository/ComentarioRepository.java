package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Comentario entity.
 */
public interface ComentarioRepository extends JpaRepository<Comentario,Long> {

    List<Comentario> findAllByTopico(Topico topico);

    List<Comentario> findAllByMorador(Morador morador);
}
