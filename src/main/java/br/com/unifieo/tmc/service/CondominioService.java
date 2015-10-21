package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.repository.CondominioRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;

@Service
@Transactional
public class CondominioService {

    private final Logger log = LoggerFactory.getLogger(CondominioService.class);

    private final CondominioRepository condominioRepository;

    @Inject
    public CondominioService(CondominioRepository condominioRepository) {
        this.condominioRepository = condominioRepository;
    }

    public Condominio save(Condominio condominio) {
        condominio.setDataCadastro(new DateTime());
        condominio.setAtivo(Boolean.TRUE);
        return condominioRepository.save(condominio);
    }
}
