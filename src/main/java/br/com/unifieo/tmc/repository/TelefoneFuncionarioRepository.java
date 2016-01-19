package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.TelefoneFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TelefoneFuncionario entity.
 */
public interface TelefoneFuncionarioRepository extends JpaRepository<TelefoneFuncionario,Long> {

}
