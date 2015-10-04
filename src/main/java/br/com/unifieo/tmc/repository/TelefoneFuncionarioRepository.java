package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.TelefoneFuncionario;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TelefoneFuncionario entity.
 */
public interface TelefoneFuncionarioRepository extends JpaRepository<TelefoneFuncionario,Long> {

}
