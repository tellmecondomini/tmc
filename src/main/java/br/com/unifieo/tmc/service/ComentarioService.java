package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.User;
import br.com.unifieo.tmc.repository.ComentarioRepository;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.repository.MoradorRepository;
import br.com.unifieo.tmc.repository.UserRepository;
import br.com.unifieo.tmc.security.SecurityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class ComentarioService {

    private final Logger log = LoggerFactory.getLogger(ComentarioService.class);

    private final ComentarioRepository comentarioRepository;
    private final UserRepository userRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final MoradorRepository moradorRepository;

    @Inject
    public ComentarioService(ComentarioRepository comentarioRepository, UserRepository userRepository,
                             FuncionarioRepository funcionarioRepository, MoradorRepository moradorRepository) {
        this.comentarioRepository = comentarioRepository;
        this.userRepository = userRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.moradorRepository = moradorRepository;
    }

    public Comentario save(Comentario comentario) {
        User user = userRepository.findOneByEmail(SecurityUtils.getCurrentLogin()).get();
        Funcionario funcionario = funcionarioRepository.findOneByEmail(user.getEmail());
        if (funcionario == null) {
            Morador morador = moradorRepository.findOneByEmail(user.getEmail());
            if (morador != null)
                comentario.setMorador(morador);
        } else {
            comentario.setFuncionario(funcionario);
        }
        comentario.setData(new DateTime());
        comentario.setAtivo(false);
        Comentario comentarioSaved = comentarioRepository.save(comentario);
        return comentarioSaved;
    }
}
