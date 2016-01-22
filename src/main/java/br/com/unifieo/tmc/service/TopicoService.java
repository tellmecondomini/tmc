package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.Topico;
import br.com.unifieo.tmc.domain.User;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.repository.MoradorRepository;
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
    private final UserService userService;
    private final FuncionarioRepository funcionarioRepository;
    private final MoradorRepository moradorRepository;

    @Inject
    public TopicoService(TopicoRepository topicoRepository, UserService userService,
                         FuncionarioRepository funcionarioRepository, MoradorRepository moradorRepository) {
        this.topicoRepository = topicoRepository;
        this.userService = userService;
        this.funcionarioRepository = funcionarioRepository;
        this.moradorRepository = moradorRepository;
    }

    public Topico save(Topico topico) {

        User user = userService.getUserWithAuthorities();
        Funcionario funcionario = funcionarioRepository.findOneByEmail(user.getEmail());
        if (funcionario == null) {
            Morador morador = moradorRepository.findOneByEmail(user.getEmail());
            if (morador != null)
                topico.setMorador(morador);
        } else {
            topico.setFuncionario(funcionario);
        }

        topico.setData(new DateTime());
        Topico topicoSaved = this.topicoRepository.save(topico);
        return topicoSaved;
    }
}
