package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.PrestadorServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class PrestadorServicoService {

    private final Logger log = LoggerFactory.getLogger(PrestadorServicoService.class);

    private final PrestadorServicoRepository prestadorServicoRepository;
    private final CepRepository cepRepository;

    @Inject
    public PrestadorServicoService(PrestadorServicoRepository prestadorServicoRepository, CepRepository cepRepository) {
        this.prestadorServicoRepository = prestadorServicoRepository;
        this.cepRepository = cepRepository;
    }

    public PrestadorServico save(PrestadorServico prestadorServico) {
        cepRepository.save(prestadorServico.getCep());
        return prestadorServicoRepository.save(prestadorServico);
    }
}
