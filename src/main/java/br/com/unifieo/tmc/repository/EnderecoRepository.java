package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Endereco;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Endereco entity.
 */
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {

}
