package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.TelefonePrestadorServico;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TelefonePrestadorServico entity.
 */
public interface TelefonePrestadorServicoRepository extends JpaRepository<TelefonePrestadorServico,Long> {

}
