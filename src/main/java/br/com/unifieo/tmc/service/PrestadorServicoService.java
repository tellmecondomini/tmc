package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.CompetenciaPrestadorRepository;
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
    private final CompetenciaPrestadorRepository competenciaPrestadorRepository;

    @Inject
    public PrestadorServicoService(PrestadorServicoRepository prestadorServicoRepository, CepRepository cepRepository,
                                   CompetenciaPrestadorRepository competenciaPrestadorRepository) {
        this.prestadorServicoRepository = prestadorServicoRepository;
        this.cepRepository = cepRepository;
        this.competenciaPrestadorRepository = competenciaPrestadorRepository;
    }

    public PrestadorServico save(PrestadorServico prestadorServico) {
        cepRepository.save(prestadorServico.getCep());
        competenciaPrestadorRepository.save(prestadorServico.getCompetenciaPrestador());
        return prestadorServicoRepository.save(prestadorServico);
    }
}
