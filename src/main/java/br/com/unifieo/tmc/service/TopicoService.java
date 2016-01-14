package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Topico;
import br.com.unifieo.tmc.repository.TopicoRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class TopicoService {

    private final Logger log = LoggerFactory.getLogger(TopicoService.class);

    private final TopicoRepository topicoRepository;

    @Inject
    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    public Topico save(Topico topico) {
        topico.setData(new DateTime());
        topico.setAprovado(false);
        Topico topicoSaved = this.topicoRepository.save(topico);
        return topicoSaved;
    }
}
