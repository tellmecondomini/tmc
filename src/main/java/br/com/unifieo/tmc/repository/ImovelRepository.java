package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Imovel entity.
 */
public interface ImovelRepository extends JpaRepository<Imovel,Long> {

}
