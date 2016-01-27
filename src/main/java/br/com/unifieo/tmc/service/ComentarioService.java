package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.*;
import br.com.unifieo.tmc.repository.*;
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
    private final SolicitaRemocaoComentarioRepository solicitaRemocaoComentarioRepository;

    @Inject
    public ComentarioService(ComentarioRepository comentarioRepository, UserRepository userRepository,
                             FuncionarioRepository funcionarioRepository, MoradorRepository moradorRepository,
                             SolicitaRemocaoComentarioRepository solicitaRemocaoComentarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.userRepository = userRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.moradorRepository = moradorRepository;
        this.solicitaRemocaoComentarioRepository = solicitaRemocaoComentarioRepository;
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
        comentario.setAtivo(true);
        Comentario comentarioSaved = comentarioRepository.save(comentario);
        return comentarioSaved;
    }

    public void getSolicitacaoRemocaoByMorador(Comentario comentario, Long moradorId, String motivo) {
        Morador morador = moradorRepository.findOne(moradorId);

        SolicitaRemocaoComentario remocaoComentario = new SolicitaRemocaoComentario();
        remocaoComentario.setData(new DateTime());
        remocaoComentario.setComentario(comentario);
        remocaoComentario.setMorador(morador);
        remocaoComentario.setMotivo(motivo);
        solicitaRemocaoComentarioRepository.save(remocaoComentario);
    }
}
