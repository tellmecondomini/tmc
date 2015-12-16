package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Funcionario;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Funcionario entity.
 */
public interface FuncionarioRepository extends JpaRepository<Funcionario,Long> {

    Funcionario findOneByCondominioAndResponsavel(Condominio condominio, boolean responsavel);
}
