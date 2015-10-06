package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.DisponibilidadeDependencia;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DisponibilidadeDependencia entity.
 */
public interface DisponibilidadeDependenciaRepository extends JpaRepository<DisponibilidadeDependencia,Long> {

}
