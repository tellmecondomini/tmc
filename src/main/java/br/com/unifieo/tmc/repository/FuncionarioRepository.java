package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Funcionario entity.
 */
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findOneByCondominioAndResponsavel(Condominio condominio, boolean responsavel);

    Funcionario findOneByEmail(String email);

    List<Funcionario> findAllByCondominio(Condominio condominio);
}
