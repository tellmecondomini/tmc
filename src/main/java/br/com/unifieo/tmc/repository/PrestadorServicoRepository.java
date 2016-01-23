package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.PrestadorServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the PrestadorServico entity.
 */
public interface PrestadorServicoRepository extends JpaRepository<PrestadorServico,Long> {

    List<PrestadorServico> findAllByCondominio(Condominio condominio);
}
