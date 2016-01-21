package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Cep;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.CondominioRepository;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.repository.TelefoneCondominioRepository;
import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@Transactional
public class CondominioService {

    private final Logger log = LoggerFactory.getLogger(CondominioService.class);

    private final CondominioRepository condominioRepository;
    private final CepRepository cepRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TelefoneCondominioRepository telefoneCondominioRepository;

    @Inject
    public CondominioService(CondominioRepository condominioRepository, CepRepository cepRepository,
                             FuncionarioRepository funcionarioRepository,
                             TelefoneCondominioRepository telefoneCondominioRepository) {
        this.condominioRepository = condominioRepository;
        this.cepRepository = cepRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.telefoneCondominioRepository = telefoneCondominioRepository;
    }

    public Condominio save(CondominioDTO condominioDto) {

        Condominio condominio = new Condominio(condominioDto);

        Cep cep = Optional.ofNullable(cepRepository.findOneByCep(condominioDto.getCondominioCep()))
            .orElseGet(() -> cepRepository.save(condominio.getCep()));

        condominio.setCep(cep);

        Condominio condominioSaved = condominioRepository.save(condominio);

        telefoneCondominioRepository.save(condominio.getTelefoneCondominios());

        return condominioRepository.findOne(condominioSaved.getId());
    }

    public void delete(Long id) {
        Condominio condominio = condominioRepository.findOne(id);
        Funcionario funcionario = funcionarioRepository.findOneByCondominioAndResponsavel(condominio, true);
        funcionario.setResponsavel(false);
        funcionario.setCondominio(null);
        funcionarioRepository.save(funcionario);
        condominioRepository.delete(id);
    }

    public Condominio findOneByRazaoSocial(String razaoSocial) {
        return condominioRepository.findOneByRazaoSocial(razaoSocial).get();
    }
}
