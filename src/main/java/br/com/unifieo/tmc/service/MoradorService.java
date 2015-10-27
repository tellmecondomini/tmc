package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.repository.MoradorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class MoradorService {

    private final Logger log = LoggerFactory.getLogger(MoradorService.class);

    private final MoradorRepository moradorRepository;

    @Inject
    public MoradorService(MoradorRepository moradorRepository) {
        this.moradorRepository = moradorRepository;
    }

    public Morador save(Morador morador) {
        morador.setAtivo(true);
        morador.setBloqueiaAgendamento(false);
        return this.moradorRepository.save(morador);
    }
}
