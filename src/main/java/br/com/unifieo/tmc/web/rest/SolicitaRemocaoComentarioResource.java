package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.SolicitaRemocaoComentario;
import br.com.unifieo.tmc.domain.Topico;
import br.com.unifieo.tmc.repository.ComentarioRepository;
import br.com.unifieo.tmc.repository.SolicitaRemocaoComentarioRepository;
import br.com.unifieo.tmc.service.CondominioService;
import br.com.unifieo.tmc.service.MailService;
import br.com.unifieo.tmc.service.UserService;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing SolicitaRemocaoComentario.
 */
@RestController
@RequestMapping("/api")
public class SolicitaRemocaoComentarioResource {

    private final Logger log = LoggerFactory.getLogger(SolicitaRemocaoComentarioResource.class);

    @Inject
    private SolicitaRemocaoComentarioRepository solicitaRemocaoComentarioRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    @Inject
    private ComentarioRepository comentarioRepository;

    @Inject
    private CondominioService condominioService;

    /**
     * POST  /solicitaRemocaoComentarios -> Create a new solicitaRemocaoComentario.
     */
    @RequestMapping(value = "/solicitaRemocaoComentarios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitaRemocaoComentario> createSolicitaRemocaoComentario(@RequestBody SolicitaRemocaoComentario solicitaRemocaoComentario) throws URISyntaxException {
        log.debug("REST request to save SolicitaRemocaoComentario : {}", solicitaRemocaoComentario);
        if (solicitaRemocaoComentario.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new solicitaRemocaoComentario cannot already have an ID").body(null);
        }
        SolicitaRemocaoComentario result = solicitaRemocaoComentarioRepository.save(solicitaRemocaoComentario);
        return ResponseEntity.created(new URI("/api/solicitaRemocaoComentarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("solicitaRemocaoComentario", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /solicitaRemocaoComentarios -> Updates an existing solicitaRemocaoComentario.
     */
    @RequestMapping(value = "/solicitaRemocaoComentarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitaRemocaoComentario> updateSolicitaRemocaoComentario(@RequestBody SolicitaRemocaoComentario solicitaRemocaoComentario) throws URISyntaxException {
        log.debug("REST request to update SolicitaRemocaoComentario : {}", solicitaRemocaoComentario);
        if (solicitaRemocaoComentario.getId() == null) {
            return createSolicitaRemocaoComentario(solicitaRemocaoComentario);
        }
        SolicitaRemocaoComentario result = solicitaRemocaoComentarioRepository.save(solicitaRemocaoComentario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("solicitaRemocaoComentario", solicitaRemocaoComentario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /solicitaRemocaoComentarios -> get all the solicitaRemocaoComentarios.
     */
    @RequestMapping(value = "/solicitaRemocaoComentarios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SolicitaRemocaoComentario> getAllSolicitaRemocaoComentarios() {
        log.debug("REST request to get all SolicitaRemocaoComentarios");
        final Condominio condominio = condominioService.getCurrentCondominio();
        return solicitaRemocaoComentarioRepository.findAll()
            .stream()
            .filter(s -> s.getMorador().getCondominio().equals(condominio))
            .collect(Collectors.toList());
    }

    /**
     * GET  /solicitaRemocaoComentarios/:id -> get the "id" solicitaRemocaoComentario.
     */
    @RequestMapping(value = "/solicitaRemocaoComentarios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitaRemocaoComentario> getSolicitaRemocaoComentario(@PathVariable Long id) {
        log.debug("REST request to get SolicitaRemocaoComentario : {}", id);
        return Optional.ofNullable(solicitaRemocaoComentarioRepository.findOne(id))
            .map(solicitaRemocaoComentario -> new ResponseEntity<>(
                solicitaRemocaoComentario,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /solicitaRemocaoComentarios/:id -> delete the "id" solicitaRemocaoComentario.
     */
    @RequestMapping(value = "/solicitaRemocaoComentarios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSolicitaRemocaoComentario(@PathVariable Long id) {
        log.debug("REST request to delete SolicitaRemocaoComentario : {}", id);
        solicitaRemocaoComentarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("solicitaRemocaoComentario", id.toString())).build();
    }

    @RequestMapping(value = "/solicitaRemocaoComentarios/aprovacao/{id}/{aprovado}/{observacao}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> executeAprovacao(@PathVariable Long id,
                                                 @PathVariable Boolean aprovado,
                                                 @PathVariable byte[] observacao,
                                                 HttpServletRequest request) {
        log.debug("REST request to get SolicitaRemocaoComentario aprovacao/reprovacao: {}", id);

        SolicitaRemocaoComentario remocaoComentario = solicitaRemocaoComentarioRepository.findOne(id);
        remocaoComentario.setObservacao(new String(observacao, StandardCharsets.UTF_8));
        remocaoComentario.setFuncionario(userService.getFuncionarioAtual());
        remocaoComentario.setAprovado(aprovado);
        remocaoComentario.setDataAtendimento(new DateTime());

        SolicitaRemocaoComentario save = solicitaRemocaoComentarioRepository.save(remocaoComentario);

        Comentario comentario = comentarioRepository.findOne(remocaoComentario.getComentario().getId());
        comentario.setAtivo(!aprovado);
        comentarioRepository.save(comentario);

        String baseUrl = request.getScheme() +
            "://" +
            request.getServerName() +
            ":" +
            request.getServerPort();

        mailService.sendSolicitacaoRemocaoComentario(save, baseUrl);

        return ResponseEntity.ok().build();
    }
}
