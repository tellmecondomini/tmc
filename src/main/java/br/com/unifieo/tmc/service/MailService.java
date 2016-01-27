package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.*;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private UserService userService;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("mail.from");
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendNewUserEmail(User user, Funcionario funcionario, String baseUrl) {
        log.debug("E-mail de confirmação de cadastro e aviso aos gestores");
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("condominio", funcionario.getCondominio());
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("newUser", context);
        String subject = "TMC - Cadastro efetuado";
        this.sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = "TMC - Alteração de Senha";
        this.sendEmail(user.getEmail(), subject, content, false, true);
    }

    public void sendNewFuncionarioEmail(Funcionario funcionario, User user, String baseUrl) {
        log.debug("Novo funcionario criado '{}'", funcionario.getNome());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("newFuncionarioEmail", context);
        String subject = "TMC - Novo Funcionario";
        this.sendEmail(funcionario.getEmail(), subject, content, false, true);
    }

    public void sendNewMoradorEmail(Morador morador, User user, String baseUrl) {
        log.debug("Novo morador criado '{}'", morador.getNome());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("newMoradorEmail", context);
        String subject = "TMC - Novo Morador";
        this.sendEmail(morador.getEmail(), subject, content, false, true);
    }

    public void sendTopicoAprovado(Topico topico, String baseUrl) {
        log.debug("Novo tópico aprovado '{}'", topico.getTitulo());
        User user = userService.getUserWithAuthorities();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("topico", topico);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("topicoAprovado", context);
        String subject = "TMC - Topico Aprovado";
        this.sendEmail(topico.getEmail(), subject, content, false, true);
    }

    public void sendTopicoReprovado(Topico topico, String baseUrl) {
        log.debug("Tópico reprovado '{}'", topico.getTitulo());
        User user = userService.getUserWithAuthorities();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("topico", topico);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("topicoReprovado", context);
        String subject = "TMC - Topico Reprovado";
        this.sendEmail(topico.getEmail(), subject, content, false, true);
    }

    public void sendEncerramentoTopico(Topico topico, Comentario comentario, String baseUrl) {
        log.debug("Tópico encerrado '{}'", topico.getTitulo());
        User user = userService.getUserWithAuthorities();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("topico", topico);
        context.setVariable("comentario", comentario);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("topicoEncerrado", context);
        String subject = "TMC - Topico Encerrado";
        this.sendEmail(topico.getEmail(), subject, content, false, true);
    }

    public void sendSolicitacaoRemocaoComentario(SolicitaRemocaoComentario solicitacao, String baseUrl) {
        log.debug("Solicitacao de remocao de comentario '{}'", solicitacao.getComentario().getConteudo());
        User user = userService.getUserWithAuthorities();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("solicitacao", solicitacao);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("solicitacaoRemocaoComentario", context);
        String subject = "TMC - Remoção de Comentário";
        this.sendEmail(solicitacao.getMorador().getEmail(), subject, content, false, true);
    }
}
