package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Cep;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.CondominioRepository;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.web.rest.dto.FuncionarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@Transactional
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final CepRepository cepRepository;
    private final CondominioRepository condominioRepository;

    @Inject
    public FuncionarioService(FuncionarioRepository funcionarioRepository, CepRepository cepRepository, CondominioRepository condominioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.cepRepository = cepRepository;
        this.condominioRepository = condominioRepository;
    }

    public Funcionario save(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = new Funcionario(funcionarioDTO);

        Cep cep = Optional.ofNullable(cepRepository.findOneByCep(funcionario.getCep().getCep()))
            .orElseGet(() -> cepRepository.save(funcionario.getCep()));

        funcionario.setCep(cep);

        Condominio condominio = condominioRepository.findOne(funcionarioDTO.getCondominioId());
        funcionario.setCondominio(condominio);

        Funcionario funcionarioSaved = funcionarioRepository.save(funcionario);
        funcionarioDTO.setId(funcionarioSaved.getId());

        return funcionarioSaved;
    }
}
