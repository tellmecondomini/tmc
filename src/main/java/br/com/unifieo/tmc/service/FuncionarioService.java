package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.web.rest.dto.FuncionarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class FuncionarioService {

    private final Logger log = LoggerFactory.getLogger(FuncionarioService.class);

    private final FuncionarioRepository funcionarioRepository;
    private final CepRepository cepRepository;

    @Inject
    public FuncionarioService(FuncionarioRepository funcionarioRepository, CepRepository cepRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.cepRepository = cepRepository;
    }

    public Funcionario save(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = new Funcionario(funcionarioDTO);
        cepRepository.save(funcionario.getCep());
        Funcionario funcionarioSaved = funcionarioRepository.save(funcionario);
        funcionarioDTO.setId(funcionarioSaved.getId());
        return funcionarioSaved;
    }
}
