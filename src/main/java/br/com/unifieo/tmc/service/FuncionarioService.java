package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import org.joda.time.DateTime;
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

    @Inject
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }


    public Funcionario save(Funcionario funcionario) {
        funcionario.setAtivo(true);
        funcionario.setDataCadastro(new DateTime());
        return this.funcionarioRepository.save(funcionario);
    }
}
