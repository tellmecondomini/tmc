package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Dependencia;
import br.com.unifieo.tmc.repository.DependenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class DependenciaService {

    private final Logger log = LoggerFactory.getLogger(DependenciaService.class);

    private final DependenciaRepository dependenciaRepository;

    @Inject
    public DependenciaService(DependenciaRepository dependenciaRepository) {
        this.dependenciaRepository = dependenciaRepository;
    }

    public Dependencia save(Dependencia dependencia) {
        dependencia.setDisponivel(true);
        return dependenciaRepository.save(dependencia);
    }
}
