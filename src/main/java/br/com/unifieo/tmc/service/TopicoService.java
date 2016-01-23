package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.*;
import br.com.unifieo.tmc.repository.ComentarioRepository;
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
    private final MailService mailService;
    private final ComentarioRepository comentarioRepository;

    @Inject
    public TopicoService(TopicoRepository topicoRepository, UserService userService,
                         FuncionarioRepository funcionarioRepository, MoradorRepository moradorRepository,
                         MailService mailService, ComentarioRepository comentarioRepository) {
        this.topicoRepository = topicoRepository;
        this.userService = userService;
        this.funcionarioRepository = funcionarioRepository;
        this.moradorRepository = moradorRepository;
        this.mailService = mailService;
        this.comentarioRepository = comentarioRepository;
    }

    public Topico save(Topico topico) {

        User user = userService.getUserWithAuthorities();
        Funcionario funcionario = funcionarioRepository.findOneByEmail(user.getEmail());
        if (funcionario == null) {
            Morador morador = moradorRepository.findOneByEmail(user.getEmail());
            if (morador != null) {
                topico.setMorador(morador);
                topico.setStatusTopico(StatusTopico.AGUARDANDO_APROVACAO);
            }
        } else {
            topico.setFuncionario(funcionario);
            topico.setStatusTopico(StatusTopico.ABERTO);
        }

        topico.setData(new DateTime());
        Topico topicoSaved = this.topicoRepository.save(topico);
        return topicoSaved;
    }

    public Topico getAprovacaoTopico(Topico topico, String status, String mensagem, String baseUrl) {

        User user = userService.getUserWithAuthorities();
        Funcionario funcionario = funcionarioRepository.findOneByEmail(user.getEmail());

        topico.setMensagemAprovacao(mensagem);
        topico.setFuncionarioAprovacao(funcionario);

        if ("ABERTO".equals(status)) {
            topico.setStatusTopico(StatusTopico.ABERTO);
            mailService.sendTopicoAprovado(topico, baseUrl);
        } else {
            topico.setStatusTopico(StatusTopico.REPROVADO);
            mailService.sendTopicoReprovado(topico, baseUrl);
        }

        return topicoRepository.save(topico);
    }

    public Topico getEncerramentoTopico(Topico topico, String solucao, String observacao, String baseUrl) {

        User user = userService.getUserWithAuthorities();
        Funcionario funcionario = funcionarioRepository.findOneByEmail(user.getEmail());

        if ("SIM".equals(solucao)) {
            topico.setStatusTopico(StatusTopico.ENCERRADO_COM_SUCESSO);
        } else {
            topico.setStatusTopico(StatusTopico.ENCERRADO_SEM_SUCESSO);
        }

        Topico topicoSaved = topicoRepository.save(topico);

        Comentario comentario = new Comentario();
        comentario.setTopico(topicoSaved);
        comentario.setAtivo(true);
        comentario.setData(new DateTime());
        comentario.setFuncionario(funcionario);
        comentario.setConteudo(observacao);

        Comentario comentarioSaved = comentarioRepository.save(comentario);

        mailService.sendEncerramentoTopico(topicoSaved, comentarioSaved, baseUrl);

        return topicoSaved;
    }
}
