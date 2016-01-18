package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.ImagemTopico;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ImagemTopico entity.
 */
public interface ImagemTopicoRepository extends JpaRepository<ImagemTopico,Long> {

}
